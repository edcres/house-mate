import 'package:house_mate/data/models/todo_item.dart';

class ChoreItem extends TodoItem {
  static const String fieldDifficulty = 'difficulty';

  final double difficulty;

  const ChoreItem({
    required String id,
    required String name,
    required String addedBy,
    required bool completed,
    String neededBy = "",
    String volunteer = "",
    int priority = 3,
    String notes = "",
    this.difficulty = 1.0,
  }) : super(
          id: id,
          name: name,
          addedBy: addedBy,
          completed: completed,
          neededBy: neededBy,
          volunteer: volunteer,
          priority: priority,
          notes: notes,
          itemType: ItemType.Chore,
        );

  @override
  List<Object?> get props => super.props..add(difficulty);

  factory ChoreItem.fromJson(Map<String, dynamic> json) {
    final commonFields = TodoItem.commonFromJson(json);
    return ChoreItem(
      id: commonFields[TodoItem.fieldId],
      name: commonFields[TodoItem.fieldName],
      addedBy: commonFields[TodoItem.fieldAddedBy],
      completed: commonFields[TodoItem.fieldCompleted],
      neededBy: commonFields[TodoItem.fieldNeededBy],
      volunteer: commonFields[TodoItem.fieldVolunteer],
      priority: commonFields[TodoItem.fieldPriority],
      notes: commonFields[TodoItem.fieldNotes],
      difficulty: (json[fieldDifficulty] as num?)?.toDouble() ?? 1.0,
    );
  }

  @override
  Map<String, dynamic> toJson() {
    final json = super.toJson();
    json.addAll({
      fieldDifficulty: difficulty,
    });
    return json;
  }
}
