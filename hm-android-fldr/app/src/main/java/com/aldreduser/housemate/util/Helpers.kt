package com.aldreduser.housemate.util

import android.content.Context
import android.util.Log
import android.widget.Toast

const val SHOPPING_ITEM = "Shopping"
const val CHORE_ITEM = "Chore"

enum class ListType {
    SHOPPING, CHORES
}

fun displayDate(date: String): String {
    return "needed by $date"
}

fun displayCost(cost: Double): String {
    return "price: $$cost"
}

fun displayPriority(priority: Int): String {
    var priorityString = ""
    when (priority) {
        1 -> priorityString = "Urgent"
        2 -> priorityString = "Not Urgent"
        3 -> priorityString = "Needed Eventually"
    }
    return priorityString
}

fun displayDifficulty(difficulty: Int): String {
    var difficultyString = ""
    when (difficulty) {
        1 -> difficultyString = "Simple"
        2 -> difficultyString = "Medium Challenge"
        3 -> difficultyString = "Challenging"
    }
    return difficultyString
}

fun displayAddedBy(addedBy: String): String {
    return "added by $addedBy"
}

fun necessaryAreFilled(name: String, qty: String): Boolean {
    Log.d("rndTAG", "name: $name, empty: ${name.isEmpty()}")
    Log.d("rndTAG", "qty: $qty, empty: ${qty.isEmpty()}")
    return (name.isNotEmpty() && qty.isNotEmpty())
}

fun presentItemQty(qty: Double): String {
    return if (getLastTwoDigits(qty.toString()) == ".0") {
        if (qty.toInt().toString() == "0"){
            ""
        } else {
            qty.toInt().toString()
        }
    } else {
        "%.2f".format(qty)
    }
}

fun getLastTwoDigits(theString: String): String {
    val y = theString[theString.length - 2]
    val z = theString.last()
    return "$y$z"
}

fun validateGroupId(string: String) = if (string.length == 13) { isFirstNDigits(8, string) &&
        isLastNLetters(5, string)
} else false

private fun isFirstNDigits(n: Int, string: String): Boolean {
    var firstNIsLetters = true
    for (i in 0 until n) {
        try {
            string[i].toString().toInt()
        } catch (e: NumberFormatException) {
            firstNIsLetters = false
        }
    }
    return firstNIsLetters
}

private fun isLastNLetters(n: Int, string: String): Boolean {
    var lastNIsDigits = true
    val nBeforeTheLast = string.length - n
    for (i in nBeforeTheLast until string.length) {
        if (!string[i].isLetter()) lastNIsDigits = false
    }
    return lastNIsDigits
}

fun add1AndScrambleLetters(oldID: String): String {
    val lettersToScramble = "asdfglkjh"
    val newID: String
    var scrambledLetters = ""
    // Add random letters to the String
    for (i in 1..5) {
        scrambledLetters = "$scrambledLetters${lettersToScramble.random()}"
    }
    // Get the fist 8 digits of 'oldID'
    var oldID8Digits = ""
    for (i in 0..7) {
        oldID8Digits = "$oldID8Digits${oldID[i]}"
    }
    var idPosition = oldID8Digits.toInt()
    // turn to int and add 1 and make it 8 characters (by filing the first characters with 0s)
    idPosition++
    var idPositionString = idPosition.toString()
    while (idPositionString.length < 8) {
        idPositionString = "0$idPositionString"
    }
    newID = "$idPositionString$scrambledLetters"
    return newID
}

fun displayToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}