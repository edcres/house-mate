import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../blocs/todo_bloc.dart';
import '../models/todo.dart';

class EditTodoScreen extends StatefulWidget {
  final int index;
  final Todo todo;

  EditTodoScreen({required this.index, required this.todo});

  @override
  _EditTodoScreenState createState() => _EditTodoScreenState();
}

class _EditTodoScreenState extends State<EditTodoScreen> {
  final _controller = TextEditingController();

  @override
  void initState() {
    super.initState();
    _controller.text = widget.todo.task;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Edit Item'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _controller,
              decoration: InputDecoration(labelText: 'Item'),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                final updatedTask = _controller.text;
                if (updatedTask.isNotEmpty) {
                  context.read<TodoBloc>().add(UpdateItem(
                      widget.todo.id, updatedTask, widget.todo.itemType));
                }
                Navigator.of(context).pop();
              },
              child: Text('Update'),
            ),
          ],
        ),
      ),
    );
  }
}
