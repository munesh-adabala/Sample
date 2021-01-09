package com.example.sampleapp

import dagger.Component

@Component(modules = [PetrolEngineModule::class])
interface CarComponent {
    fun getCar():Car

    fun inject(mainActivity: MainActivity)
}