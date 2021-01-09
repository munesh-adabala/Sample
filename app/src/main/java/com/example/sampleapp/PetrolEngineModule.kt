package com.example.sampleapp

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class PetrolEngineModule {

    @Binds
    abstract fun providesPetrolEngine(engine:PetrolEngine):Engine
}