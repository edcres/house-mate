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
  final TodoItem item;
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

// class UpdateItem extends TodoEvent {
//   final String id;
//   final String updatedTask;
//   final ItemType itemType;

//   UpdateItem(this.id, this.updatedTask, this.itemType);

//   @override
//   List<Object> get props => [id, updatedTask, itemType];
// }
class UpdateItem extends TodoEvent {
  final TodoItem updatedItem;
  final ItemType itemType;

  UpdateItem(this.updatedItem, this.itemType);

  @override
  List<Object> get props => [updatedItem, itemType];
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

class SetGroupId extends TodoEvent {
  // Locally
  final String groupId;

  SetGroupId(this.groupId);

  @override
  List<Object> get props => [groupId];
}

class CreateGroup extends TodoEvent {}

class CheckGroupIdExistsAndJoin extends TodoEvent {
  final String groupId;
  CheckGroupIdExistsAndJoin(this.groupId);
}

class JoinGroup extends TodoEvent {
  final String groupId;
  JoinGroup(this.groupId);
}

class SetUserName extends TodoEvent {
  // Locally
  final String userName;

  SetUserName(this.userName);

  @override
  List<Object> get props => [userName];
}
