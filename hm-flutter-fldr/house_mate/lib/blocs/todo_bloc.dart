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

  Future<String> _getUserId() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('user_id')!;
  }

  Future<void> _onLoadItems(LoadItems event, Emitter<TodoState> emit) async {
    final groupId = event.groupId;

    // Listen to shopping items updates
    _firestoreApiService.getShoppingItems(groupId).listen((shoppingItems) {
      // Listen to chore items updates
      _firestoreApiService.getChoreItems(groupId).listen((choreItems) {
        final items = [...shoppingItems, ...choreItems];
        emit(TodoState(items: items));
      });
    });
  }

  Future<void> _onAddItem(AddItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    _firestoreApiService.addItem(groupId, event.itemType, event.item);
    add(LoadItems(groupId));
  }

  Future<void> _onToggleItem(ToggleItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    final item = state.items.firstWhere((item) => item.id == event.id);
    _firestoreApiService.toggleItem(groupId, event.itemType, item, event.id);
    add(LoadItems(groupId));
  }

  Future<void> _onUpdateItem(UpdateItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    _firestoreApiService.updateItem(
        groupId, event.itemType, event.id, event.updatedTask);
    add(LoadItems(groupId));
  }

  Future<void> _onDeleteItem(DeleteItem event, Emitter<TodoState> emit) async {
    final groupId = await _getUserId();
    _firestoreApiService.deleteItem(groupId, event.itemType, event.id);
    add(LoadItems(groupId));
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
