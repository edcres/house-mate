import 'dart:math';

class Helper {
  // Group ID
  String generateNewID(String pastId) {
    final int prefix = int.parse(pastId.substring(0, 8));
    final int newPrefix = prefix + 1;
    const letters = 'asdfglkjh';
    final random = Random();
    final String suffix =
        List.generate(5, (index) => letters[random.nextInt(letters.length)])
            .join();
    final String newGroupId = newPrefix.toString().padLeft(8, '0') + suffix;
    return newGroupId;
  }
}
