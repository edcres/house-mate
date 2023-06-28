part of 'items_list_bloc.dart';

enum ItemsListStatus { initial, loading, success, failure }

class ItemsListState extends Equatable {
  const ItemsListState({
    this.status = ItemsListStatus.initial,
    this.listItems = const [],
    this.filter = ItemsViewFilter.all,
    this.lastDeletedItem,
  });

  final ItemsListStatus status;
  final List<ListItem> listItems;
  final ItemsViewFilter filter;
  final ListItem? lastDeletedItem;

  Iterable<ListItem> get filteredItems => filter.applyAll(listItems);

  ItemsListState copyWith({
    ItemsListStatus Function()? status,
    List<ListItem> Function()? items,
    ItemsViewFilter Function()? filter,
    ListItem? Function()? lastDeletedItem,
  }) {
    return ItemsListState(
      status: status != null ? status() : this.status,
      listItems: items != null ? items() : this.listItems,
      filter: filter != null ? filter() : this.filter,
      lastDeletedItem:
          lastDeletedItem != null ? lastDeletedItem() : this.lastDeletedItem,
    );
  }

  @override
  List<Object?> get props => [
        status,
        listItems,
        filter,
        lastDeletedItems,
      ];
}
