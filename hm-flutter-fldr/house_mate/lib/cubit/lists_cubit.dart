import 'package:flutter_bloc/flutter_bloc.dart';

import 'lists_state.dart';

class ListsCubit extends Cubit<ListsState> {
  ListsCubit() : super(const ListsState());

  // TODO: change this
  // void setTab(HomeTab tab) => emit(HomeState(tab: tab));
}
