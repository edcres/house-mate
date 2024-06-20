import 'dart:math';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/firebase_options.dart';
import 'package:house_mate/screens/tabs_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'blocs/todo_bloc.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  // Get user/group IDs from local storage.
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final String? groupId = prefs.getString('group_id');

  // Create userID
  runApp(MyApp(initialGroupId: groupId));
}

class MyApp extends StatelessWidget {
  final String? initialGroupId;

  MyApp({required this.initialGroupId});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) =>
          TodoBloc()..add(LoadItems(initialGroupId ?? '00000001aaaaa')),
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

Future<bool> checkGroupIdExists(String groupId) async {
  final FirebaseFirestore firestore = FirebaseFirestore.instance;
  final DocumentSnapshot<Map<String, dynamic>> groupDoc = await firestore
      .collection('todos')
      .doc('Group IDs')
      .collection(groupId)
      .doc('Client IDs')
      .get();

  return groupDoc.exists;
}
