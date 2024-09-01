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
  late DocumentReference groupIDsDoc;
  Helper helper = Helper();

  FirestoreApiService() {
    groupIDsDoc = firestore.collection(GENERAL_COLLECTION).doc(GROUP_IDS_DOC);
    clearFirestoreCache();
  }

  // This could go to the helper class
  String _getCollectionPath(String groupId, ItemType itemType) {
    return '$groupId/${itemType == ItemType.Shopping ? SHOPPING_LIST_DOC : CHORES_LIST_DOC}/${itemType == ItemType.Shopping ? SHOPPING_ITEMS_COLLECTION : CHORE_ITEMS_COLLECTION}';
  }

  Future<void> clearFirestoreCache() async {
    // TODO: Remove caching when streams are handled
    // Disable persistence to clear cache
    await FirebaseFirestore.instance.clearPersistence();
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
  Future<void> toggleItem(String groupId, ItemType itemType, bool completed,
      String itemName) async {
    final docRef = groupIDsDoc
        .collection(_getCollectionPath(groupId, itemType))
        .doc(itemName);
    await docRef.update({
      TodoItem.fieldCompleted: completed,
    });
  }

  // Update item
  Future<void> updateItem(
      String groupId, ItemType itemType, TodoItem updatedItem) async {
    final docRef = groupIDsDoc
        .collection(_getCollectionPath(groupId, itemType))
        .doc(updatedItem.name);
    final updatedData = updatedItem.toJson();
    await docRef.update(updatedData);
  }

  // Delete item
  Future<void> deleteItem(
      String groupId, ItemType itemType, String itemName) async {
    await groupIDsDoc
        .collection(_getCollectionPath(groupId, itemType))
        .doc(itemName)
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
      // Get the value of the last group added field
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
