package com.lord_ukaka.worldnews.presentation.screens.signup

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.components.*
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.utils.Dimension
import com.lord_ukaka.worldnews.core.utils.getJsonData
import com.lord_ukaka.worldnews.domain.models.CountryDetails
import java.lang.reflect.Type

@Composable
fun SignupScreen(
    signupViewModel: SignupViewModel = hiltViewModel(),
    onUserAccountCreated: () -> Unit,
    navController: NavController
) {
    val state = signupViewModel.state
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val jsonFileString = getJsonData(context, "countries_details.json")

    val gson = Gson()
    val countriesAndDetailsType: Type = object : TypeToken<List<CountryDetails>>() {}.type
    val countryDetails: List<CountryDetails> = gson.fromJson(jsonFileString, countriesAndDetailsType)
    val countriesMenuOptionsExpanded by remember {
        signupViewModel.countriesOptionsMenuExpanded
    }
    var passwordIsVisible by remember {
        mutableStateOf(false)
    }
    var repeatPasswordIsVisible by remember {
        mutableStateOf(false)
    }
    val passwordVisibilityIcon = if (passwordIsVisible) {
        painterResource(id = R.drawable.ic_visibility)
    } else {
        painterResource(id = R.drawable.ic_visibility_off)
    }
    val repeatPasswordVisibilityIcon = if (repeatPasswordIsVisible) {
        painterResource(id = R.drawable.ic_visibility)
    } else {
        painterResource(id = R.drawable.ic_visibility_off)
    }
    var buttonIsLoading by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = context) {
        signupViewModel.validationEvents.collect{ event ->
            when (event) {
                is SignupViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Congratulations! Registration successful",
                        Toast.LENGTH_LONG
                    ).show()
                    onUserAccountCreated()
                    //navController.navigate(Screen.Home.route)
                }
            }
        }
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
        Spacer(modifier = Modifier.height(Dimension.threeSpacers))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create account",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(Dimension.oneAndHalfSpacer))
            Row(
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(Dimension.oneSpacer)
           ) {
               Text(
                   text = "Already Joined?",
                   color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
               )
               Text(
                   text = "Log in",
                   modifier = Modifier
                       .clickable { navController.navigate(Screen.Login.route) },
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
                value = state.firstName,
                placeholder = "First Name",
                onValueChange = {
                                signupViewModel.onEvent(SignupEvents.EnteredFirstName(it))
                },
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
                }
            )
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            CustomInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth(),
                value = state.lastName,
                placeholder = "Last Name",
                onValueChange = {
                    signupViewModel.onEvent(SignupEvents.EnteredLastName(it))
                },
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
                }
            )
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            TextInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth(),
                value = state.email,
                onValueChange = {
                                signupViewModel.onEvent(SignupEvents.EnteredEmail(it))
                },
                onFocusChange = {},
                onKeyboardActionClicked = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = "email"
                    )
                },
                isError = state.emailError != null,
                placeholder = {
                    Text(text = "Email")
                },
                keyboardType = KeyboardType.Email
            )
            if (state.emailError != null) {
                Text(
                    text = state.emailError,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            TextInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth(),
                value = state.password,
                onValueChange = {
                    signupViewModel.onEvent(SignupEvents.EnteredPassword(it))
                },
                onFocusChange = {},
                onKeyboardActionClicked = {},
                visualTransformation = if (passwordIsVisible) { VisualTransformation.None}
                else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "password"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordIsVisible = !passwordIsVisible }) {
                        Icon(
                            modifier = Modifier
                                .padding(end = Dimension.pagePadding.div(2))
                                .size(Dimension.mediumIcon.times(0.7f)),
                            painter = passwordVisibilityIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
                        )
                    }
                },
                isError = state.passwordError != null,
                placeholder = {
                    Text(text = "Password")
                },
                keyboardType = KeyboardType.Password
            )
            if (state.passwordError != null) {
                Text(
                    text = state.passwordError,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            TextInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth(),
                value = state.repeatPassword,
                onValueChange = {
                    signupViewModel.onEvent(SignupEvents.EnteredRepeatedPassword(it))
                },
                onFocusChange = {},
                onKeyboardActionClicked = {},
                visualTransformation = if (repeatPasswordIsVisible) { VisualTransformation.None}
                else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "password"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { repeatPasswordIsVisible = !repeatPasswordIsVisible }) {
                        Icon(
                            modifier = Modifier
                                .padding(end = Dimension.pagePadding.div(2))
                                .size(Dimension.mediumIcon.times(0.7f)),
                            painter = repeatPasswordVisibilityIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
                        )
                    }
                },
                isError = state.repeatPasswordError != null,
                placeholder = {
                    Text(text = "Repeat Password")
                },
                keyboardType = KeyboardType.Password
            )
            if (state.repeatPasswordError != null) {
                Text(
                    text = state.repeatPasswordError,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            CountriesOptionsMenu(
                options = countryDetails,
                onOptionsMenuExpandChanges = {signupViewModel.toggleOptionsMenuExpandState()},
                onMenuOptionSelected = {
                    signupViewModel.toggleOptionsMenuExpandState()
                    signupViewModel.onEvent(SignupEvents.EnteredCountry(it.name))
                },
                optionsMenuExpanded = countriesMenuOptionsExpanded
            )
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            GenderOptionsMenu(
                onMenuOptionSelected = { signupViewModel.onEvent(SignupEvents.EnteredGender(it)) }
            )
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            TextInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth(),
                value = state.phoneNUmber,
                onValueChange = {
                    signupViewModel.onEvent(SignupEvents.EnteredPhoneNumber(it))
                },
                onFocusChange = {},
                onKeyboardActionClicked = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_call),
                        contentDescription = "phone number"
                    )
                },
                isError = state.phoneNumberError != null,
                placeholder = {
                    Text(text = "Phone Number")
                },
                keyboardType = KeyboardType.Phone
            )
            if (state.phoneNumberError != null) {
                Text(
                    text = state.phoneNumberError,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            Row(modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = state.isTermsAccepted,
                    onCheckedChange = {
                        signupViewModel.onEvent(SignupEvents.AcceptedTerms(it))
                    }
                )
                Spacer(modifier = Modifier.width(Dimension.small))
                Text(text = "Accept Terms")
            }
            if (state.termsError != null) {
                Text(
                    text = state.termsError,
                    color = MaterialTheme.colors.error
                )
            }
            Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            linkedText()
            Spacer(modifier = Modifier.height(Dimension.twoSpacers))
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large,
                    ),
                shape = MaterialTheme.shapes.large,
                padding = PaddingValues(Dimension.pagePadding.div(2)),
                buttonColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                text = "Let's Go!",
                isLoading = buttonIsLoading,
                enabled = true,
                textStyle = MaterialTheme.typography.button,
                onButtonClicked = {
                    /** Handle the click event of the login button */
                    buttonIsLoading = true
                    signupViewModel.onEvent(SignupEvents.OnClickSignIn)
                },
            )
        }
    }
}