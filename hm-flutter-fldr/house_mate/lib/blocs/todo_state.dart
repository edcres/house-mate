import 'package:equatable/equatable.dart';
import 'package:house_mate/data/models/todo.dart';

class TodoState extends Equatable {
  final List<Todo> items;
  final bool isEditMode;

  TodoState({required this.items, this.isEditMode = false});

  TodoState copyWith({List<Todo>? items, bool? isEditMode}) {
    return TodoState(
      items: items ?? this.items,
      isEditMode: isEditMode ?? this.isEditMode,
    );
  }

  @override
  List<Object> get props => [items, isEditMode];
}
