package com.example.sampleapp

import android.util.Log
import javax.inject.Inject

class PetrolEngine : Engine {
    companion object{
        const val TAG:String="PetrolEngine"
    }
    @Inject
    constructor(){

    }
    override fun start() {
        Log.e(TAG, "start: Engine started")
    }
}