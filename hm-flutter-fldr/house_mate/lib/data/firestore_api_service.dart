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

  // Get Shopping Items
  Stream<List<ShoppingItem>> getShoppingItems(String groupId) {
    print(
        "--------------------------------  call 7 --------------------------");
    print(
        "--------------------------------  call 7.5 ${_getCollectionPath(groupId, ItemType.Shopping)} --------------------------");
    return groupIDsDoc
        .collection(_getCollectionPath(groupId, ItemType.Shopping))
        .snapshots()
        .map((snapshot) {
      print(
          "--------------------------------  call 8 --------------------------");
      return snapshot.docs.map((doc) {
        print(
            "--------------------------------  call 9 --------------------------");
        final data = doc.data();
        return ShoppingItem(
            id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
      }).toList();
    });
  }

  // Get Shopping Items
  // Future<void> getShoppingItems(String groupId) async {
  //   final shoppingSnapshot = await firestore
  //       .collection(_getCollectionPath(groupId, ItemType.Shopping))
  //       .get();
  //   final shoppingItems = shoppingSnapshot.docs.map((doc) {
  //     final data = doc.data();
  //     return ShoppingItem(
  //         id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
  //   }).toList();
  // }

  // Get Chore Items
  Stream<List<ChoreItem>> getChoreItems(String groupId) {
    print(
        "--------------------------------  call 11 --------------------------");
    return groupIDsDoc
        .collection(_getCollectionPath(groupId, ItemType.Chore))
        .snapshots()
        .map((snapshot) {
      print(
          "--------------------------------  call 12 --------------------------");
      return snapshot.docs.map((doc) {
        print(
            "--------------------------------  call 13 --------------------------");
        final data = doc.data();
        return ChoreItem(
            id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
      }).toList();
    });
  }

  // Add Item
  Future<void> addItem(String groupId, ItemType itemType, String item) async {
    await firestore
        .collection(_getCollectionPath(groupId, itemType))
        .doc(item)
        .set({
      'task': item,
      'isCompleted': false,
      'itemType': itemType.toString().split('.').last,
    });
  }

  // Toggle Item
  Future<void> toggleItem(
      String groupId, ItemType itemType, TodoItem item, String eventId) async {
    await firestore
        .collection(_getCollectionPath(groupId, itemType))
        .doc(eventId)
        .update({
      'isCompleted': !item.isCompleted,
    });
  }

  // Update item
  Future<void> updateItem(String groupId, ItemType itemType, String eventId,
      String updatedTask) async {
    final oldDoc = firestore
        .collection(_getCollectionPath(groupId, itemType))
        .doc(eventId);
    final oldData = (await oldDoc.get()).data();
    if (oldData != null) {
      await firestore
          .collection(_getCollectionPath(groupId, itemType))
          .doc(updatedTask)
          .set({
        'task': updatedTask,
        'isCompleted': oldData['isCompleted'],
        'itemType': oldData['itemType'],
      });
      // TODO: Why is it deleting the old doc???
      await oldDoc.delete();
    }
  }

  // Delete item
  Future<void> deleteItem(
      String groupId, ItemType itemType, String eventId) async {
    await firestore
        .collection(_getCollectionPath(groupId, itemType))
        .doc(eventId)
        .delete();
  }

  // User ID
  // TODO: This doesn't work in the native android app so I wont finish this here yet.
  Future<String> createUserId(String groupId) async {
    // Need to fix this. Now it just creates a random ID based on nothing. USe the commented out code below as reference
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
      String? lastGroupId = data[LAST_GROUP_ADDED_FIELD];
      if (lastGroupId != null) {
        newGroupId = helper.generateNewID(lastGroupId);
        groupIDsDoc.update({LAST_GROUP_ADDED_FIELD: newGroupId});
      } else {
        print("Last group ID is null");
      }
    } else {
      newGroupId = helper.generateNewID(helper.DEFAULT_ID);
      await groupIDsDoc.set({
        LAST_GROUP_ADDED_FIELD: newGroupId,
      }, SetOptions(merge: true));
      print("docSnap doesn't exist");
    }
    return newGroupId;
  }

  Future<bool> checkGroupIdExists(String groupId) async {
    final DocumentSnapshot<Map<String, dynamic>> groupDoc =
        await groupIDsDoc.collection(groupId).doc(CLIENT_IDS_DOC).get();
    return groupDoc.exists;
  }
}
