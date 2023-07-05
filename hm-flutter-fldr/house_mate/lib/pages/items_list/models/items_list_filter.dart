enum ItemsViewFilter { all, activeOnly, completedOnly }

extension ItemsViewFilterX on ItemsViewFilter {
  bool apply(ListItem item) {
    switch (this) {
      case ItemViewFilter.all:
        return true;
      case ItemViewFilter.activeOnly:
        return !item.isCompleted;
      case ItemViewFilter.completedOnly:
        return item.isCompleted;
    }
  }

  Iterable<ListItem> applyAll(Iterable<ListItem> items) {
    return items.where(apply);
  }
}
