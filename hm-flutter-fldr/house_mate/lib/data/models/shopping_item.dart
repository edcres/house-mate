import 'todo.dart';

class ShoppingItem extends Todo {
  ShoppingItem({
    required String id,
    required String task,
    required bool isCompleted,
  }) : super(
            id: id,
            task: task,
            isCompleted: isCompleted,
            itemType: ItemType.Shopping);

  @override
  List<Object> get props => [id, task, isCompleted, itemType];
}

// import 'dart:ffi';

// class ShoppingItem {
//   ShoppingItem(
//       {required this.name,
//       required this.quantity,
//       required this.addedBy,
//       required this.completed,
//       required this.cost,
//       required this.purchaseLocation,
//       required this.neededBy,
//       required this.volunteer,
//       required this.priority});
//   // ShoppingItem({required this.name, required this.message});   ojo on {}

//   // TODO: probalby give them a default value and remove final keyword
//   // TODO: make them ? nullable wherein appropriate. Take out some requred from parameters
//   final String name;
//   final Double quantity;
//   final String addedBy;
//   final Bool completed;

//   // Not necessary and not used in Chores
//   final Double cost;
//   final String purchaseLocation;

//   // Not necessary and used in chores
//   final String neededBy;
//   final String volunteer;
//   final Int priority;
// }
