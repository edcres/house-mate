import 'todo_item.dart';

class ChoreItem extends TodoItem {
  final double difficulty;

  ChoreItem(
      {required String id,
      required String name,
      required String addedBy,
      required bool completed,
      // Not required
      String neededBy = "",
      String volunteer = "",
      int priority = 3,
      String notes = "",
      // Only for Chores
      this.difficulty = 1})
      : super(
          id: id,
          name: name,
          addedBy: addedBy,
          completed: completed,
          neededBy: neededBy,
          volunteer: volunteer,
          priority: priority,
          notes: notes,
          itemType: ItemType.Chore, // TODO: get rid of this
        );

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
        difficulty,
      ];

  factory ChoreItem.fromJson(Map<String, dynamic> json) {
    return ChoreItem(
      id: json['id'] as String,
      name: json['name'] as String,
      addedBy: json['addedBy'] as String,
      completed: json['completed'] as bool,
      neededBy: json['neededBy'] as String? ?? "",
      volunteer: json['volunteer'] as String? ?? "",
      priority: json['priority'] as int? ?? 3,
      notes: json['notes'] as String? ?? "",
      difficulty: (json['difficulty'] as num?)?.toDouble() ?? 1,
    );
  }

  Map<String, dynamic> toJson() {
    final json = super.toJson();
    json.addAll({
      'difficulty': difficulty,
    });
    return json;
  }
}
