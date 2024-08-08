import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
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
  @override
  void initState() {
    print(" ===========tabs were started ====================");
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
        return AlertDialog(
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
                onPressed: () async {
                  final String enteredGroupId = groupIdController.text.trim();
                  context
                      .read<TodoBloc>()
                      .add(CheckGroupIdExists(enteredGroupId));
                  // Listen to the bloc's state to determine existence
                  final state = context.read<TodoBloc>().state;
                  if (state.groupIdExists) {
                    await prefs.setString('group_id', enteredGroupId);
                    context.read<TodoBloc>().add(LoadItems(enteredGroupId));
                    Navigator.of(context).pop();
                  } else {
                    // Show error message
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('Group ID does not exist.')),
                    );
                  }
                },
                child: Text('Submit'),
              ),
              ElevatedButton(
                onPressed: () async {
                  // Trigger the CreateGroup event
                  context.read<TodoBloc>().add(CreateGroup());

                  // Get the current state after the event is processed
                  // final state = context.read<TodoBloc>().state;
                  // if (state.newGroupId != null) {
                  //   // Store the newGroupId in SharedPreferences
                  //   await prefs.setString('group_id', state.newGroupId!);
                  //   context.read<TodoBloc>().add(LoadItems(state.newGroupId!));
                  //   Navigator.of(context).pop();
                  // }
                  // final newGroupId = await context.read<TodoBloc>().createGroup()

                  // String newGroupId = await context.read<TodoBloc>().add(CreateGroup()); // Capture the returned group ID
                  // await prefs.setString('group_id', newGroupId);
                  // context.read<TodoBloc>().add(LoadItems(newGroupId));
                  // Navigator.of(context).pop();

                  // context.read<TodoBloc>().add(CreateGroup());
                  // await prefs.setString('group_id', newGroupId);
                  // context.read<TodoBloc>().add(LoadItems(newGroupId));
                  Navigator.of(context).pop();
                  // You may need to listen for the new group ID as well, depending on your flow
                },
                child: Text('Create New Group'),
              ),
            ],
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
  //                 if (await checkGroupIdExists(enteredGroupId)) {
  //                   await prefs.setString('group_id', enteredGroupId);
  //                   context.read<TodoBloc>().add(LoadItems(enteredGroupId));
  //                   Navigator.of(context).pop();
  //                 } else {
  //                   // Show error message
  //                   ScaffoldMessenger.of(context).showSnackBar(
  //                     SnackBar(content: Text('Group ID does not exist.')),
  //                   );
  //                 }
  //               },
  //               child: Text('Submit'),
  //             ),
  //             ElevatedButton(
  //               onPressed: () async {
  //                 final String newGroupId = await createGroup();
  //                 await prefs.setString('group_id', newGroupId);
  //                 context.read<TodoBloc>().add(LoadItems(newGroupId));
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
    final String? groupId = prefs.getString('group_id');
    final String? userId = prefs.getString('user_id');

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
        if (state.newGroupId != null) {
          // Store the newGroupId in SharedPreferences
          final prefs = await SharedPreferences.getInstance();
          await prefs.setString('group_id', state.newGroupId!);
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
