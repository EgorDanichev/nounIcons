package com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Translate;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "root")
public class Translation {

    @Element(name = "string",required = true)
    public String string;

    public String getString(){
        return string;
    }
    public Translation() {

    }
}

