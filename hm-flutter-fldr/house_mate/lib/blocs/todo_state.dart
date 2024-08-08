import 'package:equatable/equatable.dart';
import 'package:house_mate/data/models/todo_item.dart';

class TodoState extends Equatable {
  final List<TodoItem> items;
  final bool isEditMode;
  final bool groupIdExists;
  final String? newGroupId;

  TodoState(
      {required this.items,
      this.isEditMode = false,
      this.groupIdExists = false,
      this.newGroupId});

  TodoState copyWith({
    List<TodoItem>? items,
    bool? isEditMode,
    bool? groupIdExists,
  }) {
    return TodoState(
      items: items ?? this.items,
      isEditMode: isEditMode ?? this.isEditMode,
      groupIdExists: groupIdExists ?? this.groupIdExists,
    );
  }

  @override
  List<Object> get props => [items, isEditMode];
}
