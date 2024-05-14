package com.aldreduser.housemate.util

import android.content.Context
import android.widget.Toast

const val SHOPPING_ITEM = "Shopping"
const val CHORE_ITEM = "Chore"

enum class ListType { SHOPPING, CHORES }

fun displayDate(date: String) = "needed by $date"

fun displayCost(cost: Double) = "price: $$cost"

fun displayPriority(priority: Int) = when (priority) {
    1 -> "Urgent"
    2 -> "Not Urgent"
    3 -> "Needed Eventually"
    else -> ""
}

fun displayDifficulty(difficulty: Int) = when (difficulty) {
    1 -> "Simple"
    2 -> "Medium Challenge"
    3 -> "Challenging"
    else -> ""
}

fun displayAddedBy(addedBy: String) = "added by $addedBy"

fun necessaryAreFilled(name: String, qty: String): Boolean = (name.isNotEmpty() && qty.isNotEmpty())

fun presentItemQty(qty: Double) =
    if (getLastTwoDigits(qty.toString()) == ".0") {
        if (qty.toInt().toString() == "0") "" else qty.toInt().toString()
    } else "%.2f".format(qty)

fun getLastTwoDigits(theString: String): String {
    val y = theString[theString.length - 2]
    val z = theString.last()
    return "$y$z"
}

fun validateGroupId(string: String) = if (string.length == 13) {
    isFirstNDigits(8, string) && isLastNLetters(5, string)
} else false

private fun isFirstNDigits(n: Int, string: String): Boolean {
    // Checks if the first n digits are numbers in a string.
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
    // Checks if the last n are letters in a string.
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
    for (i in 1..5) scrambledLetters = "$scrambledLetters${lettersToScramble.random()}"
    // Get the fist 8 digits of 'oldID'
    var oldID8Digits = ""
    for (i in 0..7) oldID8Digits = "$oldID8Digits${oldID[i]}"
    var idPosition = oldID8Digits.toInt()
    // turn to int and add 1 and make it 8 characters (by filing the first characters with 0s)
    idPosition++
    var idPositionString = idPosition.toString()
    while (idPositionString.length < 8) idPositionString = "0$idPositionString"
    newID = "$idPositionString$scrambledLetters"
    return newID
}

fun commaPastOrders(rawOrders: List<String>): List<String> {
    val commaOrders = mutableListOf<String>()
    for (i in rawOrders) commaOrders.add(addCommasToOrder(i))
    return commaOrders
}

fun addCommasToOrder(order: String?): String {
    var newOrderReversed = ""
    var digitsStarted = false
    if (order != null) {
        var counter = 0
        for (i in order.length - 1 downTo 0) {
            newOrderReversed += order[i]
            // Find the spot between letter and number
            // TODO: 'if(i > 0)' just covers up a crash bug on 'order[i - 1]'
            //      - Happens when displaying past groups
            //      - Causes another small bug
            if(i > 0) {
                if (!digitsStarted) {
                    if (order[i].isLetter() && order[i - 1].isDigit()) {
                        newOrderReversed += "-"
                        digitsStarted = true
                    }
                }
                // Add a like between numbers
                if (order[i].isDigit()) {
                    counter++
                    if (counter == 3) {
                        newOrderReversed += "-"
                        counter = 0
                    }
                }
            }
        }
    }
    return newOrderReversed.reversed()
}

fun removeCommas(commaEdOrder: String) = commaEdOrder.split('-').joinToString("")



fun displayToast(context: Context, msg: String) =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()