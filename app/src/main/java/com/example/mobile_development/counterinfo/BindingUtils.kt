package com.example.mobile_development.counterinfo

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.mobile_development.changeColorOpacity
import com.example.mobile_development.convertLongToTime
import com.example.mobile_development.database.Counter
import com.google.android.material.floatingactionbutton.FloatingActionButton


@BindingAdapter("createdAtFormatted")
fun TextView.setCreatedAtFormatted(item: Counter?) {
    item?.let {
        text = convertLongToTime(item.createdAt)
    }
}


@BindingAdapter("tappedAtFormatted")
fun TextView.setTappedAtFormatted(item: Counter?) {
    item?.let {
        text = if (item.tappedAt == 0L) {
            "-"
        } else {
            convertLongToTime(item.tappedAt)
        }
    }
}

@BindingAdapter("modifiedBackgroundColor")
fun ConstraintLayout.setModifiedBackgroundColor(item: Counter?) {
    item?.let {
        background= ColorDrawable(Color.parseColor(changeColorOpacity(item.color, "80")))

    }
}

@BindingAdapter("modifiedBackgroundColor")
fun LinearLayout.setModifiedBackgroundColor(item: Counter?) {
    item?.let {
        background= ColorDrawable(Color.parseColor(changeColorOpacity(item.color, "80")))

    }
}

@BindingAdapter("counterBackgroundColor")
fun FloatingActionButton.setCounterBackgroundColor(item: Counter?) {
    item?.let {
        setBackgroundTintList(ColorStateList.valueOf(item.color));

    }
}