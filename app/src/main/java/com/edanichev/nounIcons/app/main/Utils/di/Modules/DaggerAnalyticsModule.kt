package com.edanichev.nounIcons.app.main.Utils.di.Modules

import com.edanichev.nounIcons.app.main.Utils.Analytics.NounFirebaseAnalytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaggerAnalyticsModule {

    @Provides
    @Singleton
    fun provideNounFirebaseAnalytics(): NounFirebaseAnalytics = NounFirebaseAnalytics()
}