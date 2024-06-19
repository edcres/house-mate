import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../blocs/todo_bloc.dart';
import 'items_screen.dart';
import '../models/todo.dart';

class TabsScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        appBar: AppBar(
          title: Text('To-Do List'),
          bottom: TabBar(
            tabs: [
              Tab(text: 'Shopping'),
              Tab(text: 'Chores'),
            ],
          ),
          actions: [
            BlocBuilder<TodoBloc, TodoState>(
              builder: (context, state) {
                return IconButton(
                  icon: Icon(state.isEditMode ? Icons.done : Icons.edit),
                  onPressed: () {
                    if (state.isEditMode) {
                      context.read<TodoBloc>().add(ExitEditMode());
                    } else {
                      context.read<TodoBloc>().add(EnterEditMode());
                    }
                  },
                );
              },
            ),
          ],
        ),
        body: TabBarView(
          children: [
            ItemsScreen(itemType: ItemType.Shopping),
            ItemsScreen(itemType: ItemType.Chore),
          ],
        ),
        floatingActionButton: Builder(
          builder: (context) {
            return FloatingActionButton(
              onPressed: () {
                final tabController = DefaultTabController.of(context);
                final itemType = tabController?.index == 1
                    ? ItemType.Chore
                    : ItemType.Shopping;
                showDialog(
                  context: context,
                  builder: (context) {
                    return AddItemDialog(initialItemType: itemType);
                  },
                );
              },
              child: Icon(Icons.add),
            );
          },
        ),
      ),
    );
  }
}

class AddItemDialog extends StatefulWidget {
  final ItemType initialItemType;

  AddItemDialog({required this.initialItemType});

  @override
  _AddItemDialogState createState() => _AddItemDialogState();
}

class _AddItemDialogState extends State<AddItemDialog> {
  final _controller = TextEditingController();
  late ItemType _selectedItemType;

  @override
  void initState() {
    super.initState();
    _selectedItemType = widget.initialItemType;
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Add Item'),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          TextField(
            controller: _controller,
            decoration: InputDecoration(hintText: 'Enter item here'),
          ),
          DropdownButton<ItemType>(
            value: _selectedItemType,
            onChanged: (ItemType? newValue) {
              setState(() {
                _selectedItemType = newValue!;
              });
            },
            items: ItemType.values.map((ItemType classType) {
              return DropdownMenuItem<ItemType>(
                value: classType,
                child: Text(classType.toString().split('.').last),
              );
            }).toList(),
          ),
        ],
      ),
      actions: [
        TextButton(
          onPressed: () {
            final item = _controller.text;
            if (item.isNotEmpty) {
              context.read<TodoBloc>().add(AddItem(item, _selectedItemType));
            }
            Navigator.of(context).pop();
          },
          child: Text('Add'),
        ),
      ],
    );
  }
}
