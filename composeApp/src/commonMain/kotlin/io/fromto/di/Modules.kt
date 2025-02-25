package io.fromto.di

import io.fromto.data.datasource.remote.RemoteDataSource
import io.fromto.data.datasource.remote.KtorRemoteDataSource
import io.fromto.data.remote.HttpClientFactory
import io.fromto.domain.repository.TranslateRepository
import io.fromto.data.repository.TranslateRepositoryImpl
import io.fromto.domain.usecase.TranslateUseCase
import org.koin.dsl.module
import io.fromto.presentation.translation.TranslateViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteDataSource).bind<RemoteDataSource>()
    singleOf(::TranslateRepositoryImpl).bind<TranslateRepository>()
    singleOf(::TranslateUseCase)
    viewModelOf(::TranslateViewModel)
}