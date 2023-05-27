import 'package:flutter/material.dart';

import '../pages/home/view/home_page.dart';

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
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
