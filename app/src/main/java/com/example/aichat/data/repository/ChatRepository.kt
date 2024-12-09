package com.example.aichat.data.repository

import com.example.aichat.model.MessageModel
import com.example.aichat.ui.viewmodel.AIViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

import javax.inject.Inject

class ChatRepository @Inject constructor(private val aiModel: GenerativeModel) {

    suspend fun getAIResponse(history: List<MessageModel>, prompt: String): String {

        try {
            val chat = aiModel.startChat(
                history.map {
                    content(it.role){
                        text(it.message)
                    }
                }.toList()
            )
            return chat.sendMessage(prompt).text.toString();
        }catch (e: Exception){
            return "Error: Unable to get response"
        }
    }

}