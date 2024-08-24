import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/Helper.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import 'package:house_mate/widgets/tabs_screen/add_item_dialog.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../blocs/todo_bloc.dart';
import 'items_screen.dart';
import '../data/models/todo_item.dart';
import '../widgets/tabs_screen/tabs_dialogs.dart';

class TabsScreen extends StatefulWidget {
  final String? initialGroupId;

  TabsScreen({required this.initialGroupId});

  @override
  _TabsScreenState createState() => _TabsScreenState();
}

class _TabsScreenState extends State<TabsScreen> {
  final helper = Helper();
  @override
  void initState() {
    super.initState();
    if (widget.initialGroupId == null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _chooseGroupDialog(context);
      });
    }
  }

  Future<void> _chooseGroupDialog(BuildContext context) async {
    final TextEditingController groupIdController = TextEditingController();
    final SharedPreferences prefs = await SharedPreferences.getInstance();

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return BlocListener<TodoBloc, TodoState>(
          // TODO: There are 2 bloclisteners in this class, I can probably merge this one into the other one.
          listener: (context, state) {
            if (state.groupIdExists) {
              final String enteredGroupId = groupIdController.text.trim();

              // Join a new group if entered group is not the SP saved group.
              final String? spGroupId = prefs.getString(helper.GROUP_ID_SP);
              if (spGroupId != enteredGroupId) {
                prefs.setString(helper.GROUP_ID_SP, enteredGroupId);
                context.read<TodoBloc>().add(JoinGroup(enteredGroupId));
              }

              prefs.setString(helper.USER_ID_SP, state.userId!);
              context.read<TodoBloc>().add(LoadItems(enteredGroupId));
              // Add groupId to sp
              helper.saveGroupToSp(enteredGroupId);
              Navigator.of(context).pop();
            } else {
              // Show error message
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Group ID does not exist.')),
              );
            }
          },
          child: AlertDialog(
            title: Text('Enter Group ID'),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: groupIdController,
                  decoration: InputDecoration(hintText: 'Group ID'),
                ),
                SizedBox(height: 20),
                ElevatedButton(
                  onPressed: () {
                    final String enteredGroupId = groupIdController.text.trim();
                    context
                        .read<TodoBloc>()
                        .add(CheckGroupIdExistsAndJoin(enteredGroupId));
                  },
                  child: Text('Submit'),
                ),
                ElevatedButton(
                  onPressed: () {
                    // Trigger the CreateGroup event
                    context.read<TodoBloc>().add(CreateGroup());
                    Navigator.of(context).pop();
                  },
                  child: Text('Create New Group'),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Future<void> _showMoreOptionsDialog(BuildContext context) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? groupId = prefs.getString(helper.GROUP_ID_SP);
    final String? userId = prefs.getString(helper.USER_ID_SP);

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('More Options'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              ListTile(
                title: Text('Change Username'),
                onTap: () async {
                  // Handle Change Username action
                  Navigator.of(context).pop();
                  await showChangeUsernameDialog(context);
                },
              ),
              ListTile(
                title: Text('Past Groups'),
                onTap: () {
                  Navigator.of(context).pop();
                  showPastGroupsDialog(context);
                },
              ),
              ListTile(
                title: Text('Change Group'),
                onTap: () {
                  Navigator.of(context).pop();
                  _chooseGroupDialog(context); // Trigger the Group ID dialog
                },
              ),
              ListTile(
                title: Text('Current Group'),
                onTap: () {
                  Navigator.of(context).pop();
                  // Show current group and user ID
                  showDialog(
                    context: context,
                    builder: (context) {
                      return AlertDialog(
                        title: Text('Current Group and User ID'),
                        content: Column(
                          mainAxisSize: MainAxisSize.min,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text('Current Group ID: ${helper.dashId(groupId)}'),
                            Text('Current User ID: ${helper.dashId(userId)}'),
                          ],
                        ),
                        actions: [
                          TextButton(
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                            child: Text('Close'),
                          ),
                        ],
                      );
                    },
                  );
                },
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: Text('Close'),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<TodoBloc, TodoState>(
      // TODO: There are 2 bloclisteners in this class, I can probably merge this one into the other one.
      listener: (context, state) async {
        if (state.groupId != null) {
          final prefs = await SharedPreferences.getInstance();
          await prefs.setString(helper.GROUP_ID_SP, state.groupId!);
          await prefs.setString(helper.USER_ID_SP, state.userId!);
        }
      },
      child: DefaultTabController(
        length: 2,
        child: Scaffold(
          appBar: AppBar(
            title: Text('To-Do List'),
            bottom: TabBar(
              tabs: [
                Tab(text: 'Shopping'),
                Tab(text: 'Chores'),
              ],
            ),
            actions: [
              BlocBuilder<TodoBloc, TodoState>(
                builder: (context, state) {
                  return IconButton(
                    icon: Icon(state.isEditMode ? Icons.done : Icons.edit),
                    onPressed: () {
                      if (state.isEditMode) {
                        context.read<TodoBloc>().add(ExitEditMode());
                      } else {
                        context.read<TodoBloc>().add(EnterEditMode());
                      }
                    },
                  );
                },
              ),
              IconButton(
                icon: Icon(Icons.more_vert),
                onPressed: () {
                  _showMoreOptionsDialog(context);
                },
              ),
            ],
          ),
          body: TabBarView(
            children: [
              ItemsScreen(itemType: ItemType.Shopping),
              ItemsScreen(itemType: ItemType.Chore),
            ],
          ),
          floatingActionButton: Builder(
            builder: (context) {
              return FloatingActionButton(
                onPressed: () {
                  final int currentIndex =
                      DefaultTabController.of(context).index;
                  showDialog(
                    context: context,
                    builder: (context) {
                      return AddItemDialog(
                        initialItemType: currentIndex == 0
                            ? ItemType.Shopping
                            : ItemType.Chore,
                      );
                    },
                  );
                },
                child: Icon(Icons.add),
              );
            },
          ),
        ),
      ),
    );
  }
}
