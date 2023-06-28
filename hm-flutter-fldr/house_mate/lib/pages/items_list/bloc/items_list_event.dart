part of 'items_list_bloc.dart';

abstract class ItemsListEvent extends Equatable {
  const ItemsListEvent();

  @override
  List<Object> get props => [];
}

class ItemsListSubscriptionRequested extends ItemsListEvent {
  const ItemsListSubscriptionRequested();
}

class ItemsListItemCompletionToggled extends ItemsListEvent {
  const ItemsListItemCompletionToggled({
    required this.listItem,
    required this.isCompleted,
  });

  final ListItem listItem;
  final bool isCompleted;

  @override
  List<Object> get props => [listItem, isCompleted];
}

class ItemsListItemDeleted extends ItemsListEvent {
  const ItemsListItemDeleted(this.listItem);

  final ListItem listItem;

  @override
  List<Object> get props => [listItem];
}

class ItemsListUndoDeletionRequested extends ItemsListEvent {
  const ItemsListUndoDeletionRequested();
}

class ItemsListFilterChanged extends ItemsListEvent {
  const ItemsListFilterChanged(this.filter);

  final ItemsListFilter filter;

  @override
  List<Object> get props => [filter];
}

class ItemsListToggleAllRequested extends ItemsListEvent {
  const ItemsListToggleAllRequested();
}

class ItemsListClearCompletedRequested extends ItemsListEvent {
  const ItemsListClearCompletedRequested();
}
