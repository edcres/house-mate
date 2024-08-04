import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../data/models/todo_item.dart';
import '../data/models/shopping_item.dart';
import '../data/models/chore_item.dart';

class TodoBloc extends Bloc<TodoEvent, TodoState> {
  TodoBloc() : super(TodoState(items: [])) {
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

    // TODO: call addItem from apiService
    // await _firestore
    //     .collection(_getCollectionPath(event.itemType, groupId))
    //     .doc(event.item)
    //     .set({
    //   'task': event.item,
    //   'isCompleted': false,
    //   'itemType': event.itemType.toString().split('.').last,
    // });
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
      add(LoadItems(groupId));
    }
  }

  Future<void> _onDeleteItem(DeleteItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    await _firestore
        .collection(_getCollectionPath(event.itemType, groupId))
        .doc(event.id)
        .delete();
    add(LoadItems(groupId));
  }

  Future<void> _onCreateGroup(
      CreateGroup event, Emitter<TodoState> emit) async {
    final groupDocPath = 'todos/Group IDs/${event.groupId}';
    final clientIDsDocPath = '$groupDocPath/Client IDs';

    await _firestore.doc(clientIDsDocPath).set({
      'lastClientAdded': '00000001fffff',
    });
  }

  void _onEnterEditMode(EnterEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: true));
  }

  void _onExitEditMode(ExitEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: false));
  }
}
