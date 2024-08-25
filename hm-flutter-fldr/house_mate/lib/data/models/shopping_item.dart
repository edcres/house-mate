import 'package:house_mate/data/models/todo_item.dart';

class ShoppingItem extends TodoItem {
  static const String fieldQuantity = 'quantity';
  static const String fieldCost = 'cost';
  static const String fieldPurchaseLocation = 'purchaseLocation';

  final double quantity;
  final double cost;
  final String purchaseLocation;

  ShoppingItem({
    required String name,
    this.quantity = 0.0,
    this.cost = 0.0,
    this.purchaseLocation = "",
  }) : super(
          name: name,
          itemType: ItemType.Shopping,
        );

  ShoppingItem.fromJson(Map<String, dynamic> json)
      : quantity = (json[fieldQuantity] as num?)?.toDouble() ?? 0.0,
        cost = (json[fieldCost] as num?)?.toDouble() ?? 0.0,
        purchaseLocation = json[fieldPurchaseLocation] as String? ?? "",
        super(
          id: json[TodoItem.fieldId] as String,
          name: json[TodoItem.fieldName] as String,
          addedBy: json[TodoItem.fieldAddedBy] as String,
          itemType: ItemType.Shopping,
        ) {
    // Set the common fields using the parent class method
    TodoItem.setCommonFields(this, json);
  }

  @override
  List<Object?> get props =>
      super.props..addAll([quantity, cost, purchaseLocation]);

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

// class ShoppingItem extends TodoItem {
//   static const String fieldQuantity = 'quantity';
//   static const String fieldCost = 'cost';
//   static const String fieldPurchaseLocation = 'purchaseLocation';

//   final double quantity;
//   final double cost;
//   final String purchaseLocation;

//   ShoppingItem({
//     required String id,
//     required String name,
//     required String addedBy,
//     this.quantity = 0.0,
//     this.cost = 0.0,
//     this.purchaseLocation = "",
//   }) : super(
//           id: id,
//           name: name,
//           addedBy: addedBy,
//           itemType: ItemType.Shopping,
//         );

//   @override
//   List<Object?> get props =>
//       super.props..addAll([quantity, cost, purchaseLocation]);

//   factory ShoppingItem.fromJson(Map<String, dynamic> json) {
//     final commonFields = TodoItem.commonFromJson(json);
//     return ShoppingItem(
//       id: commonFields[TodoItem.fieldId],
//       name: commonFields[TodoItem.fieldName],
//       addedBy: commonFields[TodoItem.fieldAddedBy],
//       // TODO: These fields give me errors
//       // completed: commonFields[TodoItem.fieldCompleted],
//       // neededBy: commonFields[TodoItem.fieldNeededBy],
//       // volunteer: commonFields[TodoItem.fieldVolunteer],
//       // priority: commonFields[TodoItem.fieldPriority],
//       // notes: commonFields[TodoItem.fieldNotes],
//       quantity: (json[fieldQuantity] as num?)?.toDouble() ?? 0.0,
//       cost: (json[fieldCost] as num?)?.toDouble() ?? 0.0,
//       purchaseLocation: json[fieldPurchaseLocation] as String? ?? "",
//     );
//   }

//   @override
//   Map<String, dynamic> toJson() {
//     final json = super.toJson();
//     json.addAll({
//       fieldQuantity: quantity,
//       fieldCost: cost,
//       fieldPurchaseLocation: purchaseLocation,
//     });
//     return json;
//   }
// }
