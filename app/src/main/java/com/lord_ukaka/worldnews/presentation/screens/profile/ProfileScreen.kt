package com.lord_ukaka.worldnews.presentation.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.components.AppIconButton
import com.lord_ukaka.worldnews.core.components.DrawableButton
import com.lord_ukaka.worldnews.core.components.NewsTopBar
import com.lord_ukaka.worldnews.core.utils.Dimension

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val state = remember {
        profileViewModel.state
    }.collectAsState()

    val scaffoldState = rememberScaffoldState()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            state.value.profilePicture = uri
        }
    )

    val generalOptions = remember {
        listOf("Settings", "Offline Reading")
    }
    val personalOptions = remember {
        listOf("Privacy Policies", "Terms and Conditions")
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            NewsTopBar(title = "About Me") {
               navController.navigateUp()
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = Dimension.pagePadding),
            verticalArrangement = Arrangement.spacedBy(Dimension.pagePadding),
        ) {
            item {
                ProfileHeaderSection(
                    image = state.value.profilePicture,
                    name = state.value.username,
                    email = state.value.email,
                    phone = state.value.phoneNumber,
                    onImageClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
            /** General options */
            item {
                Text(
                    text = "General",
                    style = MaterialTheme.typography.body1,
                )
            }
            items(generalOptions) { option ->
                ProfileOptionItem(
                    icon = if (option == "Settings") R.drawable.ic_settings else R.drawable.ic_bookmark,
                    title = option,
                    onOptionClicked = {

                    },
                )
            }
            /** Personal options */
            item {
                Text(
                    text = "Personal",
                    style = MaterialTheme.typography.body1,
                )
            }
            items(personalOptions) { option ->
                ProfileOptionItem(
                    icon = if (option == "Privacy Policies") R.drawable.ic_lock else R.drawable.ic_terms,
                    title = option,
                    onOptionClicked = {},
                )
            }
        }
    }
}

@Composable
fun ProfileOptionItem(
    icon: Int?,
    title: String?,
    onOptionClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .fillMaxWidth()
            .clickable { onOptionClicked() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.pagePadding),
    ) {
        DrawableButton(
            painter = rememberAsyncImagePainter(model = icon),
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.4f),
            iconTint = MaterialTheme.colors.primary,
            onButtonClicked = {},
            iconSize = Dimension.smallIcon,
            paddingValue = PaddingValues(Dimension.medium),
            shape = CircleShape,
        )
        title?.let {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
            )
        }
        AppIconButton(
            icon = Icons.Rounded.KeyboardArrowRight,
            backgroundColor = MaterialTheme.colors.background,
            iconTint = MaterialTheme.colors.onBackground,
            onButtonClicked = {},
            iconSize = Dimension.smallIcon,
            paddingValue = PaddingValues(Dimension.medium),
            shape = CircleShape,
        )
    }
}

@Composable
fun ProfileHeaderSection(
    image: Uri?,
    name: String,
    email: String?,
    phone: String?,
    onImageClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.pagePadding),
    ) {
        AsyncImage(
            modifier = Modifier
                .size(Dimension.extraLargeIcon)
                .clip(CircleShape)
                .clickable { onImageClick() },
            model = image,
            contentDescription = null,
        )

        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.h5,
            )
            Text(
                text = email ?: "",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
            )
            Text(
                text = phone ?: "",
                style = MaterialTheme.typography.caption
                    .copy(fontWeight = FontWeight.Medium),
            )
        }
    }
}