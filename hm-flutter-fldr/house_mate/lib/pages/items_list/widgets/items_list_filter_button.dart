import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:house_mate/pages/items_list/items_list.dart';

class ItemsListFilterButton extends StatelessWidget {
  const ItemsListFilterButton({super.key});

  @override
  Widget build(BuildContext context) {
    final l10n = context.l10n;

    final activeFilter =
        context.select((ItemsListBloc bloc) => bloc.state.filter);

    return PopupMenuButton<ItemsViewFilter>(
      shape: const ContinuousRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(16)),
      ),
      initialValue: activeFilter,
      tooltip: l10n.itemsListFilterTooltip,
      onSelected: (filter) {
        context.read<ItemsListBloc>().add(ItemsListFilterChanged(filter));
      },
      itemBuilder: (context) {
        return [
          PopupMenuItem(
            value: ItemsViewFilter.all,
            child: Text(l10n.itemsListFilterAll),
          ),
          PopupMenuItem(
            value: ItemsViewFilter.activeOnly,
            child: Text(l10n.itemsListFilterActiveOnly),
          ),
          PopupMenuItem(
            value: ItemsViewFilter.completedOnly,
            child: Text(l10n.itemsListFilterCompletedOnly),
          ),
        ];
      },
      icon: const Icon(Icons.filter_list_rounded),
    );
  }
}
