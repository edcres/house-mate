import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:house_mate/Helper.dart';

class FirestoreApiService {
  // User ID
  Helper helper = Helper();

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
    String newClientId = helper.generateNewID(lastClientId);

    await firestore
        .collection('todos')
        .doc('Group IDs')
        .collection(groupId)
        .doc('Client IDs')
        .set({'lastClientAdded': newClientId}, SetOptions(merge: true));

    return newClientId;
  }

  // Group ID
  Future<String> createGroupId() async {
    final FirebaseFirestore firestore = FirebaseFirestore.instance;
    final QuerySnapshot<Map<String, dynamic>> groupsSnapshot = await firestore
        .collection('todos')
        .doc('Group IDs')
        .collection('groups')
        .orderBy('id', descending: true)
        .limit(1)
        .get();

    String lastGroupId = groupsSnapshot.docs.isNotEmpty
        ? groupsSnapshot.docs.first.id
        : '00000000aaaaa';
    String newGroupId = helper.generateNewID(lastGroupId);

    // Create lastClientAdded, ShoppingList, ChoresList
    await firestore
        .collection('todos')
        .doc('Group IDs')
        .collection(newGroupId)
        .doc('Client IDs')
        .set({'lastClientAdded': '00000001fffff'}, SetOptions(merge: true));
    await firestore
        .collection('todos')
        .doc('Group IDs')
        .collection(newGroupId)
        .doc('Shopping List')
        .set({});
    await firestore
        .collection('todos')
        .doc('Group IDs')
        .collection(newGroupId)
        .doc('Chores List')
        .set({});

    return newGroupId;
  }
}
