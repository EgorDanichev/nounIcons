package com.edanichev.nounIcons.app.main.NounHintCloud.Presenter

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.HintCloudInteractor
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudViewInterface
import com.edanichev.nounIcons.app.main.Utils.Analytics.NounFirebaseAnalytics
import com.edanichev.nounIcons.app.main.Utils.BuildConfig.INounConfig
import com.edanichev.nounIcons.app.main.Utils.EventBus.ChipClickEvent
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.INounSharedPreferences
import org.greenrobot.eventbus.EventBus.getDefault
import javax.inject.Inject

class HintCloudPresenter @Inject
constructor(
    private val preferences: INounSharedPreferences,
    private val hintCloudInteractor: HintCloudInteractor,
    private val config: INounConfig,
    private val analytics: NounFirebaseAnalytics
) : HintCloudPresenterInterface {
    private lateinit var view: HintCloudViewInterface

    override fun setView(view: HintCloudViewInterface) {
        this.view = view
    }

    override fun onCreate() {
        loadHintCloud()
    }

    private fun loadHintCloud() {
        if (!preferences.isHintSeen || config.isDebug) {
            with(view) {
                initChips()
                addChipsToHintCloud(hintCloudInteractor.getTags())
                showHintCloud()
            }
            preferences.isHintSeen = true
        } else {
            view.hideHintCloud()
        }
    }

    override fun hintClick(text: String) {
        view.hideHintCloud()
        getDefault().post(ChipClickEvent(text))
        analytics.registerOnHintClick(text)
    }
}