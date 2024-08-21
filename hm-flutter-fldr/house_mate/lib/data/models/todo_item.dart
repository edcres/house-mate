import 'package:equatable/equatable.dart';

enum ItemType { Shopping, Chore }

abstract class TodoItem extends Equatable {
  final String id;
  final String name;
  final String addedBy;
  final bool completed;
  // Not required
  String neededBy;
  String volunteer;
  int priority;
  String notes;

  final ItemType itemType; // TODO: probably remove this

  TodoItem({
    required this.id,
    required this.name,
    required this.addedBy,
    required this.completed,
    required this.neededBy,
    required this.volunteer,
    required this.priority,
    required this.notes,
    required this.itemType,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'addedBy': addedBy,
      'completed': completed,
      'neededBy': neededBy,
      'volunteer': volunteer,
      'priority': priority,
      'notes': notes,
      // TODO: what does tgos do?
      'itemType': itemType.toString().split('.').last,
    };
  }

  @override
  List<Object> get props => [
        id,
        name,
        addedBy,
        completed,
        neededBy,
        volunteer,
        priority,
        notes,
        itemType,
      ];
}
