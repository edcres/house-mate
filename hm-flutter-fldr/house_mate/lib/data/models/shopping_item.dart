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
  }) : super(
    id: id,
    name: name,
    addedBy: addedBy,
    completed: completed,
    neededBy: neededBy,
    volunteer: volunteer,
    priority: priority,
    notes: notes,
    itemType: ItemType.Shopping,
        );

  @override
  List<Object> get props => [
        id,
        name,
        completed,
        itemType,
      ];
}


required this.id,
    required this.name,
    required this.addedBy,
    required this.completed,
    required this.neededBy,
    required this.volunteer,
    required this.priority,
    required this.notes,
    required this.itemType,