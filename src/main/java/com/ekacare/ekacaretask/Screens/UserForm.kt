package com.ekacare.ekacaretask.Screens

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ekacare.ekacaretask.Model.User
import com.ekacare.ekacaretask.ViewModel.UserViewModel
import java.util.Calendar

@Composable
fun UserScreen(viewModel: UserViewModel) {
    val users by viewModel.allUsers.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val isException by viewModel.isException.collectAsState()
    var showForm by remember { mutableStateOf(false) }
    var showList by remember { mutableStateOf(users.isNotEmpty()) }
    val saveSuccess by viewModel.saveSuccess.observeAsState(false)
    val exception by viewModel.exception.observeAsState()
    val context = LocalContext.current

    if (isException){
        Toast.makeText(context, exception, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            Log.d("UserScreen", "saveSuccess: $saveSuccess")
            showForm = false
            showList = true
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    when {
        showForm -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Enter User Details",
                    style = TextStyle(fontSize = 22.sp)
                )
                UserForm(viewModel = viewModel, onSubmit = {
                    showForm = false
                    showList = true
                })
            }
        }
        showList -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "Users Detail",
                        style = TextStyle(fontSize = 22.sp)
                    )
                    UserListScreen(viewModel = viewModel)
                }
                FloatingActionButton(
                    onClick = {
                        showForm = true
                        showList = false
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add User")
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (users.isNotEmpty()) {
                    Column (modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text( modifier = Modifier.padding(top = 10.dp), text = "Users Detail",
                            style = TextStyle(
                                fontSize = 22.sp
                            )
                        )
                        UserListScreen(viewModel = viewModel)
                    }
                    FloatingActionButton(
                        onClick = {
                            showForm = true
                            showList = false
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add User")
                    }
                }else{
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "No User Added. Click",
                            style = TextStyle(fontSize = 20.sp)
                        )
                        Icon(Icons.Default.Add, contentDescription = "Add User", Modifier.size(25.dp))
                        Text(
                            text = "to add User.",
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            showForm = true
                            showList = false
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add User")
                    }
                }

            }
        }
    }
}

@Composable
fun UserForm(viewModel: UserViewModel, onSubmit: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(start = 10.dp, top = 16.dp, bottom = 0.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        DOBPicker(dob = dob, onDateSelected = { dob = it })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (name.isNotEmpty() && age.isNotEmpty() && dob.isNotEmpty() && address.isNotEmpty()) {
                    val user = User(name = name, age = age.toInt(), dob = dob, address = address)
                    viewModel.insert(user)
                    name = ""
                    age = ""
                    dob = ""
                    address = ""
                    onSubmit()
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Submit")
        }
    }
}


@Composable
fun UserCard(user: User) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Name: ${user.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Age: ${user.age}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "DOB: ${user.dob}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Address: ${user.address}")
        }
    }
}

@Composable
fun UserListScreen(viewModel: UserViewModel) {
    val users by viewModel.allUsers.observeAsState(emptyList())

    if (users.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(users) { user ->
                UserCard(user = user)
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No Users Available", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DOBPicker(dob: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var pickedDate by remember { mutableStateOf(dob) }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            pickedDate = "$dayOfMonth/${month + 1}/$year"
            onDateSelected(pickedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    OutlinedTextField(
        value = pickedDate,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                datePickerDialog.show()
            },
        label = { Text(text = "DOB", color = Color.Black) },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledBorderColor = Color.Black
        ),
        textStyle = TextStyle.Default.copy(color = Color.Black)
    )
}


