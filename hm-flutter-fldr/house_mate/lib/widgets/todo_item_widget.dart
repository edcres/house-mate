import 'package:flutter/material.dart';
import '../data/models/todo_item.dart';

class TodoItemWidget extends StatelessWidget {
  final TodoItem todo;
  final VoidCallback onTap;
  final VoidCallback onEdit;
  final VoidCallback onToggle;

  TodoItemWidget(
      {required this.todo,
      required this.onTap,
      required this.onEdit,
      required this.onToggle});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(
        todo.name,
        style: TextStyle(
          decoration: todo.completed ? TextDecoration.lineThrough : null,
        ),
      ),
      onTap: onTap,
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconButton(
            icon: Icon(Icons.edit),
            onPressed: onEdit,
          ),
          IconButton(
            icon: Icon(
              todo.completed ? Icons.check_box : Icons.check_box_outline_blank,
            ),
            onPressed: onToggle,
          ),
        ],
      ),
    );
  }
}
