import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/Helper.dart';
import 'package:house_mate/blocs/todo_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:shared_preferences/shared_preferences.dart';

Future<void> showChangeUsernameDialog(BuildContext context) async {
  final TextEditingController userNameController = TextEditingController();
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final Helper helper = Helper();

  showDialog(
    context: context,
    builder: (context) {
      String savedName =
          prefs.getString(helper.USER_NAME_SP) ?? helper.ANON_STRING;
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
              }
              Navigator.of(context).pop();
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
                context.read<TodoBloc>().add(CheckGroupIdExistsAndJoin(group));
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
