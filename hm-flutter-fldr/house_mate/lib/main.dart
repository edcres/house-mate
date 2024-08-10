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
  runApp(MyApp(groupId: groupId));
}

class MyApp extends StatelessWidget {
  final String? groupId;

  MyApp({required this.groupId});

  @override
  Widget build(BuildContext context) {
    Helper helper = Helper();
    print(
        "--------------------------------  call 1 grp=$groupId --------------------------");
    return BlocProvider(
      create: (context) {
        final bloc = TodoBloc();
        bloc.add(SetGroupId(groupId!));
        return bloc..add(LoadItems(groupId ?? helper.DEFAULT_ID));
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
