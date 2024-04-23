package com.example.test.userslist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Task:
 * 1. Fetch list of users from https://jsonplaceholder.typicode.com/users
 * 2. Display list of those users in RepoListScreen: Their name, email and website
 * 3. Clicking user element should open a website in web browser (not webview). Website URL is part of the user model
 */
@Composable
fun UsersListScreen() {
    val vm: UsersListScreenVM = hiltViewModel()
    when (val state = vm.state) {
        is ViewState.Error -> ErrorView(state.error)
        ViewState.Idle -> Unit
        ViewState.Loading -> LoadingView()
        is ViewState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.users, { it.id }) {
                val context = LocalContext.current
                UserView(user = it, context::openWebsite)
            }
        }
    }
}

@Composable
private fun UserView(user: SimpleUser, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Gray),
            ) {
                onClick(user.websiteUrl)
            },
        colors = CardDefaults.cardColors(containerColor = Color.Red, contentColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = user.name)
            Text(modifier = Modifier.padding(top = 4.dp), text = user.email)
            Text(modifier = Modifier.padding(top = 4.dp), text = user.websiteUrl)
        }
    }
}

@Composable
private fun LoadingView() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(error: String) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = error)
    }
}

fun Context.openWebsite(url: String) = runCatching {
    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(myIntent)
}.onFailure { Log.d("Users", "openWebsite error $it") }
