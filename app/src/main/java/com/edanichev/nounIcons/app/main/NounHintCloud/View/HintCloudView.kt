package com.edanichev.nounIcons.app.main.NounHintCloud.View

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.RelativeLayout
import com.edanichev.nounIcons.app.R
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.CloudTagsModel
import com.edanichev.nounIcons.app.main.Utils.EventBus.ChipClickEvent
import com.edanichev.nounIcons.app.main.Utils.UI.Animation.NounAnimations
import com.edanichev.nounIcons.app.main.Utils.UI.Chip.ChipConfig
import com.edanichev.nounIcons.app.main.Utils.extensions.bind
import com.google.android.flexbox.FlexboxLayout
import fisk.chipcloud.ChipCloud
import fisk.chipcloud.ChipListener
import org.greenrobot.eventbus.EventBus

class HintCloudView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr), HintCloudViewInterface {

    private val flexBox by bind<FlexboxLayout>(R.id.hint_flexbox)
    private lateinit var hintCloud: ChipCloud

    override fun showHintCloud() {
        visibility = View.VISIBLE
    }

    override fun hideHintCloud() {
        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {}
        }

        val animation = NounAnimations.getBecomeInvisibleAnimation()
        animation.setAnimationListener(animationListener)
        startAnimation(animation)
    }

    override fun addChipsToHintCloud(tags: CloudTagsModel?) {
        hintCloud.addChips(tags)
    }

    override fun initView() {
        hintCloud = ChipCloud(context, flexBox, ChipConfig.getChipCloudConfig())
        hintCloud.setListener(onChipClickListener())
    }

    private fun onChipClickListener(): ChipListener {
        return ChipListener { clickedChipIndex: Int, b: Boolean, _: Boolean ->
            if (b) {
                hideHintCloud()
                val clickedChipText = hintCloud.getLabel(clickedChipIndex)
                EventBus.getDefault().post(ChipClickEvent(clickedChipText))
            }
        }
    }
}