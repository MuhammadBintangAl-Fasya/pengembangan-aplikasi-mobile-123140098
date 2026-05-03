package com.bintang.myprofileapp.di

import app.cash.sqldelight.db.SqlDriver
import com.bintang.myprofileapp.ai.network.HttpClientFactory
import com.bintang.myprofileapp.ai.repository.AIRepository
import com.bintang.myprofileapp.ai.repository.AIRepositoryImpl
import com.bintang.myprofileapp.ai.service.GeminiChatService
import com.bintang.myprofileapp.ai.service.GeminiService
import com.bintang.myprofileapp.data.NoteRepository
import com.bintang.myprofileapp.data.SettingsManager
import com.bintang.myprofileapp.db.DatabaseDriverFactory
import com.bintang.myprofileapp.db.NotesDatabase
import com.bintang.myprofileapp.viewmodel.AIViewModel
import com.bintang.myprofileapp.viewmodel.NotesViewModel
import com.bintang.myprofileapp.viewmodel.ProfileViewModel
import com.bintang.myprofileapp.viewmodel.SettingsViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val commonModule = module {
    single { Settings() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single { NotesDatabase(get<SqlDriver>()) }
    singleOf(::NoteRepository)
    singleOf(::SettingsManager)

    // AI Dependencies
    single { HttpClientFactory.create() }
    single { GeminiService(get()) }
    single { GeminiChatService(get()) }
    single<AIRepository> { AIRepositoryImpl(get(), get()) }

    // ViewModels
    viewModelOf(::NotesViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AIViewModel)
}

val appModules = listOf(commonModule, platformModule)
