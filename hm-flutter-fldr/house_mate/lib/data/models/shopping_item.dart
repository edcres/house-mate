import 'package:house_mate/data/models/todo_item.dart';

class ShoppingItem extends TodoItem {
  static const String fieldQuantity = 'quantity';
  static const String fieldCost = 'cost';
  static const String fieldPurchaseLocation = 'purchaseLocation';

  final double quantity;
  final double cost;
  final String purchaseLocation;

  const ShoppingItem({
    required String id,
    required String name,
    required String addedBy,
    required bool completed,
    String neededBy = "",
    String volunteer = "",
    int priority = 3,
    String notes = "",
    this.quantity = 0.0,
    this.cost = 0.0,
    this.purchaseLocation = "",
  }) : super(
          id: id,
          name: name,
          addedBy: addedBy,
          completed: completed,
          neededBy: neededBy,
          volunteer: volunteer,
          priority: priority,
          notes: notes,
        );

  @override
  List<Object?> get props =>
      super.props..addAll([quantity, cost, purchaseLocation]);

  factory ShoppingItem.fromJson(Map<String, dynamic> json) {
    final commonFields = TodoItem.commonFromJson(json);
    return ShoppingItem(
      id: commonFields[TodoItem.fieldId],
      name: commonFields[TodoItem.fieldName],
      addedBy: commonFields[TodoItem.fieldAddedBy],
      completed: commonFields[TodoItem.fieldCompleted],
      neededBy: commonFields[TodoItem.fieldNeededBy],
      volunteer: commonFields[TodoItem.fieldVolunteer],
      priority: commonFields[TodoItem.fieldPriority],
      notes: commonFields[TodoItem.fieldNotes],
      quantity: (json[fieldQuantity] as num?)?.toDouble() ?? 0.0,
      cost: (json[fieldCost] as num?)?.toDouble() ?? 0.0,
      purchaseLocation: json[fieldPurchaseLocation] as String? ?? "",
    );
  }

  @override
  Map<String, dynamic> toJson() {
    final json = super.toJson();
    json.addAll({
      fieldQuantity: quantity,
      fieldCost: cost,
      fieldPurchaseLocation: purchaseLocation,
    });
    return json;
  }
}





// TODO: get rid of this
//import 'todo_item.dart';

// class ShoppingItem extends TodoItem {
//   final double quantity;
//   final double cost;
//   final String purchaseLocation;

//   ShoppingItem({
//     required String id,
//     required String name,
//     required String addedBy,
//     required bool completed,
//     String neededBy = "",
//     String volunteer = "",
//     int priority = 3,
//     String notes = "",
//     this.quantity = 0.0,
//     this.cost = 0.0,
//     this.purchaseLocation = "",
//   }) : super(
//           id: id,
//           name: name,
//           addedBy: addedBy,
//           completed: completed,
//           neededBy: neededBy,
//           volunteer: volunteer,
//           priority: priority,
//           notes: notes,
//           itemType: ItemType.Shopping,
//         );

//   @override
//   List<Object> get props => [
//         id,
//         name,
//         addedBy,
//         completed,
//         neededBy,
//         volunteer,
//         priority,
//         notes,
//         quantity,
//         cost,
//         purchaseLocation,
//       ];

//   factory ShoppingItem.fromJson(Map<String, dynamic> json) {
//     return ShoppingItem(
//       id: json['id'] as String,
//       name: json['name'] as String,
//       addedBy: json['addedBy'] as String,
//       completed: json['completed'] as bool,
//       neededBy: json['neededBy'] as String? ?? "",
//       volunteer: json['volunteer'] as String? ?? "",
//       priority: json['priority'] as int? ?? 3,
//       notes: json['notes'] as String? ?? "",
//       quantity: (json['quantity'] as num?)?.toDouble() ?? 0.0,
//       cost: (json['cost'] as num?)?.toDouble() ?? 0.0,
//       purchaseLocation: json['purchaseLocation'] as String? ?? "",
//     );
//   }

//   Map<String, dynamic> toJson() {
//     final json = super.toJson();
//     json.addAll({
//       'quantity': quantity,
//       'cost': cost,
//       'purchaseLocation': purchaseLocation,
//     });
//     return json;
//   }
// }


// // class ShoppingItem extends TodoItem {
// //   ShoppingItem({
// //     required String id,
// //     required String name,
// //     required bool addedBy,
// //     required bool completed,
// //     // Not required
// //     String neededBy = "",
// //     String volunteer = "",
// //     int priority = 3,
// //     String notes = "",
// //     // Only for Shopping
// //     double quantity = 0.0,
// //     double cost = 0.0,
// //     String purchaseLocation = "",
// //   }) : super(
// //           id: id,
// //           name: name,
// //           addedBy: addedBy,
// //           completed: completed,
// //           neededBy: neededBy,
// //           volunteer: volunteer,
// //           priority: priority,
// //           notes: notes,
// //           itemType: ItemType.Shopping, // TODO: get rid of this
// //         );

// //   @override
// //   List<Object> get props => [
// //         id,
// //         name,
// //         addedBy,
// //         completed,
// //         neededBy,
// //         volunteer,
// //         priority,
// //         notes,
// //         itemType,
// //       ];
// // }

