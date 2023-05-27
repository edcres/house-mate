import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';

import '../data/firebase_options.dart';

const String DEFAULT_CLIENT_ID = "00000000asdfg";

class ListsState extends ChangeNotifier {
  ListsState() {
    init();
  }

  final String DEFAULT_CLIENT_ID = "00000000asdfg";
  final String GENERAL_COLLECTION = "General Collection";
  final String GROUP_IDS_DOC = "Group IDs";
  final String CLIENT_IDS_DOC = "Client IDs";
  final String SHOPPING_LIST_DOC = "Shopping List";
  final String SHOPPING_ITEMS_COLLECTION = "Shopping Items";
  final String CHORES_LIST_DOC = "Chores List";
  final String CHORE_ITEMS_COLLECTION = "Chore Items";

  final String LAST_GROUP_ADDED_FIELD = "lastGroupAdded";
  final String LAST_CLIENT_ADDED_FIELD = "lastClientAdded";
  final String NAME_FIELD = "name";
  final String QUANTITY_FIELD = "quantity";
  final String ADDED_BY_FIELD = "addedBy";
  final String COMPLETED_FIELD = "completed";
  final String COST_FIELD = "cost";
  final String PURCHASE_LOCATION_FIELD = "purchaseLocation";
  final String NEEDED_BY_FIELD = "neededBy";
  final String VOLUNTEER_FIELD = "volunteer";
  final String PRIORITY_FIELD = "priority";
  final String DIFFICULTY_FIELD = "difficulty";

  Future<void> init() async {
    await Firebase.initializeApp(
        options: DefaultFirebaseOptions.currentPlatform);

    final docRef = FirebaseFirestore.instance
        .collection(GENERAL_COLLECTION)
        .doc(GROUP_IDS_DOC);

    // TODO: this is just to test if I can get firestore data
    // print("test print 1");
    docRef.get().then(
      (DocumentSnapshot doc) {
        // print("test print 2");
        final data = doc.data() as Map<String, dynamic>;

        // semd this to the UI
        final dataTodisplay = data[LAST_GROUP_ADDED_FIELD];
        // TODO: use this varibale
        print(dataTodisplay);
      },
      onError: (e) => print("Error getting document: $e"),
    );
  }
}
