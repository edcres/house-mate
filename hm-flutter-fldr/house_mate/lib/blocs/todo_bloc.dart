import 'dart:async';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/Helper.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import 'package:house_mate/data/firestore_api_service.dart';
import 'package:house_mate/data/models/chore_item.dart';
import 'package:house_mate/data/models/shopping_item.dart';

class TodoBloc extends Bloc<TodoEvent, TodoState> {
  final FirestoreApiService _firestoreApiService;
  final Helper helper = Helper();

  TodoBloc()
      : _firestoreApiService = FirestoreApiService(),
        super(TodoState(items: [])) {
    on<LoadItems>(_onLoadItems);
    on<JoinGroup>(_onJoinGroup);
    on<AddItem>(_onAddItem);
    on<ToggleItem>(_onToggleItem);
    on<UpdateItem>(_onUpdateItem);
    on<DeleteItem>(_onDeleteItem);
    on<CheckGroupIdExistsAndJoin>(_onCheckGroupIdExistsAndJoin);
    on<CreateGroup>(_onCreateGroup);
    on<EnterEditMode>(_onEnterEditMode);
    on<ExitEditMode>(_onExitEditMode);
    on<SetGroupId>(_onSetGroupId);
    on<SetUserName>(_onSetUserName);
  }

  // Possible addition for good practice
  // Stream<TodoState> mapEventToState(TodoEvent event) async* {
  //   if (event is LoadItems) {
  //     yield state.copyWith(items: [], groupId: event.groupId);
  //   }
  // }

  Future<void> _onLoadItems(LoadItems event, Emitter<TodoState> emit) async {
    final groupId = event.groupId;
    if (groupId == helper.NULL_STRING) return;
    try {
      // Wait for the first emission from both streams
      final shoppingItemsFuture =
          _firestoreApiService.getShoppingItems(groupId).first;
      final choreItemsFuture =
          _firestoreApiService.getChoreItems(groupId).first;
      final results =
          await Future.wait([shoppingItemsFuture, choreItemsFuture]);
      // Combine the results
      final shoppingItems = results[0] as List<ShoppingItem>;
      final choreItems = results[1] as List<ChoreItem>;
      final items = [...shoppingItems, ...choreItems];
      emit(state.copyWith(items: items));
    } catch (error) {
      print("Error loading items: $error");
    }
  }

  Future<void> _onCheckGroupIdExistsAndJoin(
      CheckGroupIdExistsAndJoin event, Emitter<TodoState> emit) async {
    final exists = await _firestoreApiService.checkGroupIdExists(event.groupId);
    emit(state.copyWith(groupIdExists: exists, groupId: event.groupId));
    add(LoadItems(event.groupId));
  }

  Future<void> _onJoinGroup(JoinGroup event, Emitter<TodoState> emit) async {
    final String newUserId =
        await _firestoreApiService.createUserId(event.groupId);
    emit(state.copyWith(userId: newUserId));
  }

  Future<void> _onAddItem(AddItem event, Emitter<TodoState> emit) async {
    final groupId = state.groupId;
    event.item.addedBy = state.userName ?? helper.ANON_STRING;
    // TODO: When fixing user id bugs, change this
    if (groupId != null) {
      _firestoreApiService.addItem(groupId, event.itemType, event.item);
      add(LoadItems(groupId));
    } else {
      print("GroupId is null1");
    }
  }

  Future<void> _onToggleItem(ToggleItem event, Emitter<TodoState> emit) async {
    final groupId = state.groupId;
    if (groupId != null) {
      final item = state.items.firstWhere((item) => item.id == event.id);
      _firestoreApiService.toggleItem(groupId, event.itemType, item, event.id);
      add(LoadItems(groupId));
    } else {
      print("GroupId is null2");
    }
  }

  // Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
  //   print("_____________________           called");
  //   final groupId = state.groupId;
  //   if (groupId != null) {
  //     _firestoreApiService.updateItem(
  //         groupId, event.itemType, event.id, event.updatedTask);
  //     add(LoadItems(groupId));
  //   } else {
  //     print("GroupId is null3");
  //   }
  // }

  Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
    final groupId = state.groupId;
    print("_____________     update 2");
    if (groupId != null) {
      final updatedItem = event.updatedItem;
      // final updatedItem = state.items.firstWhere((item) => item.id == event.id);
      _firestoreApiService.updateItem(groupId, event.itemType, updatedItem);
      print("_____________     update 3   ${event.updatedItem}");
      add(LoadItems(groupId));
    } else {
      print("GroupId is null");
    }
  }

  Future<void> _onDeleteItem(DeleteItem event, Emitter<TodoState> emit) async {
    final groupId = state.groupId;
    if (groupId != null) {
      _firestoreApiService.deleteItem(groupId, event.itemType, event.itemName);
      add(LoadItems(groupId));
    } else {
      print("GroupId is null4");
    }
  }

  Future<void> _onCreateGroup(
      CreateGroup event, Emitter<TodoState> emit) async {
    String newGroupId = await _firestoreApiService.createGroup();
    String newUserId = await _firestoreApiService.createUserId(newGroupId);
    emit(state.copyWith(groupId: newGroupId, userId: newUserId));
    add(LoadItems(newGroupId));
    helper.saveGroupToSp(newGroupId);
  }

  void _onSetGroupId(SetGroupId event, Emitter<TodoState> emit) {
    String? groupId = event.groupId;
    bool exists = true;
    if (groupId == helper.NULL_STRING) {
      groupId = null;
      exists = false;
    }
    emit(state.copyWith(groupId: groupId, groupIdExists: exists));
  }

  void _onSetUserName(SetUserName event, Emitter<TodoState> emit) {
    emit(state.copyWith(userName: event.userName));
  }

  void _onEnterEditMode(EnterEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: true));
  }

  void _onExitEditMode(ExitEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: false));
  }
}
