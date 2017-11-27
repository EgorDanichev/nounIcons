package com.edanichev.nounIcons.app.main.NounHintCloud.Model;

public class HintCloudInteractor implements IHintCloudInteractor {

    @Override
    public CloudTagsModel getTags() {

        CloudTagsModel tags = new CloudTagsModel();
        tags.add("cat");
        tags.add("info");
        tags.add("dog");
        return tags;
    }
}
