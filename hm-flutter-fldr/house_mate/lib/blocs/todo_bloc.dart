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

// State Definition
class TodoState extends Equatable {
  final List<Todo> items;

  TodoState({required this.items});

  @override
  List<Object> get props => [items];
}

// BLoC Definition
class TodoBloc extends Bloc<TodoEvent, TodoState> {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  TodoBloc() : super(TodoState(items: [])) {
    on<LoadItems>(_onLoadItems);
    on<AddItem>(_onAddItem);
    on<ToggleItem>(_onToggleItem);
    on<UpdateItem>(_onUpdateItem);
  }

  Future<void> _onLoadItems(LoadItems event, Emitter<TodoState> emit) async {
    final snapshot = await _firestore.collection('todos').get();
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
    final newDoc = await _firestore.collection('todos').add({
      'task': event.item,
      'isCompleted': false,
      'itemType': event.itemType.toString().split('.').last,
    });
    add(LoadItems());
  }

  Future<void> _onToggleItem(ToggleItem event, Emitter<TodoState> emit) async {
    final item = state.items.firstWhere((item) => item.id == event.id);
    await _firestore.collection('todos').doc(event.id).update({
      'isCompleted': !item.isCompleted,
    });
    add(LoadItems());
  }

  Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
    await _firestore.collection('todos').doc(event.id).update({
      'task': event.updatedTask,
    });
    add(LoadItems());
  }
}
