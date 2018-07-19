
package com.hhcx.aoptestframe.activity;

import android.app.Application;

import com.hhcx.aoptestframe.bean.MainItem;
import com.hhcx.aoptestframe.helper.XmlPullParserHelper;
import com.hhcx.aoptestframe.util.SystemProperties;
import com.hhcx.frame.base.ActionManager;
import com.hhcx.frame.impl.ActionContainerImpl;

import java.util.List;
import java.util.Locale;

public class AppApplication extends Application {
    
    /** 外接设备的类型
     */
    public static String model = "";
    public static List<MainItem> testItems;

    public static final int LANGUAGE_TYPE_CN = 0;
    public static final int LANGUAGE_TYPE_OTHER = 1;
    public static int languageType = LANGUAGE_TYPE_CN;
    public static String localPath;

    @Override
    public void onCreate() {
        super.onCreate();
        initParam();
        ActionManager.initActionContainer(new ActionContainerImpl(this));
    }

    private void initParam() {
        model = SystemProperties.getModel();
        checkLanguage();
        testItems = XmlPullParserHelper.getTestItems(this);
        localPath = this.getFilesDir().getPath();
    }

    private void checkLanguage(){
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            languageType = LANGUAGE_TYPE_CN;
        } else {
            languageType = LANGUAGE_TYPE_OTHER;
        }
    }
}
