import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:house_mate/data/models/chore_item.dart';
import 'package:house_mate/data/models/todo_item.dart';
import 'package:house_mate/helper.dart';
import 'package:house_mate/data/models/shopping_item.dart';

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
  late DocumentReference groupIDsDoc;
  Helper helper = Helper();

  FirestoreApiService() {
    groupIDsDoc = firestore.collection(GENERAL_COLLECTION).doc(GROUP_IDS_DOC);
  }

  // This could go to the helper class
  String _getCollectionPath(String groupId, ItemType itemType) {
    return '$groupId/${itemType == ItemType.Shopping ? SHOPPING_LIST_DOC : CHORES_LIST_DOC}/${itemType == ItemType.Shopping ? SHOPPING_ITEMS_COLLECTION : CHORE_ITEMS_COLLECTION}';
  }

  // TODO: Implement the stream in the rest of the code
  //      - I have another bug that i think will be fixed after handling the streams. When a value in a field is updated, it's not updated in the app. If this doesn't work maybe clear the cache when the app opens up.
  Stream<List<ShoppingItem>> getShoppingItems(String groupId) {
    return groupIDsDoc
        .collection(_getCollectionPath(groupId, ItemType.Shopping))
        .snapshots()
        .map((snapshot) {
      return snapshot.docs.map((doc) {
        final data = doc.data();
        return ShoppingItem.fromJson({
          'id': doc.id,
          ...data,
        });
      }).toList();
    });
  }

  Stream<List<ChoreItem>> getChoreItems(String groupId) {
    return groupIDsDoc
        .collection(_getCollectionPath(groupId, ItemType.Chore))
        .snapshots()
        .map((snapshot) {
      return snapshot.docs.map((doc) {
        final data = doc.data();
        return ChoreItem.fromJson({
          'id': doc.id,
          ...data,
        });
      }).toList();
    });
  }

  // Get Chore Items
  Future<void> addItem(String groupId, ItemType itemType, TodoItem item) async {
    await groupIDsDoc
        .collection(_getCollectionPath(groupId, itemType))
        .doc(item.name)
        .set(item.toJson());
  }

  // Toggle Item
  Future<void> toggleItem(
      String groupId, ItemType itemType, TodoItem item, String eventId) async {
    await groupIDsDoc
        .collection(_getCollectionPath(groupId, itemType))
        .doc(eventId)
        .update({
      'isCompleted': !item.completed,
    });
  }

  // Update item
  // Future<void> updateItem(String groupId, ItemType itemType, String eventId,
  //     String updatedTask) async {
  //   final oldDoc = groupIDsDoc
  //       .collection(_getCollectionPath(groupId, itemType))
  //       .doc(eventId);
  //   final oldData = (await oldDoc.get()).data();
  //   if (oldData != null) {
  //     await groupIDsDoc
  //         .collection(_getCollectionPath(groupId, itemType))
  //         .doc(updatedTask)
  //         .set({
  //       'task': updatedTask,
  //       'isCompleted': oldData['isCompleted'],
  //       'itemType': oldData['itemType'],
  //     });
  //     // TODO: Why is it deleting the old doc???
  //     await oldDoc.delete();
  //   }
  // }

  Future<void> updateItem(
      String groupId, ItemType itemType, TodoItem updatedItem) async {
    print("_____________     update 4");
    final docRef = groupIDsDoc
        .collection(_getCollectionPath(groupId, itemType))
        .doc(updatedItem.name);
    // Use the `toJson` method to get only the relevant fields
    final updatedData = updatedItem.toJson();
    // Update the document in Firestore without deleting the old one
    await docRef.set(updatedData, SetOptions(merge: true));
    print("_____________     update 5");
  }

  // Delete item
  Future<void> deleteItem(
      String groupId, ItemType itemType, String eventId) async {
    await groupIDsDoc
        .collection(_getCollectionPath(groupId, itemType))
        .doc(eventId)
        .delete();
  }

  // User ID
  // TODO: This doesn't work in the native android app so I won't finish this here yet.
  //    - Fix this. Now it just creates a random ID based on nothing.
  Future<String> createUserId(String groupId) async {
    String newClientId = helper.generateNewID(helper.DEFAULT_ID);
    await groupIDsDoc
        .collection(groupId)
        .doc(CLIENT_IDS_DOC)
        .set({LAST_CLIENT_ADDED_FIELD: newClientId}, SetOptions(merge: true));
    return newClientId;
  }

  // Group ID
  Future<String> createGroup() async {
    final DocumentSnapshot docSnap = await groupIDsDoc.get();
    String newGroupId = "";
    if (docSnap.exists) {
      // Get the value of the field 'last group added'
      final data = docSnap.data() as Map<String, dynamic>;
      String lastGroupId = data[LAST_GROUP_ADDED_FIELD] ?? helper.DEFAULT_ID;
      newGroupId = helper.generateNewID(lastGroupId);
      groupIDsDoc.update({LAST_GROUP_ADDED_FIELD: newGroupId});
    } else {
      newGroupId = helper.generateNewID(helper.DEFAULT_ID);
      await groupIDsDoc.set({
        LAST_GROUP_ADDED_FIELD: newGroupId,
      }, SetOptions(merge: true));
    }
    return newGroupId;
  }

  Future<bool> checkGroupIdExists(String groupId) async {
    final DocumentSnapshot<Map<String, dynamic>> groupDoc =
        await groupIDsDoc.collection(groupId).doc(CLIENT_IDS_DOC).get();
    return groupDoc.exists;
  }
}
