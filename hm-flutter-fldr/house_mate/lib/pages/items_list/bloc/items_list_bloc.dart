import 'package:equatable/equatable.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
// TODO: used in event and state files for 'ViewFilter', plus more
import 'package:house_mate/pages/items_list/items_list.dart';

part 'items_list_event.dart';
part 'items_list_state.dart';

class ItemsListBloc extends Bloc<ItemsListEvent, ItemsListState> {
  ItemsListBloc({
    required ListItemsRepository listItemsRepository,
  })  : _listItemsRepository = listItemsRepository,
        super(const ItemsListState()) {
    on<ItemsListSubscriptionRequested>(_onSubscriptionRequested);
    on<ItemsListItemCompletionToggled>(_onItemCompletionToggled);
    on<ItemsListItemDeleted>(_onItemDeleted);
    on<ItemsListUndoDeletionRequested>(_onUndoDeletionRequested);
    on<ItemsListFilterChanged>(_onFilterChanged);
    on<ItemsListToggleAllRequested>(_onToggleAllRequested);
    on<ItemsListClearCompletedRequested>(_onClearCompletedRequested);
  }

  final ListItemsRepository _ListItemsRepository;

  Future<void> _onSubscriptionRequested(
    ItemsListSubscriptionRequested event,
    Emitter<ItemsListState> emit,
  ) async {
    emit(state.copyWith(status: () => ItemsListStatus.loading));

    await emit.forEach<List<ListItems>>(
      _listItemsRepository.getListItems(),
      onData: (items) => state.copyWith(
        status: () => ItemsListStatus.success,
        listItems: () => listItems,
      ),
      onError: (_, __) => state.copyWith(
        status: () => ItemsListStatus.failure,
      ),
    );
  }

  Future<void> _onItemCompletionToggled(
    ItemsListItemCompletionToggled event,
    Emitter<ItemsListState> emit,
  ) async {
    final newItem = event.listItem.copyWith(isCompleted: event.isCompleted);
    await _listItemsRepository.saveItem(newItem);
  }

  Future<void> _onItemDeleted(
    ItemsListItemDeleted event,
    Emitter<ItemsListState> emit,
  ) async {
    emit(state.copyWith(lastDeletedItem: () => event.listItem));
    await _listItemsRepository.deleteItem(event.listItem.id);
  }

  Future<void> _onUndoDeletionRequested(
    ItemsListUndoDeletionRequested event,
    Emitter<ItemsListState> emit,
  ) async {
    assert(
      state.lastDeletedItem != null,
      'Last deleted item can not be null.',
    );

    final item = state.lastDeletedItem!;
    emit(state.copyWith(lastDeletedItem: () => null));
    await _listItemsRepository.saveItem(item);
  }

  void _onFilterChanged(
    ItemsListFilterChanged event,
    Emitter<ItemsListState> emit,
  ) {
    emit(state.copyWith(filter: () => event.filter));
  }

  Future<void> _onToggleAllRequested(
    ItemsListToggleAllRequested event,
    Emitter<ItemsListState> emit,
  ) async {
    final areAllCompleted = state.listItems.every((item) => item.isCompleted);
    await _listItemsRepository.completeAll(isCompleted: !areAllCompleted);
  }

  Future<void> _onClearCompletedRequested(
    ItemsListClearCompletedRequested event,
    Emitter<ItemsListState> emit,
  ) async {
    await _listItemsRepository.clearCompleted();
  }
}
