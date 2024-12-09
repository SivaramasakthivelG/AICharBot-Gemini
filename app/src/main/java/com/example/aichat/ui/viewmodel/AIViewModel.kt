package com.example.aichat.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aichat.data.repository.ChatRepository
import com.example.aichat.model.MessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AIViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {

    private val _messageList = MutableStateFlow<List<MessageModel>>(emptyList())
    val messageList: StateFlow<List<MessageModel>> get() = _messageList


    fun sendMessage(question: String) {
        viewModelScope.launch {

            val updatedList = _messageList.value.toMutableList().apply {
                add(MessageModel("User: $question", "user"))
                add(MessageModel("Typing...", "model"))

            }
            _messageList.value = updatedList

            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getAIResponse(updatedList.takeLast(2), question)
                }
                val responseList = _messageList.value.toMutableList().apply {
                    removeAt(updatedList.lastIndex)
                    add(MessageModel(response.trim(), "model"))
                }
                _messageList.value = responseList.toList()
            } catch (e: Exception) {
                updatedList.removeAt(updatedList.lastIndex)
                updatedList.add(MessageModel("Something went wrong", "model"))
                _messageList.value = updatedList
            }

        }
    }
}
