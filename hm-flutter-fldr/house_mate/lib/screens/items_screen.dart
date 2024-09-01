import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/blocs/todo_event.dart';
import 'package:house_mate/blocs/todo_state.dart';
import 'package:house_mate/data/models/chore_item.dart';
import 'package:house_mate/data/models/shopping_item.dart';
import 'package:house_mate/helper.dart';
import '../blocs/todo_bloc.dart';
import 'edit_todo_screen.dart';
import '../data/models/todo_item.dart';

class ItemsScreen extends StatelessWidget {
  final ItemType itemType;
  Helper helper = Helper();

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
                      // showModalBottomSheet(
                      //   context: context,
                      //   builder: (context) {
                      //     final item = items[index];
                      //     final TextEditingController volunteerController =
                      //         TextEditingController(text: item.volunteer ?? '');

                      //     return StatefulBuilder(
                      //       builder:
                      //           (BuildContext context, StateSetter setState) {
                      //         return Container(
                      //           width: MediaQuery.of(context).size.width,
                      //           padding: EdgeInsets.all(16.0),
                      //           child: Column(
                      //             mainAxisSize: MainAxisSize.min,
                      //             crossAxisAlignment: CrossAxisAlignment.start,
                      //             children: [
                      //               if (item.name.isNotEmpty)
                      //                 Column(
                      //                   crossAxisAlignment:
                      //                       CrossAxisAlignment.start,
                      //                   children: [
                      //                     Text(
                      //                       item.name,
                      //                       style: TextStyle(
                      //                         fontSize: 20,
                      //                         fontWeight: FontWeight.bold,
                      //                       ),
                      //                     ),
                      //                     SizedBox(height: 10),
                      //                   ],
                      //                 ),
                      //               // Fields specific to ShoppingItem
                      //               if (item is ShoppingItem) ...[
                      //                 if (item.quantity != null &&
                      //                     item.quantity > 0)
                      //                   Text(
                      //                     'Quantity: ${item.quantity}',
                      //                     style: TextStyle(
                      //                       fontSize: 16,
                      //                     ),
                      //                   ),
                      //                 if (item.cost != null && item.cost > 0)
                      //                   Text(
                      //                     'Cost: \$${item.cost.toStringAsFixed(2)}',
                      //                     style: TextStyle(
                      //                       fontSize: 16,
                      //                     ),
                      //                   ),
                      //                 if (item.purchaseLocation.isNotEmpty)
                      //                   Text(
                      //                     'Purchase Location: ${item.purchaseLocation}',
                      //                     style: TextStyle(
                      //                       fontSize: 16,
                      //                     ),
                      //                   ),
                      //               ],
                      //               // Fields specific to ChoreItem
                      //               if (item is ChoreItem) ...[
                      //                 if (item.difficulty != null)
                      //                   Text(
                      //                     'Difficulty: ${item.difficulty}',
                      //                     style: TextStyle(
                      //                       fontSize: 16,
                      //                     ),
                      //                   ),
                      //               ],
                      //               // Common fields for both ShoppingItem and ChoreItem
                      //               if (item.neededBy != null &&
                      //                   item.neededBy.isNotEmpty)
                      //                 Text(
                      //                   'Needed By: ${item.neededBy}',
                      //                   style: TextStyle(
                      //                     fontSize: 16,
                      //                   ),
                      //                 ),
                      //               if (item.priority != null)
                      //                 Text(
                      //                   'Priority: ${item.priority}',
                      //                   style: TextStyle(
                      //                     fontSize: 16,
                      //                   ),
                      //                 ),
                      //               if (item.addedBy != null &&
                      //                   item.addedBy != helper.ANON_STRING)
                      //                 Text(
                      //                   'Added By: ${item.addedBy}',
                      //                   style: TextStyle(
                      //                     fontSize: 16,
                      //                   ),
                      //                 ),
                      //               if (item.notes != null &&
                      //                   item.notes.isNotEmpty)
                      //                 Text(
                      //                   'Notes: ${item.notes}',
                      //                   style: TextStyle(
                      //                     fontSize: 16,
                      //                   ),
                      //                 ),
                      //               // Edit box for volunteer field
                      //               SizedBox(height: 10),
                      //               Text(
                      //                 'Volunteer:',
                      //                 style: TextStyle(
                      //                     fontSize: 16,
                      //                     fontWeight: FontWeight.bold),
                      //               ),
                      //               TextFormField(
                      //                 controller: volunteerController,
                      //                 decoration: InputDecoration(
                      //                   hintText: 'Enter volunteer name',
                      //                 ),
                      //               ),
                      //               SizedBox(height: 10),
                      //               ElevatedButton(
                      //                 onPressed: () {
                      //                   // Update the volunteer field in the database
                      //                   context.read<TodoBloc>().add(
                      //                         UpdateItem(
                      //                           item.id,
                      //                           volunteerController.text,
                      //                           item.itemType,
                      //                         ),
                      //                       );
                      //                   Navigator.of(context).pop();
                      //                 },
                      //                 child: Text('Update Volunteer'),
                      //               ),
                      //             ],
                      //           ),
                      //         );
                      //       },
                      //     );
                      //   },
                      // );
                      showModalBottomSheet(
                        context: context,
                        isScrollControlled:
                            true, // Allows the bottom sheet to take more space
                        builder: (context) {
                          final item = items[index];
                          final TextEditingController volunteerController =
                              TextEditingController(text: item.volunteer ?? '');

                          return StatefulBuilder(
                            builder:
                                (BuildContext context, StateSetter setState) {
                              return Padding(
                                padding: EdgeInsets.only(
                                  bottom: MediaQuery.of(context)
                                      .viewInsets
                                      .bottom, // Adjusts for keyboard
                                ),
                                child: SingleChildScrollView(
                                  child: Container(
                                    padding: EdgeInsets.all(16.0),
                                    child: Column(
                                      mainAxisSize: MainAxisSize.min,
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                        if (item.name.isNotEmpty)
                                          Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                item.name,
                                                style: TextStyle(
                                                  fontSize: 20,
                                                  fontWeight: FontWeight.bold,
                                                ),
                                              ),
                                              SizedBox(height: 10),
                                            ],
                                          ),
                                        if (item.completed != null)
                                          Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                'Completed: ${item.completed ? 'Yes' : 'No'}',
                                                style: TextStyle(
                                                  fontSize: 16,
                                                ),
                                              ),
                                              SizedBox(height: 10),
                                            ],
                                          ),
                                        if (item.itemType != null)
                                          Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                'Type: ${item.itemType == ItemType.Shopping ? 'Shopping' : 'Chore'}',
                                                style: TextStyle(
                                                  fontSize: 16,
                                                ),
                                              ),
                                              SizedBox(height: 10),
                                            ],
                                          ),
                                        // Fields specific to ShoppingItem
                                        if (item is ShoppingItem) ...[
                                          if (item.quantity != null &&
                                              item.quantity > 0)
                                            Text(
                                              'Quantity: ${item.quantity}',
                                              style: TextStyle(
                                                fontSize: 16,
                                              ),
                                            ),
                                          if (item.cost != null &&
                                              item.cost > 0)
                                            Text(
                                              'Cost: \$${item.cost.toStringAsFixed(2)}',
                                              style: TextStyle(
                                                fontSize: 16,
                                              ),
                                            ),
                                          if (item.purchaseLocation.isNotEmpty)
                                            Text(
                                              'Purchase Location: ${item.purchaseLocation}',
                                              style: TextStyle(
                                                fontSize: 16,
                                              ),
                                            ),
                                        ],
                                        // Fields specific to ChoreItem
                                        if (item is ChoreItem) ...[
                                          if (item.difficulty != null)
                                            Text(
                                              'Difficulty: ${item.difficulty}',
                                              style: TextStyle(
                                                fontSize: 16,
                                              ),
                                            ),
                                        ],
                                        // Common fields for both ShoppingItem and ChoreItem
                                        if (item.neededBy != null &&
                                            item.neededBy.isNotEmpty)
                                          Text(
                                            'Needed By: ${item.neededBy}',
                                            style: TextStyle(
                                              fontSize: 16,
                                            ),
                                          ),
                                        if (item.priority != null)
                                          Text(
                                            'Priority: ${item.priority}',
                                            style: TextStyle(
                                              fontSize: 16,
                                            ),
                                          ),
                                        if (item.addedBy != null &&
                                            item.addedBy.isNotEmpty)
                                          Text(
                                            'Added By: ${item.addedBy}',
                                            style: TextStyle(
                                              fontSize: 16,
                                            ),
                                          ),
                                        if (item.notes != null &&
                                            item.notes.isNotEmpty)
                                          Text(
                                            'Notes: ${item.notes}',
                                            style: TextStyle(
                                              fontSize: 16,
                                            ),
                                          ),
                                        // Edit box for volunteer field
                                        SizedBox(height: 10),
                                        TextFormField(
                                          controller: volunteerController,
                                          decoration: InputDecoration(
                                            hintText: item.volunteer.isNotEmpty
                                                ? item.volunteer
                                                : 'Volunteer',
                                          ),
                                        ),
                                        SizedBox(height: 10),
                                        ElevatedButton(
                                          onPressed: () {
                                            // Update the volunteer field in the database
                                            // TODO: Change this so it updates only the volunteer
                                            context.read<TodoBloc>().add(
                                                  UpdateItem(
                                                    item,
                                                    item.itemType,
                                                  ),
                                                );
                                            Navigator.of(context)
                                                .pop(); // Close the bottom sheet after updating
                                          },
                                          child: Text('Update Volunteer'),
                                        ),
                                      ],
                                    ),
                                  ),
                                ),
                              );
                            },
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
