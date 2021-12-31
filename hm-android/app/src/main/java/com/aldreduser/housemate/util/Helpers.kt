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