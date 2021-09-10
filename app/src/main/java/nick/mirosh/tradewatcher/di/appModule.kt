package nick.mirosh.tradewatcher.di

import nick.mirosh.tradewatcher.MainActivityViewModel
import org.koin.dsl.module

val appModule = module {

    // single instance of HelloRepository
    single<HelloRepository> { HelloRepositoryImpl() }

    // Simple Presenter Factory
    factory { MainActivityViewModel(get()) }
}