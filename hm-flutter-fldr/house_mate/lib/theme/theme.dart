import 'package:flutter/material.dart';
import 'package:house_mate/theme/colors.dart';

class AppTheme {
  static const colors = AppColors();

  const AppTheme._();

  static ThemeData get light {
    return ThemeData(
      brightness: Brightness.light,
      primaryColor: const Color(0xFF14DD7B),
      primaryColorDark: const Color(0xFF00542C),
      fontFamily: 'Georgia',
      scaffoldBackgroundColor: const Color(0xFFEAEA33),
      buttonTheme: ButtonThemeData(
        // 4
        shape:
            RoundedRectangleBorder(borderRadius: BorderRadius.circular(18.0)),
        buttonColor: Colors.purple,
      ),
      appBarTheme: const AppBarTheme(
        color: Color(0xFF13B9FF),
      ),
      colorScheme: ColorScheme.fromSwatch(
        accentColor: const Color(0xFF13B9FF),
      ),
      snackBarTheme: const SnackBarThemeData(
        behavior: SnackBarBehavior.floating,
      ),
    );
  }

  static ThemeData get dark {
    return ThemeData(
      appBarTheme: const AppBarTheme(
        color: Color(0xFF13B9FF),
      ),
      colorScheme: ColorScheme.fromSwatch(
        brightness: Brightness.dark,
        accentColor: const Color(0xFF13B9FF),
      ),
      snackBarTheme: const SnackBarThemeData(
        behavior: SnackBarBehavior.floating,
      ),
    );
  }
}
