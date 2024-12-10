package com.example.aichat.di

import com.example.aichat.BuildConfig.*
import com.example.aichat.data.repository.ChatRepository
import com.example.aichat.ui.viewmodel.AIViewModel
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AIModule {

    @Singleton
    @Provides
    fun provideGemini(): GenerativeModel {
        return GenerativeModel(
            "gemini-1.5-flash", API_KEY
        )
    }

    @Provides
    fun provideChatRepository(aiModel: GenerativeModel): ChatRepository{
        return ChatRepository(aiModel)
    }

}