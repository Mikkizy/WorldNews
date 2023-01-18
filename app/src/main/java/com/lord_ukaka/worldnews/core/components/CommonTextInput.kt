package com.lord_ukaka.worldnews.core.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.lord_ukaka.worldnews.core.utils.Dimension

@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
    backgroundColor: Color = MaterialTheme.colors.surface,
    requireSingleLine: Boolean = true,
    textShouldBeCentered: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int = Int.MAX_VALUE,
    imeAction: ImeAction = ImeAction.Done,
    shape: Shape = MaterialTheme.shapes.small,
    padding: PaddingValues = PaddingValues(horizontal = Dimension.extraSmall),
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    onValueChange: (string: String) -> Unit,
    onFocusChange: (focused: Boolean) -> Unit,
    onKeyboardActionClicked: KeyboardActionScope.() -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current

    Row(
        modifier = modifier
            .clip(shape = shape)
            .background(backgroundColor)
            .padding(paddingValues = padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.pagePadding.div(2))
    ) {
        leadingIcon()
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    onFocusChange(it.isFocused)
                },
            value = value,
            onValueChange = {
                if (it.length <= maxLength) {
                    /** when the value change and maxLength is not reached yet then pass it up **/
                    onValueChange(it)
                }
            },
            decorationBox = { container ->
                Box(
                    contentAlignment = if (textShouldBeCentered) Alignment.Center else Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle,
                            color = textColor.copy(alpha = 0.3f),
                            maxLines = if (requireSingleLine) 1 else Int.MAX_VALUE,
                        )
                    }
                    container()
                }
            },
            visualTransformation = visualTransformation,
            singleLine = requireSingleLine,
            textStyle = textStyle.copy(color = textColor),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onAny = {
                    focusRequester.freeFocus()
                    /** It doesn't has the focus now, hide the input keyboard */
                    inputService?.hideSoftwareKeyboard()
                    onKeyboardActionClicked()
                }
            ),
            cursorBrush = SolidColor(value = textColor),
        )
        trailingIcon()
    }
}

@Composable
fun TextInputField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: @Composable () -> Unit = {},
    label: @Composable () -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textColors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    requireSingleLine: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int = Int.MAX_VALUE,
    imeAction: ImeAction = ImeAction.Done,
    shape: Shape = MaterialTheme.shapes.small,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    onValueChange: (string: String) -> Unit,
    onFocusChange: (focused: Boolean) -> Unit,
    onKeyboardActionClicked: KeyboardActionScope.() -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current

    TextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    onFocusChange(it.isFocused)
                },
            value = value,
            onValueChange = {
                if (it.length <= maxLength) {
                    /** when the value change and maxLength is not reached yet then pass it up **/
                    onValueChange(it)
                }
            },
            placeholder = placeholder,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            colors = textColors,
            isError = isError,
            visualTransformation = visualTransformation,
            singleLine = requireSingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onAny = {
                    focusRequester.freeFocus()
                    /** It doesn't have the focus now, hide the input keyboard */
                    inputService?.hideSoftwareKeyboard()
                    onKeyboardActionClicked()
                }
            ),
            shape = shape,
            maxLines = maxLength,
            interactionSource = interactionSource
        )
}

@Composable
fun linkedText() {

    val annotatedString = buildAnnotatedString {
        append("By signing up, I confirm that I accept the ")
        //Use the right annotation links here.
        pushStringAnnotation(tag = "terms of use", annotation = "https://google.com/terms")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary,
                textDecoration = TextDecoration.Underline
            ),
        ) {
            append("terms of use")
        }
        pop()
        append(" and consent to the processing of my personal and biometric data as stated in the ")
        pushStringAnnotation(tag = "policy", annotation = "https://google.com/policy")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("privacy policy")
        }
        pop()
    }
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.body1,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "terms of use", start = offset, end = offset).firstOrNull()?.let {
               Log.d("terms of use", it.item)
           }
            annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset).firstOrNull()?.let {
                Log.d("policy", it.item)
            }
        }
    )
}