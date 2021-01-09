package com.example.sampleapp

import android.util.Log
import dagger.Module
import javax.inject.Inject

class Car {
    companion object{
        const val TAG:String="Car"
    }
    lateinit var wheels:Wheels
    lateinit var engine: Engine

    @Inject
    constructor(wheels:Wheels,engine:Engine){
        this.wheels=wheels
        this.engine=engine
    }

    fun start() {
        engine.start()
        Log.e(TAG, "start: Car starting")

    }
}