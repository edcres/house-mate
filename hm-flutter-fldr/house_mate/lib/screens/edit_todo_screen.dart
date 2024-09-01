import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/data/models/chore_item.dart';
import 'package:house_mate/data/models/shopping_item.dart';
import 'package:house_mate/helper.dart';
import '../blocs/todo_bloc.dart';
import '../data/models/todo_item.dart';

class EditTodoScreen extends StatefulWidget {
  final int index;
  final TodoItem todo;

  EditTodoScreen({required this.index, required this.todo});

  @override
  _EditTodoScreenState createState() => _EditTodoScreenState();
}

class _EditTodoScreenState extends State<EditTodoScreen> {
  final TextEditingController _itemNameController = TextEditingController();
  final TextEditingController _quantityController = TextEditingController();
  final TextEditingController _locationController = TextEditingController();
  final TextEditingController _costController = TextEditingController();
  final TextEditingController _notesController = TextEditingController();
  DateTime? _dateNeeded;
  int? _priority;
  int? _difficulty;
  late ItemType _selectedItemType;
  final Helper helper = Helper();

  @override
  void initState() {
    super.initState();
    _selectedItemType = widget.todo.itemType;
    _initializeFields();
  }

  // void _initializeFields() {
  //   _itemNameController.text = widget.todo.name;
  //   if (widget.todo is ShoppingItem) {
  //     final shoppingItem = widget.todo as ShoppingItem;
  //     _quantityController.text = shoppingItem.quantity.toString();
  //     _locationController.text = shoppingItem.purchaseLocation;
  //     _costController.text = shoppingItem.cost.toString();
  //   } else if (widget.todo is ChoreItem) {
  //     final choreItem = widget.todo as ChoreItem;
  //     _difficulty = choreItem.difficulty;
  //   }
  //   _notesController.text = widget.todo.notes ?? '';
  //   _dateNeeded = widget.todo.neededBy != null
  //       ? helper.parseDate(widget.todo.neededBy)
  //       : null;
  //   _priority = widget.todo.priority ?? 3;
  // }

  void _initializeFields() {
    _itemNameController.text = widget.todo.name;
    if (widget.todo is ShoppingItem) {
      final shoppingItem = widget.todo as ShoppingItem;
      _quantityController.text = shoppingItem.quantity.toString();
      _locationController.text = shoppingItem.purchaseLocation;
      _costController.text = shoppingItem.cost.toString();
    } else if (widget.todo is ChoreItem) {
      final choreItem = widget.todo as ChoreItem;
      _difficulty = choreItem.difficulty;
    }
    _notesController.text = widget.todo.notes ?? '';
    _dateNeeded = helper.stringToDate(widget.todo.neededBy);
    _priority = widget.todo.priority ?? 3;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Edit Item'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            children: [
              TextField(
                controller: _itemNameController,
                decoration: InputDecoration(labelText: 'Item Name'),
              ),
              if (_selectedItemType == ItemType.Shopping) ...[
                TextField(
                  controller: _quantityController,
                  decoration: InputDecoration(labelText: 'Quantity'),
                  keyboardType: TextInputType.numberWithOptions(decimal: true),
                ),
                TextField(
                  controller: _locationController,
                  decoration: InputDecoration(labelText: 'Location'),
                ),
                TextField(
                  controller: _costController,
                  decoration: InputDecoration(labelText: 'Cost'),
                  keyboardType: TextInputType.numberWithOptions(decimal: true),
                ),
              ],
              ListTile(
                title: Text(_dateNeeded == null
                    ? 'Select Date Needed'
                    : 'Needed by ${helper.formatDate(_dateNeeded)}'),
                trailing: Icon(Icons.calendar_today),
                onTap: () async {
                  DateTime? picked = await showDatePicker(
                    context: context,
                    initialDate: _dateNeeded ?? DateTime.now(),
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
              if (_selectedItemType == ItemType.Chore)
                _buildDifficultyRadioButtons(),
              TextField(
                controller: _notesController,
                decoration: InputDecoration(labelText: 'Notes'),
                maxLines: 3,
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: _updateItem,
                child: Text('Update'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _updateItem() {
    final itemName = _itemNameController.text;
    if (itemName.isNotEmpty) {
      TodoItem updatedItem;
      if (_selectedItemType == ItemType.Shopping) {
        updatedItem = ShoppingItem(
          name: itemName,
          quantity: double.tryParse(_quantityController.text) ?? 0.0,
          cost: double.tryParse(_costController.text) ?? 0.0,
          purchaseLocation: _locationController.text,
        );
      } else {
        updatedItem = ChoreItem(
          name: itemName,
          difficulty: _difficulty ?? 1,
        );
      }
      updatedItem.neededBy = helper.formatDate(_dateNeeded);
      updatedItem.priority = _priority ?? 3;
      updatedItem.notes = _notesController.text;
      print("_____________     update 1");
      context
          .read<TodoBloc>()
          .add(UpdateItem(widget.todo.id, updatedItem, _selectedItemType));
      Navigator.of(context).pop();
    } else {
      // TODO: Show a warning to type a name
    }
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


// class EditTodoScreen extends StatefulWidget {
//   final int index;
//   final TodoItem todo;

//   EditTodoScreen({required this.index, required this.todo});

//   @override
//   _EditTodoScreenState createState() => _EditTodoScreenState();
// }

// class _EditTodoScreenState extends State<EditTodoScreen> {
//   final _controller = TextEditingController();

//   @override
//   void initState() {
//     super.initState();
//     _controller.text = widget.todo.name;
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: Text('Edit Item'),
//       ),
//       body: Padding(
//         padding: EdgeInsets.all(16.0),
//         child: Column(
//           children: [
//             TextField(
//               controller: _controller,
//               decoration: InputDecoration(labelText: 'Item'),
//             ),
//             SizedBox(height: 20),
//             ElevatedButton(
//               onPressed: () {
//                 final updatedTask = _controller.text;
//                 if (updatedTask.isNotEmpty) {
//                   context.read<TodoBloc>().add(UpdateItem(
//                         widget.todo.id,
//                         updatedTask,
//                         widget.todo.itemType,
//                       ));
//                 }
//                 Navigator.of(context).pop();
//               },
//               child: Text('Update'),
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }
