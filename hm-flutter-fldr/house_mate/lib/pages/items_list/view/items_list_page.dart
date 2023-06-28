import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../edit_item/view/edit_item_page.dart';
import '../bloc/items_list_bloc.dart';

class ItemsListPage extends StatelessWidget {
  const ItemsListPage({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => ItemsListBloc(
        listItemsRepository: context.read<ListItemsRepository>(),
      )..add(const ItemsListSubscriptionRequested()),
      child: const ItemsListView(),
    );
  }
}

class ItemsListView extends StatelessWidget {
  const ItemsListView({super.key});

  @override
  Widget build(BuildContext context) {
    // Languages
    final l10n = context.l10n;

    return Scaffold(
      appBar: AppBar(
        title: Text(l10n.ItemsListAppBarTitle),
        actions: const [
          ItemsListFilterButton(),
          ItemsListOptionsButton(),
        ],
      ),
      body: MultiBlocListener(
        listeners: [
          BlocListener<ItemsListBloc, ItemsListState>(
            listenWhen: (previous, current) =>
                previous.status != current.status,
            listener: (context, state) {
              if (state.status == ItemsListStatus.failure) {
                ScaffoldMessenger.of(context)
                  ..hideCurrentSnackBar()
                  ..showSnackBar(
                    SnackBar(
                      content: Text(l10n.ItemsListErrorSnackbarText),
                    ),
                  );
              }
            },
          ),
          BlocListener<ItemsListBloc, ItemsListState>(
            listenWhen: (previous, current) =>
                previous.lastDeletedItem != current.lastDeletedItem &&
                current.lastDeletedItem != null,
            listener: (context, state) {
              final deletedItem = state.lastDeletedItem!;
              final messenger = ScaffoldMessenger.of(context);
              messenger
                ..hideCurrentSnackBar()
                ..showSnackBar(
                  SnackBar(
                    content: Text(
                      l10n.itemsListItemDeletedSnackbarText(
                        deletedItem.title,
                      ),
                    ),
                    action: SnackBarAction(
                      label: l10n.itemsListUndoDeletionButtonText,
                      onPressed: () {
                        messenger.hideCurrentSnackBar();
                        context
                            .read<ItemsListBloc>()
                            .add(const ItemsListUndoDeletionRequested());
                      },
                    ),
                  ),
                );
            },
          ),
        ],
        child: BlocBuilder<ItemsListBloc, ItemsListState>(
          builder: (context, state) {
            if (state.listItems.isEmpty) {
              if (state.status == ItemsListStatus.loading) {
                return const Center(child: CupertinoActivityIndicator());
              } else if (state.status != ItemsListStatus.success) {
                return const SizedBox();
              } else {
                return Center(
                  child: Text(
                    l10n.itemsListEmptyText,
                    style: Theme.of(context).textTheme.bodySmall,
                  ),
                );
              }
            }

            return CupertinoScrollbar(
              child: ListView(
                children: [
                  for (final item in state.filteredItems)
                    ItemsListTile(
                      listItem: item,
                      onToggleCompleted: (isCompleted) {
                        context.read<ItemsListBloc>().add(
                              ItemsListItemCompletionToggled(
                                listItem: item,
                                isCompleted: isCompleted,
                              ),
                            );
                      },
                      onDismissed: (_) {
                        context
                            .read<ItemsListBloc>()
                            .add(ItemsListItemDeleted(item));
                      },
                      onTap: () {
                        Navigator.of(context).push(
                          EditItemPage.route(initialItem: item),
                        );
                      },
                    ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }
}
