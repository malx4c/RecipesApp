package com.malx4c.recipesapp

import android.app.Application
import com.malx4c.recipesapp.di.AppContainer

class RecipesApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}