package com.example.aichat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aichat.ui.theme.AIChatTheme
import com.example.aichat.ui.viewmodel.AIViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIChatTheme {
                val viewModel = hiltViewModel<AIViewModel>()
                ChatScreen(viewModel)
            }
        }
    }
}


@Composable
fun ChatScreen(viewModel: AIViewModel) {
    val chatMessages by viewModel.messageList.collectAsState()
    val keyboardOptions = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Column(modifier = Modifier.padding(top = 50.dp, bottom = 50.dp, start = 16.dp, end = 16.dp)) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(chatMessages) { message ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = if (message.role == "user") {
                        Arrangement.End
                    } else {
                        Arrangement.Start
                    }
                ) {
                    if (message.role == "model") {
                        Text(
                            text = message.message,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            message.message,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Blue,
                        )
                    }
                }

            }
        }

        var userInput by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp, top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your message...") }
            )
            Button(
                onClick = {
                    if (userInput.isNotEmpty()) {
                        viewModel.sendMessage(userInput)
                    } else {
                        Toast.makeText(context, "Prompt empty", Toast.LENGTH_SHORT).show()
                    }
                    keyboardOptions?.hide()
                    userInput = ""
                },
                Modifier.weight(0.3f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.send),
                    contentDescription = "send",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
