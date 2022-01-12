package com.example.workmanager.poc

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class TimePeriodLayout(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet) {

    private var confirmButton: MaterialButton? = null
    private var startTime: EditText? = null
    private var endTime: EditText? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.time_period_layout, this, true)
        confirmButton = this.findViewById(R.id.confirm_button)
        startTime = this.findViewById(R.id.start_time)
        endTime = this.findViewById(R.id.end_time)
        setButtonListener()
    }

    private fun setButtonListener(){
        confirmButton?.setOnClickListener {
            if (startTime?.text?.toString()?.checkIfValidTime()!!
                && endTime?.text?.toString()?.checkIfValidTime()!!
            ) {
                val start = startTime?.text?.toString()?.split(":")
                val end = endTime?.text?.toString()?.split(":")
                val startTimePeriod = Calendar.getInstance()
                val endTimePeriod = Calendar.getInstance()
                startTimePeriod[Calendar.HOUR_OF_DAY] = start?.get(0)?.toInt()!!
                startTimePeriod[Calendar.MINUTE] = start[1].toInt()
                endTimePeriod[Calendar.HOUR_OF_DAY] = end?.get(0)?.toInt()!!
                endTimePeriod[Calendar.MINUTE] = end[1].toInt()
                appendToMutableList(TimePeriod(startTimePeriod, endTimePeriod))
                startTime?.isEnabled = false
                endTime?.isEnabled = false
            } else Toast.makeText(context, "Enter correct time info", Toast.LENGTH_LONG).show()
        }
    }

    fun setTimePeriods(timePeriod: TimePeriod){
        startTime?.setText("${timePeriod.start[Calendar.HOUR_OF_DAY]}:${timePeriod.start[Calendar.MINUTE]}")
        endTime?.setText("${timePeriod.end[Calendar.HOUR_OF_DAY]}:${timePeriod.end[Calendar.MINUTE]}")
        startTime?.isEnabled = false
        endTime?.isEnabled = false
    }

    private fun appendToMutableList(timePeriod: TimePeriod) {
        val tempList = AppPrefHelper().getTimePeriodsList() ?: mutableListOf()
        tempList.add(timePeriod)
        AppPrefHelper().setTimePeriodsList(tempList)
    }

}

fun String.checkIfValidTime(): Boolean {
    val regex = Regex(pattern = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\$", options = setOf(RegexOption.IGNORE_CASE))
    return regex.matches(this)
}