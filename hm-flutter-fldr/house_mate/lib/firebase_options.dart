// File generated by FlutterFire CLI.
// ignore_for_file: lines_longer_than_80_chars, avoid_classes_with_only_static_members
import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

/// Default [FirebaseOptions] for use with your Firebase apps.
///
/// Example:
/// ```dart
/// import 'firebase_options.dart';
/// // ...
/// await Firebase.initializeApp(
///   options: DefaultFirebaseOptions.currentPlatform,
/// );
/// ```
class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      case TargetPlatform.macOS:
        return macos;
      case TargetPlatform.windows:
        return windows;
      case TargetPlatform.linux:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for linux - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      default:
        throw UnsupportedError(
          'DefaultFirebaseOptions are not supported for this platform.',
        );
    }
  }

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyApwHE0bWTkKAPLphMs4jMlOhC_aGvIJOI',
    appId: '1:795882824191:web:6fe1002f52e69c96e847ca',
    messagingSenderId: '795882824191',
    projectId: 'house-mate-3b2d2',
    authDomain: 'house-mate-3b2d2.firebaseapp.com',
    storageBucket: 'house-mate-3b2d2.appspot.com',
    measurementId: 'G-5S25H1MQK6',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyAUhvEs6PiDeRsukZq0vZ-Se7dU5OX_2Qo',
    appId: '1:795882824191:android:eaab2cea0327a132e847ca',
    messagingSenderId: '795882824191',
    projectId: 'house-mate-3b2d2',
    storageBucket: 'house-mate-3b2d2.appspot.com',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'AIzaSyAOKA4qVZMBIBS9TJaNrDKgY0RNWb6vqYI',
    appId: '1:795882824191:ios:dc12d1cd40d058f6e847ca',
    messagingSenderId: '795882824191',
    projectId: 'house-mate-3b2d2',
    storageBucket: 'house-mate-3b2d2.appspot.com',
    iosClientId: '795882824191-9q690ul7p55ci21sba72sibj77badas5.apps.googleusercontent.com',
    iosBundleId: 'com.example.houseMate',
  );

  static const FirebaseOptions macos = FirebaseOptions(
    apiKey: 'AIzaSyAOKA4qVZMBIBS9TJaNrDKgY0RNWb6vqYI',
    appId: '1:795882824191:ios:dc12d1cd40d058f6e847ca',
    messagingSenderId: '795882824191',
    projectId: 'house-mate-3b2d2',
    storageBucket: 'house-mate-3b2d2.appspot.com',
    iosClientId: '795882824191-9q690ul7p55ci21sba72sibj77badas5.apps.googleusercontent.com',
    iosBundleId: 'com.example.houseMate',
  );

  static const FirebaseOptions windows = FirebaseOptions(
    apiKey: 'AIzaSyApwHE0bWTkKAPLphMs4jMlOhC_aGvIJOI',
    appId: '1:795882824191:web:fab96368c1c5b354e847ca',
    messagingSenderId: '795882824191',
    projectId: 'house-mate-3b2d2',
    authDomain: 'house-mate-3b2d2.firebaseapp.com',
    storageBucket: 'house-mate-3b2d2.appspot.com',
    measurementId: 'G-DY0QCZMPGL',
  );

}