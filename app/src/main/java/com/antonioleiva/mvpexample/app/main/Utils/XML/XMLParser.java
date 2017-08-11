package com.antonioleiva.mvpexample.app.main.Utils.XML;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {

    public static String getString(String str) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(str);

        ByteArrayInputStream input =  new ByteArrayInputStream(
                xmlStringBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);

        return doc.getFirstChild().getFirstChild().getNodeValue();

    }
}
