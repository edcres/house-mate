import 'dart:math';

class Helper {
  // Group ID
  final String DEFAULT_ID = "00000000asdfg";
  final String GROUP_ID_SP = "group_id";
  final String USER_ID_SP = "user_id";
  final String USER_NAME_SP = "user_name";
  final String PAST_GROUPS = "past_groups";
  final String NULL_STRING = "null";
  final String GROUP_ID_SP_SEPARATOR = ",";

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

  // Format id with dashes
  String dashId(String? id) {
    if (id != null) {
      // Split the ID into numeric and letter parts
      final RegExp idPattern = RegExp(r'(\d+)([a-zA-Z]+)');
      final match = idPattern.firstMatch(id);
      if (match == null) {
        throw ArgumentError('ID format is incorrect.');
      }
      String numericPart = match.group(1)!;
      String letterPart = match.group(2)!;
      // Reverse the numeric part for easier insertion of dashes every 3 characters
      String reversedNumeric = numericPart.split('').reversed.join('');
      // Insert dashes every 3 characters, but avoid adding a dash at the end if only 3 digits
      String formattedReversedNumeric = reversedNumeric.replaceAllMapped(
        RegExp(r'.{3}'),
        (match) => '${match.group(0)}-',
      );
      // Reverse the numeric part back and remove any trailing dash
      String formattedNumeric =
          formattedReversedNumeric.split('').reversed.join('');
      if (formattedNumeric.startsWith('-')) {
        formattedNumeric = formattedNumeric.substring(1);
      }
      // Combine the numeric part with the letter part
      return '$formattedNumeric-$letterPart';
    } else {
      return "null id";
    }
  }

  List addIdToSPList(String newItem, String? pastGroups) {
    // Remove an existing item if any, then add new one
    if (pastGroups == null) {
      List<String> newList = [newItem];
      return newList;
    } else {
      List pastGroupsList = pastGroups.split(GROUP_ID_SP_SEPARATOR);
      if (pastGroupsList.contains(newItem)) {
        pastGroupsList.remove(newItem);
      } else if (pastGroupsList.length >= 5) {
        pastGroupsList.removeAt(0);
      }
      pastGroupsList.add(newItem);
      return pastGroupsList;
    }
  }

  List<String> pastGroupsToList(String pastGroups) {
    if (pastGroups == NULL_STRING) {
      List<String> emptyList = ['No Past Groups'];
      return emptyList;
    } else {
      return pastGroups.split(GROUP_ID_SP_SEPARATOR);
    }
  }
}
