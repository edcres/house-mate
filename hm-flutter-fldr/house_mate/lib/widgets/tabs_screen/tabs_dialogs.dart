import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/Helper.dart';
import 'package:house_mate/blocs/todo_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../data/models/todo_item.dart';

class AddItemDialog extends StatefulWidget {
  final ItemType initialItemType;

  AddItemDialog({required this.initialItemType});

  @override
  _AddItemDialogState createState() => _AddItemDialogState();
}

class _AddItemDialogState extends State<AddItemDialog> {
  final TextEditingController _controller = TextEditingController();
  late ItemType _selectedItemType;

  @override
  void initState() {
    super.initState();
    _selectedItemType = widget.initialItemType;
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Add Item'),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          TextField(
            controller: _controller,
            decoration: InputDecoration(hintText: 'Enter item here'),
          ),
          DropdownButton<ItemType>(
            value: _selectedItemType,
            onChanged: (ItemType? newValue) {
              setState(() {
                _selectedItemType = newValue!;
              });
            },
            items: ItemType.values.map((ItemType classType) {
              return DropdownMenuItem<ItemType>(
                value: classType,
                child: Text(classType.toString().split('.').last),
              );
            }).toList(),
          ),
        ],
      ),
      actions: [
        TextButton(
          onPressed: () {
            final item = _controller.text;
            if (item.isNotEmpty) {
              context.read<TodoBloc>().add(AddItem(item, _selectedItemType));
            }
            Navigator.of(context).pop();
          },
          child: Text('Add'),
        ),
      ],
    );
  }
}

Future<void> showChangeUsernameDialog(BuildContext context) async {
  final TextEditingController userNameController = TextEditingController();
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final Helper helper = Helper();

  showDialog(
    context: context,
    builder: (context) {
      String savedName = prefs.getString(helper.USER_NAME_SP) ?? "Anon";
      return AlertDialog(
        title: Text('Change Username'),
        content: TextField(
          controller: userNameController,
          decoration: InputDecoration(hintText: savedName),
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.of(context).pop();
            },
            child: Text('Cancel'),
          ),
          TextButton(
            onPressed: () async {
              final String userName = userNameController.text.trim();
              if (userName.isNotEmpty) {
                // Save the username in shared preferences and bloc.
                await prefs.setString(helper.USER_NAME_SP, userName);
                context.read<TodoBloc>().add(SetUserName(userName));

                Navigator.of(context).pop();
              } else {
                // Show a message or handle the case where the username is empty
              }
            },
            child: Text('Save'),
          ),
        ],
      );
    },
  );
}

Future<void> showPastGroupsDialog(BuildContext context) async {
  final prefs = await SharedPreferences.getInstance();
  final Helper helper = Helper();
  // Retrieve the past groups string from SharedPreferences
  String pastGroups =
      await prefs.getString(helper.PAST_GROUPS) ?? helper.NULL_STRING;
  final List<String> pastGroupsList = helper.pastGroupsToList(pastGroups);

  showDialog(
    context: context,
    builder: (context) {
      return AlertDialog(
        title: Text('Past Groups'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: pastGroupsList.reversed.map((group) {
            return ListTile(
              title: Text(helper.dashId(group)),
              onTap: () {
                Navigator.of(context).pop();
                // TODO: I shouldn't have to use both the functions here.
                context.read<TodoBloc>().add(CheckGroupIdExistsAndJoin(group));
                // context.read<TodoBloc>().add(LoadItems(group));
              },
            );
          }).toList(),
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
