import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import 'package:house_mate/data/firestore_api_service.dart';
import 'package:shared_preferences/shared_preferences.dart';

class TodoBloc extends Bloc<TodoEvent, TodoState> {
  final FirestoreApiService _firestoreApiService;

  TodoBloc(this._firestoreApiService) : super(TodoState(items: [])) {
    on<LoadItems>(_onLoadItems);
    on<AddItem>(_onAddItem);
    on<ToggleItem>(_onToggleItem);
    on<UpdateItem>(_onUpdateItem);
    on<DeleteItem>(_onDeleteItem);
    on<CreateGroup>(_onCreateGroup);
    on<EnterEditMode>(_onEnterEditMode);
    on<ExitEditMode>(_onExitEditMode);
  }

  Future<String> _getUserId() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('user_id')!;
  }

  Future<void> _onLoadItems(LoadItems event, Emitter<TodoState> emit) async {
    final groupId = event.groupId;

    // TODO: call Shopping items from apiService
    // TODO: call Chore items from apiService
    // final shoppingSnapshot = await _firestore
    //     .collection(_getCollectionPath(ItemType.Shopping, groupId))
    //     .get();
    // final choreSnapshot = await _firestore
    //     .collection(_getCollectionPath(ItemType.Chore, groupId))
    //     .get();

    // final shoppingItems = shoppingSnapshot.docs.map((doc) {
    //   final data = doc.data();
    //   return ShoppingItem(
    //       id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
    // }).toList();

    // final choreItems = choreSnapshot.docs.map((doc) {
    //   final data = doc.data();
    //   return ChoreItem(
    //       id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
    // }).toList();

    // TODO: Get the shopping items and chore items in realtime from the database
    final items = [...shoppingItems, ...choreItems];
    emit(TodoState(items: items));
  }

  Future<void> _onAddItem(AddItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();

    // TODO: test then remove this comment
    // await _firestore
    //     .collection(_getCollectionPath(event.itemType, groupId))
    //     .doc(event.item)
    //     .set({
    //   'task': event.item,
    //   'isCompleted': false,
    //   'itemType': event.itemType.toString().split('.').last,
    // });
    _firestoreApiService.addItem(groupId, event.itemType, event.item);
    add(LoadItems(groupId));
  }

  Future<void> _onToggleItem(ToggleItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    final item = state.items.firstWhere((item) => item.id == event.id);
    // TODO: call the funciton from apiService
    // await _firestore
    //     .collection(_getCollectionPath(event.itemType, groupId))
    //     .doc(event.id)
    //     .update({
    //   'isCompleted': !item.isCompleted,
    // });
    _firestoreApiService.toggleItem(groupId, event.itemType, item, event.id);
    add(LoadItems(groupId));
  }

  Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    // TODO: call the funciton from apiService
    // final oldDoc = _firestore
    //     .collection(_getCollectionPath(event.itemType, groupId))
    //     .doc(event.id);
    // final oldData = (await oldDoc.get()).data();
    // if (oldData != null) {
    //   await _firestore
    //       .collection(_getCollectionPath(event.itemType, groupId))
    //       .doc(event.updatedTask)
    //       .set({
    //     'task': event.updatedTask,
    //     'isCompleted': oldData['isCompleted'],
    //     'itemType': oldData['itemType'],
    //   });
    //   await oldDoc.delete();
    _firestoreApiService.updateItem(
        groupId, event.itemType, event.id, event.updatedTask);
    add(LoadItems(groupId));
  }

  Future<void> _onDeleteItem(DeleteItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    // await _firestore
    //     .collection(_getCollectionPath(event.itemType, groupId))
    //     .doc(event.id)
    //     .delete();
    _firestoreApiService.deleteItem(groupId, event.itemType, event.id);
    add(LoadItems(groupId));
  }

// Future<void> _onCreateGroup() async {
  // TODO: create ID function
  Future<void> _onCreateGroup(
      CreateGroup event, Emitter<TodoState> emit) async {
    // TODO: Maybe do something with this group ID (like save it oin shared preferences ass current group and in the list of groups)
    String newGroupID = await _firestoreApiService.createGroup();
  }

  void _onEnterEditMode(EnterEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: true));
  }

  void _onExitEditMode(ExitEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: false));
  }
}
