package com.example.rayamajhi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.mycontacts.R





@Composable
fun MainActivityUI(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display a logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )



        // Image button to navigate to the second activity
        Button(
            onClick = { navController.navigate("second_activity") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Go to Add Contact")
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main_activity") {
                composable("main_activity") { MainActivityUI(navController) }
                composable("second_activity") { AddContactScreen(navController) }
            }
        }
    }
}


// ViewModel to handle contact data
class ContactViewModel : ViewModel() {
    var name by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var email by mutableStateOf("")
    var contactType by mutableStateOf("Friend")

    private val _contactList = mutableStateListOf<Contact>()
    val contactList: List<Contact> get() = _contactList

    fun addContact() {
        val contact = Contact(name, phoneNumber, email, contactType)
        _contactList.add(contact)
    }
}

data class Contact(val name: String, val phone: String, val email: String, val type: String)

@Composable
fun AddContactScreen(navController: NavHostController, viewModel: ContactViewModel = viewModel()) {
    var showSnackbar by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Input fields for contact details
        TextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Name") }
        )
        TextField(
            value = viewModel.phoneNumber,
            onValueChange = { viewModel.phoneNumber = it },
            label = { Text("Phone Number") }
        )
        TextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Radio buttons for contact type
        Text("Select Contact Type")
        Row {
            RadioButton(selected = viewModel.contactType == "Friend", onClick = { viewModel.contactType = "Friend" })
            Text("Friend")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = viewModel.contactType == "Family", onClick = { viewModel.contactType = "Family" })
            Text("Family")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = viewModel.contactType == "Work", onClick = { viewModel.contactType = "Work" })
            Text("Work")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to add the contact
        Button(
            onClick = {
                viewModel.addContact()
                showSnackbar = true
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Add Contact")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show Snack bar with contact details when added
        if (showSnackbar) {
            Snackbar(
                action = {
                    Button(onClick = { showSnackbar = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Contact Added: ${viewModel.name}, ${viewModel.phoneNumber}, ${viewModel.email}, ${viewModel.contactType}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display added contacts using LazyColumn
        LazyColumn {
            items(viewModel.contactList) { contact ->
                Text(text = "${contact.name} - ${contact.phone} - ${contact.email} (${contact.type})")
            }
        }
    }
}
