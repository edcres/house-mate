import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import '../blocs/todo_bloc.dart';
import '../data/models/todo_item.dart';

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
