import 'package:equatable/equatable.dart';
import 'package:house_mate/data/models/todo_item.dart';

class TodoState extends Equatable {
  final List<TodoItem> items;
  final bool isEditMode;
  final bool groupIdExists;
  final String? groupId;
  final String? userId;

  TodoState(
      {required this.items,
      this.isEditMode = false,
      this.groupIdExists = false,
      this.groupId,
      this.userId});

  TodoState copyWith({
    List<TodoItem>? items,
    bool? isEditMode,
    bool? groupIdExists,
    String? groupId,
    String? userId,
  }) {
    return TodoState(
      items: items ?? this.items,
      isEditMode: isEditMode ?? this.isEditMode,
      groupIdExists: groupIdExists ?? this.groupIdExists,
      groupId: groupId ?? this.groupId,
      userId: userId ?? this.userId,
    );
  }

  @override
  List<Object> get props => [items, isEditMode];
}
