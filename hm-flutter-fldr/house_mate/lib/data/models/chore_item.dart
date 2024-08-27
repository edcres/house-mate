import 'package:house_mate/data/models/todo_item.dart';

class ChoreItem extends TodoItem {
  static const String fieldDifficulty = 'difficulty';

  final int difficulty;

  ChoreItem({
    required String name,
    this.difficulty = 1,
  }) : super(
          name: name,
          itemType: ItemType.Chore,
        );

  ChoreItem.fromJson(Map<String, dynamic> json)
      : difficulty = (json[fieldDifficulty] as num?)?.toInt() ?? 1,
        super(
          id: json[TodoItem.fieldId] as String,
          name: json[TodoItem.fieldName] as String,
          addedBy: json[TodoItem.fieldAddedBy] as String,
          itemType: ItemType.Chore,
        ) {
    // Set the common fields using the parent class method
    TodoItem.setCommonFields(this, json);
  }

  @override
  List<Object?> get props => super.props..add(difficulty);

  @override
  Map<String, dynamic> toJson() {
    final json = super.toJson();
    json.addAll({
      fieldDifficulty: difficulty,
    });
    return json;
  }
}
