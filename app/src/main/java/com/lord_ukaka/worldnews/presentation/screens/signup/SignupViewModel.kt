package com.lord_ukaka.worldnews.presentation.screens.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lord_ukaka.worldnews.domain.models.User
import com.lord_ukaka.worldnews.domain.usecases.GetValidations
import com.lord_ukaka.worldnews.domain.usecases.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userUsecase: UserUsecase,
    private val getValidations: GetValidations
) : ViewModel() {

    var state by mutableStateOf(SignupState())

    private var currentUserId: Int? = null
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var phoneNumber: String
    private lateinit var country: String
    private lateinit var gender: String

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val _countriesOptionsMenuExpanded = mutableStateOf(false)
    val countriesOptionsMenuExpanded: State<Boolean> = _countriesOptionsMenuExpanded

    fun toggleOptionsMenuExpandState() {
        _countriesOptionsMenuExpanded.value = !_countriesOptionsMenuExpanded.value
    }

    fun onEvent(event: SignupEvents) {
        when (event) {
            is SignupEvents.EnteredFirstName -> {
                state = state.copy(firstName = event.firstName)
                firstName = state.firstName
            }
            is SignupEvents.EnteredLastName -> {
                state = state.copy(lastName = event.lastName)
                lastName = state.lastName
            }
            is SignupEvents.EnteredEmail -> {
                state = state.copy(email = event.email)
                email = state.email
            }
            is SignupEvents.EnteredPassword -> {
                state = state.copy(password = event.password)
                password = state.password
            }
            is SignupEvents.EnteredRepeatedPassword -> {
                state = state.copy(repeatPassword = event.repeatedPassword)
            }
            is SignupEvents.EnteredPhoneNumber -> {
                state = state.copy(phoneNUmber = event.phoneNumber)
                phoneNumber = state.phoneNUmber
            }
            is SignupEvents.EnteredGender -> {
                state = state.copy(gender = event.gender)
                gender = state.gender
            }
            is SignupEvents.EnteredCountry -> {
                state = state.copy(country = event.country)
                country = state.country
            }
            is SignupEvents.AcceptedTerms -> {
                state = state.copy(isTermsAccepted = event.acceptedTerms)
            }
            is SignupEvents.OnClickSignIn -> {
                if (validEntries()) {
                    viewModelScope.launch {
                        validationEventChannel.send(ValidationEvent.Success)
                        userUsecase.insertUser(
                            User(
                                userId = currentUserId,
                                firstName = firstName,
                                lastName = lastName,
                                phone = phoneNumber,
                                email = email,
                                password = password,
                                gender = gender,
                                country = country
                            )
                        )
                    }
                }
            }
        }
    }

    fun validEntries(): Boolean {
        val emailResult = getValidations.validateEmail(state.email)
        val passwordResult = getValidations.validatePassword(state.password)
        val repeatedPasswordResult = getValidations
            .validateRepeatedPassword(state.repeatPassword, state.password)
        val phoneNumberResult = getValidations.validatePhoneNumber(state.phoneNUmber)
        val termsResult = getValidations.validateTerms(state.isTermsAccepted)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            phoneNumberResult,
            termsResult
        ).any {
            !it.successful
        }

        state = state.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            repeatPasswordError = repeatedPasswordResult.errorMessage,
            phoneNumberError = phoneNumberResult.errorMessage,
            termsError = termsResult.errorMessage
        )

        return !hasError
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}