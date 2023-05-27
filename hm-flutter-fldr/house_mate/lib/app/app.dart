import 'package:flutter/material.dart';

import '../pages/home/view/home_page.dart';

class App extends StatelessWidget {
  const App({super.key, required this.todosRepository});

  @override
  Widget build(BuildContext context) {
    // TODO: maybe remove this
    // return RepositoryProvider.value(
    //   value: todosRepository,
    //   child: const AppView(),
    // );
    return const AppView();
  }
}

class AppView extends StatelessWidget {
  const AppView({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ListsTheme.light,
      darkTheme: ListsTheme.dark,
      home: const HomePage(),
    );
  }
}
