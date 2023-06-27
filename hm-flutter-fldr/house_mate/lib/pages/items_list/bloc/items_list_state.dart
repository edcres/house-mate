part of 'items_list_bloc.dart';

enum ItemsOverviewStatus { initial, loading, success, failure }

class ItemsOverviewState extends Equatable {
  const ItemsOverviewState({
    this.status = ItemsOverviewStatus.initial,
    this.listItems = const [],
    this.filter = ItemsViewFilter.all,
    this.lastDeletedItem,
  });

  final ItemsOverviewStatus status;
  final List<ListItem> listItems;
  final ItemsViewFilter filter;
  final ListItem? lastDeletedItem;

  Iterable<ListItem> get filteredItems => filter.applyAll(listItems);

  ItemsOverviewState copyWith({
    ItemsOverviewStatus Function()? status,
    List<ListItem> Function()? items,
    ItemsViewFilter Function()? filter,
    ListItem? Function()? lastDeletedItem,
  }) {
    return ItemsOverviewState(
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
