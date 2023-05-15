package com.example.locationtaskreminder.helpers

import com.example.locationtaskreminder.data.model.Reminder
import java.text.DecimalFormat
import kotlin.random.Random


fun Double.formatToTwoDecimalPlaces(): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(this)
}


// Define a function to generate a random Reminder object
fun generateRandomReminder(): Reminder {
    val titles = listOf("Meeting", "Grocery Shopping", "Workout", "Appointment")
    val descriptions = listOf("Discuss project updates", "Buy milk and eggs", "30-minute jog", "Dentist visit")
    val latitudes = (1..100).map { Random.nextDouble(0.0, 90.0) }
    val longitudes = (1..100).map { Random.nextDouble(0.0, 180.0) }

    val randomTitle = titles.random()
    val randomDescription = descriptions.random()
    val randomLatitude = latitudes.random().toString()
    val randomLongitude = longitudes.random().toString()

    return Reminder(title = randomTitle, description = randomDescription,
        latitude = randomLatitude, longitude = randomLongitude)
}

// Generate a list of random reminders
val randomReminders: List<Reminder> = List(10) { generateRandomReminder() }
