import 'package:house_mate/data/models/todo_item.dart';

class ChoreItem extends TodoItem {
  static const String fieldDifficulty = 'difficulty';

  final int difficulty;

  ChoreItem({
    required String name,
    required String addedBy,
    this.difficulty = 1,
  }) : super(
          name: name,
          addedBy: addedBy,
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


// class ChoreItem extends TodoItem {
//   static const String fieldDifficulty = 'difficulty';

//   final double difficulty;

//   ChoreItem({
//     required String id,
//     required String name,
//     required String addedBy,
//     this.difficulty = 1.0,
//   }) : super(
//           id: id,
//           name: name,
//           addedBy: addedBy,
//           itemType: ItemType.Chore,
//         );

//   @override
//   List<Object?> get props => super.props..add(difficulty);

//   factory ChoreItem.fromJson(Map<String, dynamic> json) {
//     final commonFields = TodoItem.commonFromJson(json);
//     return ChoreItem(
//       id: commonFields[TodoItem.fieldId],
//       name: commonFields[TodoItem.fieldName],
//       addedBy: commonFields[TodoItem.fieldAddedBy],
//       // completed: commonFields[TodoItem.fieldCompleted],
//       // neededBy: commonFields[TodoItem.fieldNeededBy],
//       // volunteer: commonFields[TodoItem.fieldVolunteer],
//       // priority: commonFields[TodoItem.fieldPriority],
//       // notes: commonFields[TodoItem.fieldNotes],
//       difficulty: (json[fieldDifficulty] as num?)?.toDouble() ?? 1.0,
//     );
//   }

//   @override
//   Map<String, dynamic> toJson() {
//     final json = super.toJson();
//     json.addAll({
//       fieldDifficulty: difficulty,
//     });
//     return json;
//   }
// }
