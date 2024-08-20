import 'todo_item.dart';

class ChoreItem extends TodoItem {
  ChoreItem({
    required String id,
    required String task,
    required bool isCompleted,
  }) : super(
            id: id,
            name: task,
            completed: isCompleted,
            itemType: ItemType.Chore);

  @override
  List<Object> get props => [id, name, completed, itemType];
}
