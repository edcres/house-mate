import 'package:flutter/material.dart';
import '../data/models/todo.dart';

class TodoItem extends StatelessWidget {
  final Todo todo;
  final VoidCallback onTap;
  final VoidCallback onEdit;
  final VoidCallback onToggle;

  TodoItem(
      {required this.todo,
      required this.onTap,
      required this.onEdit,
      required this.onToggle});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(
        todo.task,
        style: TextStyle(
          decoration: todo.isCompleted ? TextDecoration.lineThrough : null,
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
              todo.isCompleted
                  ? Icons.check_box
                  : Icons.check_box_outline_blank,
            ),
            onPressed: onToggle,
          ),
        ],
      ),
    );
  }
}
