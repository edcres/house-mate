import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/Helper.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import 'package:house_mate/data/firestore_api_service.dart';
import 'package:shared_preferences/shared_preferences.dart';

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
    on<CheckGroupIdExists>(_onCheckGroupIdExists);
    on<CreateGroup>(_onCreateGroup);
    on<EnterEditMode>(_onEnterEditMode);
    on<ExitEditMode>(_onExitEditMode);
  }

  // TODO: everything that calls this, shouldn't
  // Future<String> _getUserId() async {
  //   final SharedPreferences prefs = await SharedPreferences.getInstance();
  //   return prefs.getString('user_id')!;
  // }

  Future<void> _onLoadItems(LoadItems event, Emitter<TodoState> emit) async {
    print(
        "------------------------------l-  add call 9999 groupid=${state.groupId} --------------------------");
    final groupId = event.groupId;

    print(
        "--------------------------------  call 4 --------------------------");
    // Listen to shopping items updates
    _firestoreApiService.getShoppingItems(groupId).listen((shoppingItems) {
      // Listen to chore items updates
      print(
          "--------------------------------  call 5 --------------------------");
      _firestoreApiService.getChoreItems(groupId).listen((choreItems) {
        final items = [...shoppingItems, ...choreItems];
        print(
            "--------------------------------  call 5.5 --------------------------");
        emit(TodoState(items: items)); // TODO: error happens here
        print(
            "--------------------------------  call 5.75 --------------------------");
      });
      print(
          "--------------------------------  call 6 --------------------------");
    });
  }

  Future<void> _onAddItem(AddItem event, Emitter<TodoState> emit) async {
    print(
        "------------------------------l-  add call 3 --------------------------");
    final groupId = state.groupId;
    print(
        "------------------------------l-  add call 3.5 groupid=${groupId} --------------------------");
    if (groupId == null) {
      _firestoreApiService.addItem(groupId!, event.itemType, event.item);
      add(LoadItems(groupId));
    } else {
      print("GroupId is null");
    }
    print(
        "------------------------------l-  add call 4 user=${groupId} --------------------------");
    print(
        "------------------------------l-  add call5 --------------------------");
  }

  Future<void> _onToggleItem(ToggleItem event, Emitter<TodoState> emit) async {
    final groupId = state.groupId;
    if (groupId == null) {
      final item = state.items.firstWhere((item) => item.id == event.id);
      _firestoreApiService.toggleItem(groupId!, event.itemType, item, event.id);
      add(LoadItems(groupId));
    } else {
      print("GroupId is null");
    }
  }

  Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
    final groupId = state.groupId;
    if (groupId == null) {
      _firestoreApiService.updateItem(
          groupId!, event.itemType, event.id, event.updatedTask);
      add(LoadItems(groupId));
    } else {
      print("GroupId is null");
    }
  }

  Future<void> _onDeleteItem(DeleteItem event, Emitter<TodoState> emit) async {
    final groupId = state.groupId;
    if (groupId == null) {
      _firestoreApiService.deleteItem(groupId!, event.itemType, event.id);
      add(LoadItems(groupId));
    } else {
      print("GroupId is null");
    }
  }

  Future<void> _onCheckGroupIdExists(
      CheckGroupIdExists event, Emitter<TodoState> emit) async {
    final exists = await _firestoreApiService.checkGroupIdExists(event.groupId);
    emit(state.copyWith(groupIdExists: exists, groupId: event.groupId));
  }

  Future<void> _onJoinGroup(JoinGroup event, Emitter<TodoState> emit) async {
    // TODO: When fixing user id bugs, change this
    final String newUserId =
        await _firestoreApiService.createUserId(event.groupId);
    emit(state.copyWith(userId: newUserId));
  }

  Future<void> _onCreateGroup(
      CreateGroup event, Emitter<TodoState> emit) async {
    String newGroupId = await _firestoreApiService.createGroup();
    String newUserId = await _firestoreApiService.createUserId(newGroupId);
    emit(state.copyWith(
      groupId: newGroupId,
      userId: newUserId,
    ));
  }

  void _onEnterEditMode(EnterEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: true));
  }

  void _onExitEditMode(ExitEditMode event, Emitter<TodoState> emit) {
    emit(state.copyWith(isEditMode: false));
  }
}
