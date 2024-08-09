import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/Helper.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../blocs/todo_bloc.dart';
import 'items_screen.dart';
import '../data/models/todo_item.dart';
import '../widgets/add_item_dialog.dart';

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
      WidgetsBinding.instance?.addPostFrameCallback((_) {
        _showGroupIdDialog(context);
      });
    }
  }

  Future<void> _showGroupIdDialog(BuildContext context) async {
    final TextEditingController groupIdController = TextEditingController();
    final SharedPreferences prefs = await SharedPreferences.getInstance();

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return BlocListener<TodoBloc, TodoState>(
          listener: (context, state) {
            // Adjust this condition based on your actual state for checking group ID existence
            print(
                "----------------------------  this happened -1   ------------------------------");
            if (state.groupIdExists) {
              print(
                  "----------------------------  this happened 0   ------------------------------");
              final String enteredGroupId = groupIdController.text.trim();
              prefs.setString(helper.GROUP_ID_SP, enteredGroupId);
              print(
                  "----------------------------  this happened 0.5   ------------------------------");
              context.read<TodoBloc>().add(JoinGroup(enteredGroupId));
              print(
                  "----------------------------  ^^^this happened 0.75   ------------------------------");
              context.read<TodoBloc>().add(LoadItems(enteredGroupId));
              print(
                  "----------------------------  this happened 1   ------------------------------");
              Navigator.of(context).pop(); // Close dialog
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
                        .add(CheckGroupIdExists(enteredGroupId));
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

  // Future<void> _showGroupIdDialog(BuildContext context) async {
  //   final TextEditingController groupIdController = TextEditingController();
  //   final SharedPreferences prefs = await SharedPreferences.getInstance();

  //   showDialog(
  //     context: context,
  //     barrierDismissible: false,
  //     builder: (BuildContext context) {
  //       return AlertDialog(
  //         title: Text('Enter Group ID'),
  //         content: Column(
  //           mainAxisSize: MainAxisSize.min,
  //           children: [
  //             TextField(
  //               controller: groupIdController,
  //               decoration: InputDecoration(hintText: 'Group ID'),
  //             ),
  //             SizedBox(height: 20),
  //             ElevatedButton(
  //               onPressed: () async {
  //                 final String enteredGroupId = groupIdController.text.trim();
  //                 context
  //                     .read<TodoBloc>()
  //                     .add(CheckGroupIdExists(enteredGroupId));
  //                 print(
  //                     "-------------------------  this happened -2  ------------------------------------------------");
  //                 // TODO: remove this
  //                 // Listen to the bloc's state to determine existence
  //                 // final state = context.read<TodoBloc>().state;
  //                 // if (state.groupIdExists) {
  //                 //   await prefs.setString(helper.GROUP_ID_SP, enteredGroupId);
  //                 //   context.read<TodoBloc>().add(JoinGroup(enteredGroupId));
  //                 //   context.read<TodoBloc>().add(LoadItems(enteredGroupId));
  //                 //   Navigator.of(context).pop();
  //                 // } else {
  //                 //   // Show error message
  //                 //   ScaffoldMessenger.of(context).showSnackBar(
  //                 //     SnackBar(content: Text('Group ID does not exist.')),
  //                 //   );
  //                 // }
  //               },
  //               child: Text('Submit'),
  //             ),
  //             ElevatedButton(
  //               onPressed: () async {
  //                 // Trigger the CreateGroup event
  //                 context.read<TodoBloc>().add(CreateGroup());
  //                 Navigator.of(context).pop();
  //               },
  //               child: Text('Create New Group'),
  //             ),
  //           ],
  //         ),
  //       );
  //     },
  //   );
  // }

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
              Text('Current Group ID: $groupId'),
              Text('Current User ID: $userId'),
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
                      DefaultTabController.of(context)!.index;
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
