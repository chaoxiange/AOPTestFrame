
package com.hhcx.frame.impl;

import android.content.Context;
import android.util.Log;

import com.hhcx.aoptestframe.R;
import com.hhcx.aoptestframe.activity.AppApplication;
import com.hhcx.aoptestframe.bean.MainItem;
import com.hhcx.frame.base.ActionContainer;

public class ActionContainerImpl extends ActionContainer {
    private static final String TAG = "ActionContainerImpl";
    private Context context;

    public ActionContainerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void initActions() {
        for (MainItem mainItem : AppApplication.testItems) {
            try {
                String classPath = getClassPath(mainItem);
                Class clazz = Class.forName(classPath);
                addAction(mainItem.getCommand(), clazz, true);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Can't find this action");
            }
        }
    }

    private String getClassPath(MainItem mainItem) {
        String classPath;
        if (mainItem.isUnique()) {
            classPath = mainItem.getPackageName();
        } else {
            classPath = context.getResources().getString(R.string.action_package_name)
                    + mainItem.getCommand();
        }
        return classPath;
    }

}
