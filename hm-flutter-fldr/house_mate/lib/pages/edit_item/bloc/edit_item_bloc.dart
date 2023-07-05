import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:equatable/equatable.dart';

part 'edit_item_event.dart';
part 'edit_item_state.dart';

class EditItemBloc extends Bloc<EditItemEvent, EditItemState> {
  EditItemBloc({
    required ItemRepository itemsRepository,
    required ListItem? initialItem,
  })  : _itemRepository = itemsRepository,
        super(
          EditItemState(
            initialItem: initialItem,
            title: initialItem?.title ?? '',
            description: initialItem?.description ?? '',
          ),
        ) {
    on<EditItemTitleChanged>(_onTitleChanged);
    on<EditItemDescriptionChanged>(_onDescriptionChanged);
    on<EditItemSubmitted>(_onSubmitted);
  }

  final ItemsRepository _itemRepository;

  void _onTitleChanged(
    EditItemTitleChanged event,
    Emitter<EditItemState> emit,
  ) {
    emit(state.copyWith(title: event.title));
  }

  void _onDescriptionChanged(
    EditItemDescriptionChanged event,
    Emitter<EditItemState> emit,
  ) {
    emit(state.copyWith(description: event.description));
  }

  Future<void> _onSubmitted(
    EditItemSubmitted event,
    Emitter<EditItemState> emit,
  ) async {
    emit(state.copyWith(status: EditItemStatus.loading));
    final listItem = (state.initialItem ?? ListItem(title: '')).copyWith(
      title: state.title,
      description: state.description,
    );

    try {
      await _itemRepository.saveItem(listItem);
      emit(state.copyWith(status: EditItemStatus.success));
    } catch (e) {
      emit(state.copyWith(status: EditItemStatus.failure));
    }
  }
}
