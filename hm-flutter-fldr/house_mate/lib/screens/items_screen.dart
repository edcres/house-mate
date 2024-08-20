import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import '../blocs/todo_bloc.dart';
import 'edit_todo_screen.dart';
import '../data/models/todo_item.dart';

class ItemsScreen extends StatelessWidget {
  final ItemType itemType;

  ItemsScreen({required this.itemType});

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<TodoBloc, TodoState>(
      builder: (context, state) {
        final items =
            state.items.where((item) => item.itemType == itemType).toList();

        return ListView.builder(
          itemCount: items.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(
                items[index].name,
                style: TextStyle(
                  decoration: items[index].completed
                      ? TextDecoration.lineThrough
                      : null,
                ),
              ),
              trailing: state.isEditMode
                  ? Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(
                          icon: Icon(Icons.edit),
                          onPressed: () {
                            Navigator.of(context).push(MaterialPageRoute(
                              builder: (context) => EditTodoScreen(
                                index: state.items.indexOf(items[index]),
                                todo: items[index],
                              ),
                            ));
                          },
                        ),
                        IconButton(
                          icon: Icon(Icons.delete),
                          onPressed: () {
                            context
                                .read<TodoBloc>()
                                .add(DeleteItem(items[index].id, itemType));
                          },
                        ),
                      ],
                    )
                  : IconButton(
                      icon: Icon(
                        items[index].completed
                            ? Icons.check_box
                            : Icons.check_box_outline_blank,
                      ),
                      onPressed: () {
                        context
                            .read<TodoBloc>()
                            .add(ToggleItem(items[index].id, itemType));
                      },
                    ),
              onTap: !state.isEditMode
                  ? () {
                      showModalBottomSheet(
                        context: context,
                        builder: (context) {
                          return Container(
                            width: MediaQuery.of(context).size.width,
                            padding: EdgeInsets.all(16.0),
                            child: Column(
                              mainAxisSize: MainAxisSize.min,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  items[index].name,
                                  style: TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                SizedBox(height: 10),
                                Text(
                                  'Completed: ${items[index].completed ? 'Yes' : 'No'}',
                                  style: TextStyle(
                                    fontSize: 16,
                                  ),
                                ),
                                SizedBox(height: 10),
                                Text(
                                  'Type: ${items[index].itemType == ItemType.Shopping ? 'Shopping' : 'Chore'}',
                                  style: TextStyle(
                                    fontSize: 16,
                                  ),
                                ),
                              ],
                            ),
                          );
                        },
                      );
                    }
                  : null,
            );
          },
        );
      },
    );
  }
}
