package com.edanichev.nounIcons.app.main.NounHintCloud.Presenter

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.IHintCloudInteractor
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudViewInterface
import com.edanichev.nounIcons.app.main.Utils.Analytics.NounFirebaseAnalytics
import com.edanichev.nounIcons.app.main.Utils.BuildConfig.INounConfig
import com.edanichev.nounIcons.app.main.Utils.EventBus.ChipClickEvent
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.INounSharedPreferences

import org.greenrobot.eventbus.EventBus

import javax.inject.Inject

class HintCloudPresenter @Inject
constructor(
    private val preferences: INounSharedPreferences,
    private val hintCloudInteractor: IHintCloudInteractor,
    private val config: INounConfig,
    private val analytics: NounFirebaseAnalytics
) : HintCloudPresenterInterface {
    private var view: HintCloudViewInterface? = null

    override fun setView(view: HintCloudViewInterface) {
        this.view = view
    }

    override fun onCreate() {
        loadHintCloud()
    }

    private fun loadHintCloud() {
        if (!preferences.isHintSeen || config.isDebug) {
            view!!.initView()
            view!!.addChipsToHintCloud(hintCloudInteractor.tags)
            view!!.showHintCloud()
            preferences.isHintSeen = true
        } else {
            view!!.hideHintCloud()
        }
    }

    override fun hintClick(text: String) {
        view!!.hideHintCloud()
        EventBus.getDefault().post(ChipClickEvent(text))
        analytics.registerOnHintClick(text)
    }
}