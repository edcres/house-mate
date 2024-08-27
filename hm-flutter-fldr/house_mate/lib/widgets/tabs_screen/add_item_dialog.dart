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
  final TextEditingController _itemNameController = TextEditingController();
  final TextEditingController _quantityController = TextEditingController();
  final TextEditingController _locationController = TextEditingController();
  final TextEditingController _costController = TextEditingController();
  final TextEditingController _notesController = TextEditingController();
  DateTime? _dateNeeded;
  int? _priority;
  int? _difficulty;
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
      content: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: _itemNameController,
              decoration: InputDecoration(hintText: 'Enter item name'),
            ),
            _selectedItemType == ItemType.Shopping
                ? TextField(
                    controller: _quantityController,
                    decoration: InputDecoration(hintText: 'Enter quantity'),
                    keyboardType:
                        TextInputType.numberWithOptions(decimal: true),
                  )
                : SizedBox.shrink(),
            _selectedItemType == ItemType.Shopping
                ? TextField(
                    controller: _locationController,
                    decoration: InputDecoration(hintText: 'Enter location'),
                  )
                : SizedBox.shrink(),
            _selectedItemType == ItemType.Shopping
                ? TextField(
                    controller: _costController,
                    decoration: InputDecoration(hintText: 'Enter cost'),
                    keyboardType:
                        TextInputType.numberWithOptions(decimal: true),
                  )
                : SizedBox.shrink(),
            ListTile(
              title: Text(_dateNeeded == null
                  ? 'Select Date Needed'
                  : 'Needed by ${_dateNeeded!.month}/${_dateNeeded!.day}/${_dateNeeded!.year}'),
              trailing: Icon(Icons.calendar_today),
              onTap: () async {
                DateTime? picked = await showDatePicker(
                  context: context,
                  initialDate: DateTime.now(),
                  firstDate: DateTime(2000),
                  lastDate: DateTime(2100),
                );
                if (picked != null && picked != _dateNeeded) {
                  setState(() {
                    _dateNeeded = picked;
                  });
                }
              },
            ),
            _buildPriorityRadioButtons(),
            _selectedItemType == ItemType.Chore
                ? _buildDifficultyRadioButtons()
                : SizedBox.shrink(),
            TextField(
              controller: _notesController,
              decoration: InputDecoration(hintText: 'Enter notes'),
              maxLines: 3,
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () {
            final itemName = _itemNameController.text;
            if (itemName.isNotEmpty) {
              final newItem;
              switch (_selectedItemType) {
                case ItemType.Shopping:
                  newItem = ShoppingItem(
                    name: itemName,
                    quantity: double.tryParse(_quantityController.text) ?? 0.0,
                    cost: double.tryParse(_costController.text) ?? 0.0,
                    purchaseLocation: _locationController.text,
                  );
                  break;
                case ItemType.Chore:
                  newItem = ChoreItem(
                    name: itemName,
                    difficulty: _difficulty ?? 1,
                  );
                  break;
              }
              newItem.neededBy = _dateNeeded?.toString() ?? '';
              newItem.volunteer; // TODO: maybe do this in the bloc class
              newItem.priority = _priority ?? 3;
              newItem.notes = _notesController.text;
              print("_________________   addIt 1.5  ___");
              context.read<TodoBloc>().add(AddItem(newItem, _selectedItemType));
            }
            Navigator.of(context).pop();
          },
          child: Text('Add'),
        ),
      ],
    );
  }

  Widget _buildPriorityRadioButtons() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('Priority:'),
        Row(
          children: [
            Radio<int>(
              value: 1,
              groupValue: _priority,
              onChanged: (int? value) {
                setState(() {
                  _priority = value;
                });
              },
            ),
            Text('1'),
            Radio<int>(
              value: 2,
              groupValue: _priority,
              onChanged: (int? value) {
                setState(() {
                  _priority = value;
                });
              },
            ),
            Text('2'),
            Radio<int>(
              value: 3,
              groupValue: _priority,
              onChanged: (int? value) {
                setState(() {
                  _priority = value;
                });
              },
            ),
            Text('3'),
          ],
        ),
      ],
    );
  }

  Widget _buildDifficultyRadioButtons() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('Difficulty:'),
        Row(
          children: [
            Radio<int>(
              value: 1,
              groupValue: _difficulty,
              onChanged: (int? value) {
                setState(() {
                  _difficulty = value;
                });
              },
            ),
            Text('1'),
            Radio<int>(
              value: 2,
              groupValue: _difficulty,
              onChanged: (int? value) {
                setState(() {
                  _difficulty = value;
                });
              },
            ),
            Text('2'),
            Radio<int>(
              value: 3,
              groupValue: _difficulty,
              onChanged: (int? value) {
                setState(() {
                  _difficulty = value;
                });
              },
            ),
            Text('3'),
          ],
        ),
      ],
    );
  }
}
