import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:house_mate/Helper.dart';

class FirestoreApiService {
  final FirebaseFirestore firestore = FirebaseFirestore.instance;
  final String GENERAL_COLLECTION = "todos";
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
  final String NOTES_FIELD = "notes";
  DocumentReference groupIDsDoc =
      DocumentReference as DocumentReference<Object?>;
  Helper helper = Helper();

  FirestoreApiService() {
    groupIDsDoc = firestore.collection(GENERAL_COLLECTION).doc(GROUP_IDS_DOC);
  }

  // User ID
  Future<String> createUserId(String groupId) async {
    final DocumentSnapshot<Map<String, dynamic>> clientIdsDoc =
        await groupIDsDoc.collection(groupId).doc(CLIENT_IDS_DOC).get();

    String lastClientId =
        clientIdsDoc.data()?[LAST_CLIENT_ADDED_FIELD] ?? helper.DEFAULT_ID;
    String newClientId = helper.generateNewID(lastClientId);

    await groupIDsDoc
        .collection(groupId)
        .doc(CLIENT_IDS_DOC)
        .set({LAST_CLIENT_ADDED_FIELD: newClientId}, SetOptions(merge: true));

    return newClientId;
  }

  // Group ID
  Future<String> createGroup() async {
    final QuerySnapshot<Map<String, dynamic>> groupsSnapshot = await groupIDsDoc
        .collection(helper.generateNewID(helper.DEFAULT_ID))
        // TODO: what is 'id'  ?
        .orderBy('id', descending: true)
        .limit(1)
        .get();

    String lastGroupId = groupsSnapshot.docs.isNotEmpty
        ? groupsSnapshot.docs.first.id
        : helper.DEFAULT_ID;
    String newGroupId = helper.generateNewID(lastGroupId);

    // Create lastClientAdded, ShoppingList, ChoresList
    await groupIDsDoc.collection(newGroupId).doc(CLIENT_IDS_DOC).set(
        {LAST_CLIENT_ADDED_FIELD: helper.DEFAULT_ID}, SetOptions(merge: true));

    return newGroupId;
  }

  // Check if group exists.
  Future<bool> checkGroupIdExists(String groupId) async {
    final DocumentSnapshot<Map<String, dynamic>> groupDoc =
        await groupIDsDoc.collection(groupId).doc(CLIENT_IDS_DOC).get();
    return groupDoc.exists;
  }
}
