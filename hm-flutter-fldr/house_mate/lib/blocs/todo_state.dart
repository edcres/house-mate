import 'package:equatable/equatable.dart';
import 'package:house_mate/data/models/todo_item.dart';

class TodoState extends Equatable {
  final List<TodoItem> items;
  final bool isEditMode;
  final bool groupIdExists;
  final String? groupId;
  final String? userId;
  final String? userName;

  TodoState(
      {required this.items,
      this.isEditMode = false,
      this.groupIdExists = false,
      this.groupId,
      this.userId,
      this.userName});

  TodoState copyWith({
    List<TodoItem>? items,
    bool? isEditMode,
    bool? groupIdExists,
    String? groupId,
    String? userId,
    String? userName,
  }) {
    final newState = TodoState(
      items: items ?? this.items,
      isEditMode: isEditMode ?? this.isEditMode,
      groupIdExists: groupIdExists ?? this.groupIdExists,
      groupId: groupId ?? this.groupId,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
    );
    return newState;
  }

  @override
  List<Object?> get props =>
      [items, isEditMode, groupIdExists, groupId, userId, userName];
}
