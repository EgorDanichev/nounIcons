package com.edanichev.nounIcons.app.main.NounHintCloud.Presenter

import com.edanichev.nounIcons.app.main.NounBase.BasePresenter
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudViewInterface

interface HintCloudPresenterInterface :BasePresenter<HintCloudViewInterface> {
    fun hintClick(text: String)
}