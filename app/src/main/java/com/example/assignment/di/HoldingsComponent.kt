package com.example.assignment.di

import android.content.Context
import com.example.assignment.ui.DashboardViewModel
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [HoldingModuleDependencies::class])
interface HoldingsComponent {

    fun inject(holdingViewModel: DashboardViewModel)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(loginModuleDependencies: HoldingModuleDependencies): Builder
        fun build(): HoldingsComponent
    }
}