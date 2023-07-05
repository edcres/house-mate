part of 'edit_item_bloc.dart';

abstract class EditItemEvent extends Equatable {
  const EditItemEvent();

  @override
  List<Object> get props => [];
}

class EditItemTitleChanged extends EditItemEvent {
  const EditItemTitleChanged(this.title);

  final String title;

  @override
  List<Object> get props => [title];
}

class EditItemDescriptionChanged extends EditItemEvent {
  const EditItemDescriptionChanged(this.description);

  final String description;

  @override
  List<Object> get props => [description];
}

class EditItemSubmitted extends EditItemEvent {
  const EditItemSubmitted();
}
