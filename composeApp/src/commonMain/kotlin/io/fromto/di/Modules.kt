package io.fromto.di

import io.fromto.data.datasource.local.HistoryLocalDataSource
import io.fromto.data.datasource.local.SqlHistoryDataSource
import io.fromto.data.datasource.remote.KtorRemoteDataSource
import io.fromto.data.datasource.remote.RemoteDataSource
import io.fromto.data.remote.HttpClientFactory
import io.fromto.data.repository.HistoryRepositoryImpl
import io.fromto.data.repository.LocalizationRepositoryImpl
import io.fromto.data.repository.TranslateRepositoryImpl
import io.fromto.data.repository.LanguageSelectorRepositoryImpl
import io.fromto.database.TranslateDatabase
import io.fromto.domain.repository.HistoryRepository
import io.fromto.domain.repository.LanguageSelectorRepository
import io.fromto.domain.repository.LocalizationRepository
import io.fromto.domain.repository.TranslateRepository
import io.fromto.domain.usecase.ChangeLocaleUseCase
import io.fromto.domain.usecase.ClearHistoryUseCase
import io.fromto.domain.usecase.DeleteHistoryUseCase
import io.fromto.domain.usecase.GetCurrentLocaleUseCase
import io.fromto.domain.usecase.GetHistoryUseCase
import io.fromto.domain.usecase.SaveHistoryUseCase
import io.fromto.domain.usecase.GetSelectedLanguagesUseCase
import io.fromto.domain.usecase.UpdateSelectedLanguagesUseCase
import io.fromto.domain.usecase.TranslateUseCase
import io.fromto.presentation.history.HistoryViewModel
import io.fromto.presentation.translation.TranslateViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteDataSource).bind<RemoteDataSource>()
    singleOf(::TranslateRepositoryImpl).bind<TranslateRepository>()
    singleOf(::LocalizationRepositoryImpl).bind<LocalizationRepository>()
    singleOf(::LanguageSelectorRepositoryImpl).bind<LanguageSelectorRepository>()
    singleOf(::TranslateUseCase)
    singleOf(::ChangeLocaleUseCase)
    singleOf(::GetSelectedLanguagesUseCase)
    singleOf(::UpdateSelectedLanguagesUseCase)
    singleOf(::ChangeLocaleUseCase)
    singleOf(::GetCurrentLocaleUseCase)
    viewModelOf(::TranslateViewModel)
}

val historyModule = module {
    singleOf(::HistoryRepositoryImpl).bind<HistoryRepository>()
    singleOf(::SqlHistoryDataSource).bind<HistoryLocalDataSource>()
    single { TranslateDatabase(get()) }
    singleOf(::GetHistoryUseCase)
    singleOf(::DeleteHistoryUseCase)
    singleOf(::ClearHistoryUseCase)
    singleOf(::SaveHistoryUseCase)
    viewModelOf(::HistoryViewModel)
}