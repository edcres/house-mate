import 'package:equatable/equatable.dart';
import 'package:house_mate/data/models/chore_item.dart';
import 'package:house_mate/data/models/shopping_item.dart';

enum ItemType { Shopping, Chore }

abstract class TodoItem extends Equatable {
  // Immutable field names
  static const String fieldId = 'id';
  static const String fieldName = 'name';
  static const String fieldAddedBy = 'addedBy';
  static const String fieldCompleted = 'completed';
  static const String fieldNeededBy = 'neededBy';
  static const String fieldVolunteer = 'volunteer';
  static const String fieldPriority = 'priority';
  static const String fieldNotes = 'notes';

  final String id;
  final String name;
  final String addedBy;
  final bool completed;
  final String neededBy;
  final String volunteer;
  final int priority;
  final String notes;

  const TodoItem({
    required this.id,
    required this.name,
    required this.addedBy,
    required this.completed,
    this.neededBy = "",
    this.volunteer = "",
    this.priority = 3,
    this.notes = "",
  });

  Map<String, dynamic> toJson() {
    return {
      fieldId: id,
      fieldName: name,
      fieldAddedBy: addedBy,
      fieldCompleted: completed,
      fieldNeededBy: neededBy,
      fieldVolunteer: volunteer,
      fieldPriority: priority,
      fieldNotes: notes,
    };
  }

  static TodoItem fromJson(Map<String, dynamic> json, ItemType itemType) {
    switch (itemType) {
      case ItemType.Shopping:
        return ShoppingItem.fromJson(json);
      case ItemType.Chore:
        return ChoreItem.fromJson(json);
      default:
        throw Exception('Unknown item type');
    }
  }

  // static TodoItem fromJsonn(Map<String, dynamic> json) {
  //   switch (ItemType.values
  //       .firstWhere((e) => e.toString() == 'ItemType.${json[fieldItemType]}')) {
  //     case ItemType.Shopping:
  //       return ShoppingItem.fromJson(json);
  //     case ItemType.Chore:
  //       return ChoreItem.fromJson(json);
  //   }
  // }

  @override
  List<Object?> get props => [
        id,
        name,
        addedBy,
        completed,
        neededBy,
        volunteer,
        priority,
        notes,
      ];

  // Common deserialization logic
  static Map<String, dynamic> commonFromJson(Map<String, dynamic> json) {
    return {
      fieldId: json[fieldId] as String,
      fieldName: json[fieldName] as String,
      fieldAddedBy: json[fieldAddedBy] as String,
      fieldCompleted: json[fieldCompleted] as bool,
      fieldNeededBy: json[fieldNeededBy] as String? ?? "",
      fieldVolunteer: json[fieldVolunteer] as String? ?? "",
      fieldPriority: json[fieldPriority] as int? ?? 3,
      fieldNotes: json[fieldNotes] as String? ?? "",
    };
  }
}




// TODO: get rid of this
// enum ItemType { Shopping, Chore }

// abstract class TodoItem extends Equatable {
//   final String id;
//   final String name;
//   final String addedBy;
//   final bool completed;
//   // Not required
//   String neededBy;
//   String volunteer;
//   int priority;
//   String notes;

//   final ItemType itemType; // TODO: probably remove this

//   TodoItem({
//     required this.id,
//     required this.name,
//     required this.addedBy,
//     required this.completed,
//     required this.neededBy,
//     required this.volunteer,
//     required this.priority,
//     required this.notes,
//     required this.itemType,
//   });

//   Map<String, dynamic> toJson() {
//     return {
//       'id': id,
//       'name': name,
//       'addedBy': addedBy,
//       'completed': completed,
//       'neededBy': neededBy,
//       'volunteer': volunteer,
//       'priority': priority,
//       'notes': notes,
//       // TODO: what does tgos do?
//       'itemType': itemType.toString().split('.').last,
//     };
//   }

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
//         itemType,
//       ];
// }
