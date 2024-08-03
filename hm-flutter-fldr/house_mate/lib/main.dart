import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/helper.dart';
import 'package:house_mate/blocs/todo_event.dart';
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
  // Get user/group IDs from local storage.
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final String? groupId = prefs.getString(helper.GROUP_ID_SP);
  // Create userID
  runApp(MyApp(initialGroupId: groupId));
}

class MyApp extends StatelessWidget {
  final String? initialGroupId;

  MyApp({required this.initialGroupId});

  @override
  Widget build(BuildContext context) {
    Helper helper = Helper();
    return BlocProvider(
      create: (context) =>
          TodoBloc()..add(LoadItems(initialGroupId ?? helper.DEFAULT_ID)),
      child: MaterialApp(
        title: 'Flutter To-Do List',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: TabsScreen(initialGroupId: initialGroupId),
      ),
    );
  }
}
