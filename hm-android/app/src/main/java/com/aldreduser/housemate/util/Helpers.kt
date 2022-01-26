package com.aldreduser.housemate.util

import android.content.Context
import com.aldreduser.housemate.R
import com.aldreduser.housemate.ui.main.activities.MainActivity

fun displayDate(date: String): String {
    return "by $date"
}

fun displayCost(cost: Double): String {
    return "price: $${cost.toString()}"
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
    return "by $addedBy"
}

fun necessaryAreFilled(name: String, qty: String): Boolean {
    return name.isNotEmpty() && qty.isNotEmpty()
}

fun add1AndScrambleLetters(oldID: String): String {
    val lettersToScramble = "asdfg"
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