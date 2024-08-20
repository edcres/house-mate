import 'todo_item.dart';

class ChoreItem extends TodoItem {
  ChoreItem({
    required String id,
    required String name,
    required bool addedBy,
    required bool completed,
    // Not required
    String neededBy = "",
    String volunteer = "",
    int priority = 3,
    String notes = "",
    // Only for Chores
    int difficulty = 1,
  }) : super(
          id: id,
          name: name,
          addedBy: addedBy,
          completed: completed,
          neededBy: neededBy,
          volunteer: volunteer,
          priority: priority,
          notes: notes,
          itemType: ItemType.Chore, // TODO: get rid of this
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
