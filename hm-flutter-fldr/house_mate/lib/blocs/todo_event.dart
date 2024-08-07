import 'package:equatable/equatable.dart';
import 'package:house_mate/data/models/todo_item.dart';

abstract class TodoEvent extends Equatable {
  const TodoEvent();

  @override
  List<Object> get props => [];
}

class LoadItems extends TodoEvent {
  final String groupId;

  const LoadItems(this.groupId);

  @override
  List<Object> get props => [groupId];
}

class AddItem extends TodoEvent {
  final String item;
  final ItemType itemType;

  AddItem(this.item, this.itemType);

  @override
  List<Object> get props => [item, itemType];
}

class ToggleItem extends TodoEvent {
  final String id;
  final ItemType itemType;

  ToggleItem(this.id, this.itemType);

  @override
  List<Object> get props => [id, itemType];
}

class UpdateItem extends TodoEvent {
  final String id;
  final String updatedTask;
  final ItemType itemType;

  UpdateItem(this.id, this.updatedTask, this.itemType);

  @override
  List<Object> get props => [id, updatedTask, itemType];
}

class DeleteItem extends TodoEvent {
  final String id;
  final ItemType itemType;

  DeleteItem(this.id, this.itemType);

  @override
  List<Object> get props => [id, itemType];
}

class EnterEditMode extends TodoEvent {}

class ExitEditMode extends TodoEvent {}

class CreateGroup extends TodoEvent {
  final String groupId;

  CreateGroup(this.groupId);

  @override
  List<Object> get props => [groupId];
}

// TODO: maybe delete this. Made it when adding checkGroupIdExists and createGroup to the tabs screen
class CheckGroupIdExists extends TodoEvent {
  final String groupId;
  CheckGroupIdExists(this.groupId);
}
