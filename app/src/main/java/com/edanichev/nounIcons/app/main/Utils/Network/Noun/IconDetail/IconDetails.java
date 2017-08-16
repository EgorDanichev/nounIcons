package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail;


import java.util.List;

public class IconDetails {

    public icon icon;

    public class icon {
        public String id;
        private List<tag> tags;

        public List<tag> getTags(){
            return tags;
        }

    }

    public class tag {
        String id;
        String slug;
        String term;

        public String getSlug(){
            return slug;
        }

    }



}
