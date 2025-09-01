package com.malx4c.recipesapp.di

interface Factory<T> {
    fun create(): T
}