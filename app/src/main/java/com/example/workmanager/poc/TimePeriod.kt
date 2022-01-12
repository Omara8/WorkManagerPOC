package com.example.workmanager.poc

import java.io.Serializable
import java.util.Calendar

data class TimePeriod(val start: Calendar, val end: Calendar): Serializable