package io.fromto.di

import org.koin.dsl.module
import io.fromto.presentation.translation.TranslateViewModel
import org.koin.core.module.dsl.viewModelOf

val sharedModule = module {
    single { "English" }
    viewModelOf(::TranslateViewModel)
}