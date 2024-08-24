import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/data/models/chore_item.dart';
import 'package:house_mate/data/models/shopping_item.dart';
import '../../data/models/todo_item.dart';

class AddItemDialog extends StatefulWidget {
  final ItemType initialItemType;

  AddItemDialog({required this.initialItemType});

  @override
  _AddItemDialogState createState() => _AddItemDialogState();
}

class _AddItemDialogState extends State<AddItemDialog> {
  final TextEditingController _controller = TextEditingController();
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
            // TODO: get rid of this
            // final item = _controller.text;
            // if (item.isNotEmpty) {
            //   context.read<TodoBloc>().add(AddItem(item, _selectedItemType));
            // }
            final itemName = _controller.text;
            if (itemName.isNotEmpty) {
              final newItem;
              switch (_selectedItemType) {
                case ItemType.Shopping:
                  newItem = ShoppingItem(
                    id: '', // Generate or provide an ID
                    name: itemName,
                    addedBy: 'user_id', // Replace with actual user ID
                    completed: false,
                    // Set other fields as needed or with default values
                  );
                  break;
                case ItemType.Chore:
                  newItem = ChoreItem(
                    id: '', // Generate or provide an ID
                    name: itemName,
                    addedBy: 'user_id', // Replace with actual user ID
                    completed: false,
                    // Set other fields as needed or with default values
                  );
                  break;
              }
              context.read<TodoBloc>().add(AddItem(newItem, _selectedItemType));
            }
            Navigator.of(context).pop();
            Navigator.of(context).pop();
          },
          child: Text('Add'),
        ),
      ],
    );
  }
}
