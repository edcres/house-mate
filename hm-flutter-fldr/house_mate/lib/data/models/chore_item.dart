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
            name: task,
            completed: isCompleted,
            itemType: ItemType.Chore);

  @override
  List<Object> get props => [id, name, completed, itemType];
}
