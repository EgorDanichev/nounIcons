package com.edanichev.nounIcons.app.main.NounHintCloud.Model

open class HintCloudInteractor {

    open fun getTags(): CloudTagsModel {

        val tags = CloudTagsModel()
        tags.add("cat")
        tags.add("info")
        tags.add("dog")
        return tags
    }
}
