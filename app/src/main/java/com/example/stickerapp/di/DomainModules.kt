package com.example.stickerapp.di

import com.example.stickerapp.domain.usecases.*
import org.koin.dsl.module

val domainModules = module {
    factory { AddTeamStickersUseCase(get()) }
    factory { GetAllTeamsUseCase(get()) }
    factory { GetLostStickersText(get()) }
    factory { GetTeamUseCase(get()) }
    factory { PopulateInitialTeamsUseCase(get()) }
    factory { UpdateTeamStickersUseCase(get()) }
}