import 'todo_item.dart';

class ShoppingItem extends TodoItem {
  ShoppingItem({
    required String id,
    required String name,
    required bool addedBy,
    required bool completed,
    // Not required
    String neededBy = "",
    String volunteer = "",
    int priority = 3,
    String notes = "",
    // Only for Shopping
    double quantity = 0.0,
    double cost = 0.0,
    String purchaseLocation = "",
  }) : super(
          id: id,
          name: name,
          addedBy: addedBy,
          completed: completed,
          neededBy: neededBy,
          volunteer: volunteer,
          priority: priority,
          notes: notes,
          itemType: ItemType.Shopping, // TODO: get rid of this
        );

  @override
  List<Object> get props => [
        id,
        name,
        addedBy,
        completed,
        neededBy,
        volunteer,
        priority,
        notes,
        itemType,
      ];
}
