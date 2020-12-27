package com.del.demoinsta.di.component

import com.del.demoinsta.di.module.ApplicationTestModule
import dagger.Component
import javax.inject.Singleton

/**
 *
 *
 *
 * Almost same as // ApplicationComponent
 * but here we extend that interface and mention our test module
 */
@Singleton
@Component(modules = [ApplicationTestModule::class])
interface TestComponent : ApplicationComponent {
}