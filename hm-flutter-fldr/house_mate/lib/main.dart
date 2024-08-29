import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/helper.dart';
import 'package:house_mate/firebase_options.dart';
import 'package:house_mate/screens/tabs_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'blocs/todo_bloc.dart';

void main() async {
  Helper helper = Helper();
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final String? groupId = prefs.getString(helper.GROUP_ID_SP);
  final String? userName = prefs.getString(helper.USER_NAME_SP);
  runApp(MyApp(groupId: groupId, userName: userName));
}

class MyApp extends StatelessWidget {
  final String? groupId;
  final String? userName;

  MyApp({required this.groupId, required this.userName});

  @override
  Widget build(BuildContext context) {
    Helper helper = Helper();
    return BlocProvider(
      create: (context) {
        final bloc = TodoBloc();
        bloc.add(SetGroupId(groupId ?? helper.NULL_STRING));
        bloc.add(SetUserName(userName ?? helper.ANON_STRING));
        return bloc..add(LoadItems(groupId ?? helper.NULL_STRING));
      },
      child: MaterialApp(
        title: 'Flutter To-Do List',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: TabsScreen(initialGroupId: groupId),
      ),
    );
  }
}
