import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/pages/items_list/items_list.dart';

@visibleForTesting
enum ItemsListOption { toggleAll, clearCompleted }

class ItemsListOptionsButton extends StatelessWidget {
  const ItemsListOptionsButton({super.key});

  @override
  Widget build(BuildContext context) {
    final l10n = context.l10n;

    final listItems =
        context.select((ItemsListBloc bloc) => bloc.state.listItems);
    final hasItems = listItems.isNotEmpty;
    final completedItemsAmount =
        listItems.where((item) => item.isCompleted).length;

    return PopupMenuButton<ItemsListOption>(
      shape: const ContinuousRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(16)),
      ),
      tooltip: l10n.itemsListOptionsTooltip,
      onSelected: (options) {
        switch (options) {
          case ItemsListOption.toggleAll:
            context
                .read<ItemsListBloc>()
                .add(const ItemsListToggleAllRequested());
            break;
          case ItemsListOption.clearCompleted:
            context
                .read<ItemsListBloc>()
                .add(const ItemsListClearCompletedRequested());
        }
      },
      itemBuilder: (context) {
        return [
          PopupMenuItem(
            value: ItemsListOption.toggleAll,
            enabled: hasItems,
            child: Text(
              completedItemsAmount == listItems.length
                  ? l10n.itemsListOptionsMarkAllIncomplete
                  : l10n.itemsListOptionsMarkAllComplete,
            ),
          ),
          PopupMenuItem(
            value: ItemsListOption.clearCompleted,
            enabled: hasItems && completedItemsAmount > 0,
            child: Text(l10n.itemsListOptionsClearCompleted),
          ),
        ];
      },
      icon: const Icon(Icons.more_vert_rounded),
    );
  }
}
