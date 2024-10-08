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

  String id; // TODO: This field is not used
  final String name;
  String addedBy;
  bool completed;
  String neededBy;
  String volunteer;
  int priority;
  String notes;
  final ItemType itemType; // Only used locally

  TodoItem({
    this.id = "0",
    required this.name,
    this.addedBy = "",
    this.completed = false,
    this.neededBy = "",
    this.volunteer = "",
    this.priority = 3,
    this.notes = "",
    required this.itemType,
  });

  Map<String, dynamic> toJson() {
    return {
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

  @override
  List<Object?> get props => [
        name,
        addedBy,
        completed,
        neededBy,
        volunteer,
        priority,
        notes,
      ];

  // Common deserialization logic for shared fields
  static void setCommonFields(TodoItem item, Map<String, dynamic> json) {
    item.completed = json[fieldCompleted] as bool? ?? false;
    item.neededBy = json[fieldNeededBy] as String? ?? "";
    item.volunteer = json[fieldVolunteer] as String? ?? "";
    item.priority = json[fieldPriority] as int? ?? 3;
    item.notes = json[fieldNotes] as String? ?? "";
  }
}
