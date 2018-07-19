
package com.hhcx.aoptestframe.helper;

import android.content.Context;
import android.util.Log;

import com.hhcx.aoptestframe.R;
import com.hhcx.aoptestframe.activity.ConstantActivity;
import com.hhcx.aoptestframe.bean.MainItem;
import com.hhcx.aoptestframe.bean.SubItem;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;


public class XmlPullParserHelper {
    public static XmlPullParser getXmlPullParser(Context context) {
        XmlPullParser xmlPullParser;
        switch (ConstantActivity.terminalType) {
            default:
                xmlPullParser = context.getResources().getXml(R.xml.testitems);
                break;
        }
        return xmlPullParser;
    }

    private static MainItem parseToMainItem(XmlPullParser xmlPullParser) {
        MainItem mainItem = new MainItem();
        String command = xmlPullParser.getAttributeValue(null, "command");
        String nameCN = xmlPullParser.getAttributeValue(null, "name_CN");
        String nameEN = xmlPullParser.getAttributeValue(null, "name_EN");
        String packageName = xmlPullParser.getAttributeValue(null, "packageName");
        String isActivity = xmlPullParser.getAttributeValue(null, "isActivity");
        String isUnique = xmlPullParser.getAttributeValue(null, "isUnique");

        mainItem.setCommand(command);
        mainItem.setDisplayNameCN(nameCN);
        mainItem.setDisplayNameEN(nameEN);
        mainItem.setPackageName(packageName);
        mainItem.setActivity(Boolean.parseBoolean(isActivity));
        mainItem.setUnique(Boolean.parseBoolean(isUnique));

        return mainItem;
    }

    private static SubItem parseToSubItem(XmlPullParser xmlPullParser) {
        SubItem subItem = new SubItem();
        String command = xmlPullParser.getAttributeValue(null, "command");
        String nameCN = xmlPullParser.getAttributeValue(null, "name_CN");
        String nameEN = xmlPullParser.getAttributeValue(null, "name_EN");
        String needTest = xmlPullParser.getAttributeValue(null, "needTest");

        subItem.setCommand(command);
        subItem.setDisplayNameCN(nameCN);
        subItem.setDisplayNameEN(nameEN);
        subItem.setNeedTest(Boolean.parseBoolean(needTest));

        return subItem;
    }

    public static List<MainItem> getTestItems(Context context) {
        List<MainItem> testItems = new ArrayList<>();
        try {
            XmlPullParser xmlPullParser = XmlPullParserHelper.getXmlPullParser(context);
            int mEventType = xmlPullParser.getEventType();
            MainItem mainItem = null;
            String tagName;
            while (mEventType != XmlPullParser.END_DOCUMENT) {
                if (mEventType == XmlPullParser.START_TAG) {
                    tagName = xmlPullParser.getName();
                    if (tagName.equals("MainItem")) {
                        mainItem = XmlPullParserHelper.parseToMainItem(xmlPullParser);
                    } else if (tagName.equals("SubItem")) {
                        SubItem subItem = XmlPullParserHelper.parseToSubItem(xmlPullParser);
                        if (mainItem != null) {
                            mainItem.addSubItem(subItem);
                        }
                    }
                } else if (mEventType == XmlPullParser.END_TAG) {
                    tagName = xmlPullParser.getName();
                    if (tagName.equals("MainItem")) {
                        testItems.add(mainItem);
                    }
                }
                mEventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testItems;
    }
}
