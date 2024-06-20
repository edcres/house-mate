import 'package:equatable/equatable.dart';

enum ItemType { Shopping, Chore }

abstract class Todo extends Equatable {
  final String id;
  final String task;
  final bool isCompleted;
  final ItemType itemType;

  Todo({
    required this.id,
    required this.task,
    required this.isCompleted,
    required this.itemType,
  });

  @override
  List<Object> get props => [id, task, isCompleted, itemType];
}
