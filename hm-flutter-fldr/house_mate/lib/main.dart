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

  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final String? userId = prefs.getString('user_id');
  if (userId == null) {
    final String newUserId = await createUserId();
    await prefs.setString('user_id', newUserId);
  }

  runApp(MyApp());
}

Future<String> createUserId() async {
  final FirebaseFirestore firestore = FirebaseFirestore.instance;
  final String groupId = '00000001aaaaa';
  final DocumentSnapshot<Map<String, dynamic>> clientIdsDoc = await firestore
      .collection('todos')
      .doc('Group IDs')
      .collection(groupId)
      .doc('Client IDs')
      .get();

  String lastClientId =
      clientIdsDoc.data()?['lastClientAdded'] ?? '00000001ffffe';
  String newClientId = generateNextClientId(lastClientId);

  await firestore
      .collection('todos')
      .doc('Group IDs')
      .collection(groupId)
      .doc('Client IDs')
      .set({'lastClientAdded': newClientId}, SetOptions(merge: true));

  return newClientId;
}

String generateNextClientId(String lastClientId) {
  // Extract the first 8 characters as integers
  final int prefix = int.parse(lastClientId.substring(0, 8));
  final int newPrefix = prefix + 1;

  // Generate a random string of 5 letters from the specified set
  const letters = 'asdfglkjh';
  final random = Random();
  final String suffix =
      List.generate(5, (index) => letters[random.nextInt(letters.length)])
          .join();

  final String newClientId = newPrefix.toString().padLeft(8, '0') + suffix;
  return newClientId;
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => TodoBloc()..add(LoadItems()),
      child: MaterialApp(
        title: 'Flutter To-Do List',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: TabsScreen(),
      ),
    );
  }
}
