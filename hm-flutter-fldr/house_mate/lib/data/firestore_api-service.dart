import 'package:cloud_firestore/cloud_firestore.dart';

class FirestoreApiService {
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
}
