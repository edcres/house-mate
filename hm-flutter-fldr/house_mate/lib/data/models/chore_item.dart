import 'todo_item.dart';

class ChoreItem extends TodoItem {
  ChoreItem({
    required String id,
    required String task,
    required bool isCompleted,
  }) : super(
            id: id,
            task: task,
            isCompleted: isCompleted,
            itemType: ItemType.Chore);

  @override
  List<Object> get props => [id, task, isCompleted, itemType];
}
