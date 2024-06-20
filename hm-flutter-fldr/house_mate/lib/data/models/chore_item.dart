import 'todo.dart';

class ChoreItem extends Todo {
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
