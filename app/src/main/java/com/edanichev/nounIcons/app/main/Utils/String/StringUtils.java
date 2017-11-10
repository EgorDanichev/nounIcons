package com.edanichev.nounIcons.app.main.Utils.String;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StringUtils {
    public static List<String> getTagsInString(List<Tag> tagList) {
        List<String> tags = new ArrayList<>();
        if (tagList != null) {
            for (Tag tag : tagList) {
                if (!Objects.equals(tag.getSlug().trim(), "")) tags.add(tag.getSlug());
            }
        }
        return tags;
    }}
