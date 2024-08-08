import 'package:equatable/equatable.dart';
import 'package:house_mate/data/models/todo_item.dart';

class TodoState extends Equatable {
  final List<TodoItem> items;
  final bool isEditMode;
  final bool groupIdExists;
  final String? newGroupId;
  final String? userId;

  TodoState(
      {required this.items,
      this.isEditMode = false,
      this.groupIdExists = false,
      this.newGroupId,
      this.userId});

  TodoState copyWith({
    List<TodoItem>? items,
    bool? isEditMode,
    bool? groupIdExists,
    String? newGroupId,
    String? userId,
  }) {
    return TodoState(
      items: items ?? this.items,
      isEditMode: isEditMode ?? this.isEditMode,
      groupIdExists: groupIdExists ?? this.groupIdExists,
      newGroupId: newGroupId ?? this.newGroupId,
    );
  }

  @override
  List<Object> get props => [items, isEditMode];
}
