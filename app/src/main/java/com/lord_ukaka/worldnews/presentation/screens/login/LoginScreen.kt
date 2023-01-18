package com.lord_ukaka.worldnews.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.components.CustomButton
import com.lord_ukaka.worldnews.core.components.CustomInputField
import com.lord_ukaka.worldnews.core.components.NewsTopBar
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.sealed.UiState
import com.lord_ukaka.worldnews.core.utils.Dimension


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onUserAuthenticated: () -> Unit,
    onToastRequested: (message: String) -> Unit,
) {
    val uiState by remember { loginViewModel.uiState }
    val email by remember { loginViewModel.email }
    val password by remember { loginViewModel.password }
    val scrollState = rememberScrollState()
    var passwordIsVisible by remember {
        mutableStateOf(false)
    }
    val visibilityIcon = if (passwordIsVisible) {
        painterResource(id = R.drawable.ic_visibility)
    } else {
        painterResource(id = R.drawable.ic_visibility_off)
    }
    var buttonIsLoading by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(Dimension.pagePadding)
    ) {
        NewsTopBar(title = "") {
            navController.navigateUp()
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.h1.copy(fontSize = with(LocalDensity.current){
                    Dimension.extraLarge.times(2).toSp()
                }),
                color = MaterialTheme.colors.primary,
                fontFamily = FontFamily.Cursive,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimension.oneSpacer)
            ) {
                Text(
                    text = "Don't have an account?",
                    color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                )
                Text(
                    text = "Create one now",
                    modifier = Modifier
                        .clickable { navController.navigate(Screen.Signup.route) },
                    color = MaterialTheme.colors.onSurface.copy(
                        alpha = 0.4f
                    )
                )
            }
            Spacer(modifier = Modifier.height(Dimension.twoSpacers))
            CustomInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth(),
                value = email ?: "",
                placeholder = "Email",
                onValueChange = {
                             loginViewModel.updateEmail(it.ifBlank { null })
                },
                keyboardType = KeyboardType.Email,
                onFocusChange = {},
                onKeyboardActionClicked = {},
                textStyle = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                padding = PaddingValues(
                    horizontal = Dimension.pagePadding,
                    vertical = Dimension.pagePadding.times(0.7f),
                ),
                backgroundColor = MaterialTheme.colors.surface,
                textColor = MaterialTheme.colors.onBackground,
                imeAction = ImeAction.Next,
                shape = MaterialTheme.shapes.large,
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = Dimension.pagePadding.div(2))
                            .size(Dimension.mediumIcon.times(0.7f)),
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
                    )
                },
            )
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            CustomInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large,
                    )
                    .fillMaxWidth(),
                value = password ?: "",
                onValueChange = {
                    loginViewModel.updatePassword(it.ifBlank { null })
                },
                placeholder = "Password ...",
                visualTransformation = if (passwordIsVisible) { VisualTransformation.None}
                    else PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                padding = PaddingValues(
                    horizontal = Dimension.pagePadding,
                    vertical = Dimension.pagePadding.times(0.7f),
                ),
                keyboardType = KeyboardType.Password,
                backgroundColor = MaterialTheme.colors.surface,
                textColor = MaterialTheme.colors.onBackground,
                imeAction = ImeAction.Done,
                shape = MaterialTheme.shapes.large,
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = Dimension.pagePadding.div(2))
                            .size(Dimension.mediumIcon.times(0.7f)),
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordIsVisible = !passwordIsVisible }) {
                        Icon(
                            modifier = Modifier
                                .padding(end = Dimension.pagePadding.div(2))
                                .size(Dimension.mediumIcon.times(0.7f)),
                            painter = visibilityIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
                        )
                    }
                },
                onFocusChange = { },
                onKeyboardActionClicked = { },
            )
            /** The login button */
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = if (uiState !is UiState.Loading) Dimension.elevation else Dimension.zero,
                        shape = MaterialTheme.shapes.large,
                    ),
                shape = MaterialTheme.shapes.large,
                padding = PaddingValues(Dimension.pagePadding.div(2)),
                buttonColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                text = "Login",
                isLoading = buttonIsLoading,
                enabled = uiState !is UiState.Loading,
                textStyle = MaterialTheme.typography.button,
                onButtonClicked = {
                    /** Handle the click event of the login button */
                    buttonIsLoading = true
                    loginViewModel.authenticateUser(
                        email = email ?: "",
                        password = password ?: "",
                        onAuthenticated = {
                            /** When user is authenticated, go home or back */
                            onUserAuthenticated()
                            buttonIsLoading = false
                        },
                        onAuthenticationFailed = {
                            /** Do whatever you want when it failed */
                            onToastRequested("Authentication failed")
                            buttonIsLoading = false
                        }
                    )
                },
            )
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {  },
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = 0.4f
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimension.pagePadding),
                contentAlignment = Alignment.Center,
            ) {
                Column {
                    Divider()
                    Text(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .padding(horizontal = Dimension.pagePadding.div(2)),
                        text = "Or sign in with your social account",
                        style = MaterialTheme.typography.caption
                            .copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                            ),
                    )
                }
            }
            /** Other signing options */
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                buttonColor = Color.Transparent.copy(alpha = 0.2f),
                contentColor = Color.Black,
                text = "Continue with Google",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google"
                    )
                }
            ) {
            }
            Spacer(modifier = Modifier.height(Dimension.oneAndHalfSpacer))
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                buttonColor = Color.Blue.copy(alpha = 0.3f),
                contentColor = Color.White,
                text = "Continue with Facebook",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook"
                    )
                }
            ) {
            }
            Spacer(modifier = Modifier.height(Dimension.oneAndHalfSpacer))
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                buttonColor = Color.Blue.copy(alpha = 0.5f),
                contentColor = Color.White,
                text = "Continue with Twitter",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_twitter),
                        contentDescription = "Twitter"
                    )
                }
            ) {
            }
            Spacer(modifier = Modifier.height(Dimension.oneAndHalfSpacer))
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                buttonColor = Color.Black,
                contentColor = Color.White,
                text = "Continue with Apple",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_apple),
                        contentDescription = "Google"
                    )
                }
            ) {
            }
            Spacer(modifier = Modifier.height(Dimension.threeSpacers))
        }
    }
}