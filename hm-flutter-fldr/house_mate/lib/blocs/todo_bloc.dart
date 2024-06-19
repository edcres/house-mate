import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
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

  ToggleItem(this.id);

  @override
  List<Object> get props => [id];
}

class UpdateItem extends TodoEvent {
  final String id;
  final String updatedTask;

  UpdateItem(this.id, this.updatedTask);

  @override
  List<Object> get props => [id, updatedTask];
}

class DeleteItem extends TodoEvent {
  final String id;

  DeleteItem(this.id);

  @override
  List<Object> get props => [id];
}

class EnterEditMode extends TodoEvent {}

class ExitEditMode extends TodoEvent {}

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
    on<EnterEditMode>(_onEnterEditMode);
    on<ExitEditMode>(_onExitEditMode);
  }

  final String groupID = '00000001aaaaa';
  final String collectionPath =
      'todos/Group IDs/00000001aaaaa/Items List/List Items';

  Future<void> _onLoadItems(LoadItems event, Emitter<TodoState> emit) async {
    final snapshot = await _firestore.collection(collectionPath).get();
    final items = snapshot.docs.map((doc) {
      final data = doc.data();
      final itemType =
          data['itemType'] == 'Shopping' ? ItemType.Shopping : ItemType.Chore;
      return itemType == ItemType.Shopping
          ? ShoppingItem(
              id: doc.id, task: data['task'], isCompleted: data['isCompleted'])
          : ChoreItem(
              id: doc.id, task: data['task'], isCompleted: data['isCompleted']);
    }).toList();
    emit(TodoState(items: items));
  }

  Future<void> _onAddItem(AddItem event, Emitter<TodoState> emit) async {
    await _firestore.collection(collectionPath).doc(event.item).set({
      'task': event.item,
      'isCompleted': false,
      'itemType': event.itemType.toString().split('.').last,
    });
    add(LoadItems());
  }

  Future<void> _onToggleItem(ToggleItem event, Emitter<TodoState> emit) async {
    final item = state.items.firstWhere((item) => item.id == event.id);
    await _firestore.collection(collectionPath).doc(event.id).update({
      'isCompleted': !item.isCompleted,
    });
    add(LoadItems());
  }

  Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
    final oldDoc = _firestore.collection(collectionPath).doc(event.id);
    final oldData = (await oldDoc.get()).data();
    if (oldData != null) {
      await _firestore.collection(collectionPath).doc(event.updatedTask).set({
        'task': event.updatedTask,
        'isCompleted': oldData['isCompleted'],
        'itemType': oldData['itemType'],
      });
      await oldDoc.delete();
      add(LoadItems());
    }
  }

  Future<void> _onDeleteItem(DeleteItem event, Emitter<TodoState> emit) async {
    await _firestore.collection(collectionPath).doc(event.id).delete();
    add(LoadItems());
  }

  void _onEnterEditMode(EnterEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: true));
  }

  void _onExitEditMode(ExitEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: false));
  }
}
