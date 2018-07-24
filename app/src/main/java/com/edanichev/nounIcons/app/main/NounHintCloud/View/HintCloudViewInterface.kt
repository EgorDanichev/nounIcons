package com.edanichev.nounIcons.app.main.NounHintCloud.View

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.CloudTagsModel
import com.edanichev.nounIcons.app.main.NounHintCloud.Presenter.HintCloudPresenterInterface
import com.edanichev.nounIcons.app.main.base.data.BaseView

interface HintCloudViewInterface : BaseView<HintCloudPresenterInterface> {

    fun showHintCloud()

    fun hideHintCloud()

    fun addChipsToHintCloud(tags: CloudTagsModel)

    fun initChips()
}