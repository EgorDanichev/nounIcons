package com.edanichev.nounIcons.app.main.NounHintCloud.View

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.edanichev.nounIcons.app.R
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.CloudTagsModel
import com.edanichev.nounIcons.app.main.NounHintCloud.Presenter.HintCloudPresenterInterface
import com.edanichev.nounIcons.app.main.Utils.UI.Chip.ChipConfig
import com.edanichev.nounIcons.app.main.Utils.extensions.bind
import com.google.android.flexbox.FlexboxLayout
import fisk.chipcloud.ChipCloud
import fisk.chipcloud.ChipListener

class HintCloudView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr), HintCloudViewInterface {

    private val flexBox by bind<FlexboxLayout>(R.id.hint_flexbox)
    private lateinit var hintCloud: ChipCloud

    override lateinit var presenter: HintCloudPresenterInterface

    override fun showHintCloud() {
        visibility = View.VISIBLE
    }

    override fun hideHintCloud() {
        visibility = View.GONE
    }

    override fun addChipsToHintCloud(tags: CloudTagsModel) {
        hintCloud.addChips(tags)
    }

    override fun initChips() {
        hintCloud = ChipCloud(context, flexBox, ChipConfig.getChipCloudConfig())
        hintCloud.setListener(onChipClickListener)
    }

    private val onChipClickListener =
        ChipListener { clickedChipIndex: Int, b: Boolean, _: Boolean ->
            if (b) {
                val clickedChipText = hintCloud.getLabel(clickedChipIndex)
                presenter.hintClick(clickedChipText)
            }
        }
}