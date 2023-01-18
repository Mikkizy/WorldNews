package com.lord_ukaka.worldnews.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.lord_ukaka.worldnews.core.sealed.Orientation
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/** An extension function on Date's object that is used to get a formatted date & time.
 * It takes the pattern that you want.
 * Shortcuts: yyyy: year , MM: month , dd: day , HH: hour , mm: minutes.
 */
fun Long.getFormattedDate(pattern: String): String {
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    return simpleDateFormat.format(Calendar.getInstance().also { it.timeInMillis = this }.time)
}

/** An extension function to convert time in milliseconds to a formatted time HH:MM:ss */
@Composable
fun Long.toTimeString(): String {
    return if (this <= 0) "--:--" else {
        val time = this / 1000 // convert from milliseconds to seconds
        val timeString = StringBuilder()

        val hours = time / 3600
        timeString.append(if (hours > 0) hours.prettifyTime().plus(":") else "")
        val minutes = (time % 3600) / 60
        timeString.append(minutes.prettifyTime().plus(":"))
        val seconds = time % 60
        timeString.append(seconds.prettifyTime())
        Timber.d("Milliseconds is $this and formatted time is $timeString")
        return timeString.toString()
    }
}

private fun Long.prettifyTime() = if (this < 10) "0$this" else "$this"

//Custom placeholder using accompanist
@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.myPlaceHolder(
    visible: Boolean,
    shape: Shape = MaterialTheme.shapes.small,
    color: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
    highLightColor: Color = MaterialTheme.colors.surface,
) = this.placeholder(
    visible = visible,
    color = color,
    shape = shape,
    highlight = PlaceholderHighlight.shimmer(
        highlightColor = highLightColor
    ),
)

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showToast(message: Int) {
    Toast.makeText(this, this.getText(message), Toast.LENGTH_LONG).show()
}

/**
 *  An extension function that is used to append an element to a list - or remove it in case it already exist.
 * Return the element if added or null if removed
 */
fun <T> MutableList<T>.appendOrRemove(element: T): T? {
    remove(element).also { removed ->
        return if (removed) {
            /** Removed successfully */
            null
        } else {
            /** Not exist, we should add it */
            this.add(element = element)
            element
        }
    }
}

fun Modifier.addMoveAnimation(orientation: Orientation, from: Dp, to: Dp, duration: Int): Modifier =
    composed {
        var contentOffset by remember { mutableStateOf(from) }
        val animatedContentOffset by animateDpAsState(
            targetValue = contentOffset,
            animationSpec = TweenSpec(
                durationMillis = duration,
            )
        ).also {
            contentOffset = to
        }
        when (orientation) {
            is Orientation.Vertical -> this.offset(y = animatedContentOffset)
            is Orientation.Horizontal -> this.offset(x = animatedContentOffset)
        }
    }

fun Modifier.addFadeAnimation(from: Float, to: Float, duration: Int): Modifier = composed {
    var contentAlpha by remember { mutableStateOf(from) }
    val animatedContentAlpha by animateFloatAsState(
        targetValue = contentAlpha,
        animationSpec = TweenSpec(
            durationMillis = duration,
        )
    ).also {
        contentAlpha = to
    }
    this.alpha(animatedContentAlpha)
}

fun String.getValidColor() = when (this) {
    "white" -> 0xFFFFFFFF
    "gold" -> 0xFFFFC107
    "yellow" -> 0xFFFFEB3B
    "green" -> 0xFF4CAF50
    "dark-green" -> 0xFF3C613E
    "lemon" -> 0xFF44FF00
    "red" -> 0xFFF44336
    "black" -> 0xFF000000
    "gray" -> 0xFF494949
    "pink" -> 0xFFC95E90
    else -> 0xFF000000
}
