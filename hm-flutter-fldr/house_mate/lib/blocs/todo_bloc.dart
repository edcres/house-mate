import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/todo.dart';
import '../models/shopping_item.dart';
import '../models/chore_item.dart';

// Event Definitions
abstract class TodoEvent extends Equatable {
  @override
  List<Object> get props => [];
}

class LoadItems extends TodoEvent {}

class AddItem extends TodoEvent {
  final String item;
  final ItemType itemType;

  AddItem(this.item, this.itemType);

  @override
  List<Object> get props => [item, itemType];
}

class ToggleItem extends TodoEvent {
  final String id;
  final ItemType itemType;

  ToggleItem(this.id, this.itemType);

  @override
  List<Object> get props => [id, itemType];
}

class UpdateItem extends TodoEvent {
  final String id;
  final String updatedTask;
  final ItemType itemType;

  UpdateItem(this.id, this.updatedTask, this.itemType);

  @override
  List<Object> get props => [id, updatedTask, itemType];
}

class DeleteItem extends TodoEvent {
  final String id;
  final ItemType itemType;

  DeleteItem(this.id, this.itemType);

  @override
  List<Object> get props => [id, itemType];
}

class EnterEditMode extends TodoEvent {}

class ExitEditMode extends TodoEvent {}

class CreateGroup extends TodoEvent {
  final String groupId;

  CreateGroup(this.groupId);

  @override
  List<Object> get props => [groupId];
}

// State Definition
class TodoState extends Equatable {
  final List<Todo> items;
  final bool isEditMode;

  TodoState({required this.items, this.isEditMode = false});

  TodoState copyWith({List<Todo>? items, bool? isEditMode}) {
    return TodoState(
      items: items ?? this.items,
      isEditMode: isEditMode ?? this.isEditMode,
    );
  }

  @override
  List<Object> get props => [items, isEditMode];
}

// BLoC Definition
class TodoBloc extends Bloc<TodoEvent, TodoState> {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

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

  final String defaultGroupID = '00000001aaaaa';

  Future<String> _getUserId() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('user_id')!;
  }

  String _getCollectionPath(ItemType itemType, String groupId) {
    return 'todos/Group IDs/$groupId/${itemType == ItemType.Shopping ? 'Shopping List' : 'Chores List'}/${itemType == ItemType.Shopping ? 'Shopping Items' : 'Chore Items'}';
  }

  Future<void> _onLoadItems(LoadItems event, Emitter<TodoState> emit) async {
    final shoppingSnapshot = await _firestore
        .collection(_getCollectionPath(ItemType.Shopping, defaultGroupID))
        .get();
    final choreSnapshot = await _firestore
        .collection(_getCollectionPath(ItemType.Chore, defaultGroupID))
        .get();

    final shoppingItems = shoppingSnapshot.docs.map((doc) {
      final data = doc.data();
      return ShoppingItem(
          id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
    }).toList();

    final choreItems = choreSnapshot.docs.map((doc) {
      final data = doc.data();
      return ChoreItem(
          id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
    }).toList();

    final items = [...shoppingItems, ...choreItems];
    emit(TodoState(items: items));
  }

  Future<void> _onAddItem(AddItem event, Emitter<TodoState> emit) async {
    await _firestore
        .collection(_getCollectionPath(event.itemType, defaultGroupID))
        .doc(event.item)
        .set({
      'task': event.item,
      'isCompleted': false,
      'itemType': event.itemType.toString().split('.').last,
    });
    add(LoadItems());
  }

  Future<void> _onToggleItem(ToggleItem event, Emitter<TodoState> emit) async {
    final item = state.items.firstWhere((item) => item.id == event.id);
    await _firestore
        .collection(_getCollectionPath(event.itemType, defaultGroupID))
        .doc(event.id)
        .update({
      'isCompleted': !item.isCompleted,
    });
    add(LoadItems());
  }

  Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
    final oldDoc = _firestore
        .collection(_getCollectionPath(event.itemType, defaultGroupID))
        .doc(event.id);
    final oldData = (await oldDoc.get()).data();
    if (oldData != null) {
      await _firestore
          .collection(_getCollectionPath(event.itemType, defaultGroupID))
          .doc(event.updatedTask)
          .set({
        'task': event.updatedTask,
        'isCompleted': oldData['isCompleted'],
        'itemType': oldData['itemType'],
      });
      await oldDoc.delete();
      add(LoadItems());
    }
  }

  Future<void> _onDeleteItem(DeleteItem event, Emitter<TodoState> emit) async {
    await _firestore
        .collection(_getCollectionPath(event.itemType, defaultGroupID))
        .doc(event.id)
        .delete();
    add(LoadItems());
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
