package com.del.demoinsta.di

import javax.inject.Qualifier

/**
 *
 */
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ApplicationContextQualifier

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityContextQualifier

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class FragmentContextQualifier

//-------------------------------------------------

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class DatabaseInfoQualifier

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class NetworkInfoQualifier


/**
 *
 * Added a qualifier, so in future if we write more provideOtherDirectory()
 * It will be helpful to distinguish the responsibilities.
 */
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class TempDirectoryQualifier