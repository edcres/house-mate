import 'todo_item.dart';

class ShoppingItem extends TodoItem {
  ShoppingItem({
    required String id,
    required String task,
    required bool isCompleted,
  }) : super(
            id: id,
            task: task,
            isCompleted: isCompleted,
            itemType: ItemType.Shopping);

  @override
  List<Object> get props => [id, task, isCompleted, itemType];
}
