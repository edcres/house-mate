import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/helper.dart';
import 'package:house_mate/firebase_options.dart';
import 'package:house_mate/screens/tabs_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'blocs/todo_bloc.dart';

// TODO: Start the app in a cleaner way. Maybe have a function here to 'startApp()' and send the sharedPref to the bloc

void main() async {
  Helper helper = Helper();
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final String? groupId = prefs.getString(helper.GROUP_ID_SP);
  runApp(MyApp(groupId: groupId));
}

class MyApp extends StatelessWidget {
  final String? groupId;

  MyApp({required this.groupId});

  @override
  Widget build(BuildContext context) {
    print(
        "----------------------------  happens 1 group=$groupId  --------------------------------------");
    Helper helper = Helper();
    return BlocProvider(
      create: (context) {
        print(
            "----------------------------  happens 1.5 group=$groupId  --------------------------------------");
        final bloc = TodoBloc();
        print(
            "----------------------------  happens 1.75 group=$groupId  --------------------------------------");
        bloc.add(SetGroupId(groupId ?? helper.NULL_STRING));
        print(
            "----------------------------  happens 2 group=$groupId  --------------------------------------");
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
