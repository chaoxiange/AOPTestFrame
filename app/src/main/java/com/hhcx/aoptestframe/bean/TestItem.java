
package com.hhcx.aoptestframe.bean;

import com.hhcx.aoptestframe.activity.AppApplication;

public class TestItem {
    private String command;
    private String displayNameCN;
    private String displayNameEN;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDisplayName() {
        if (AppApplication.languageType == AppApplication.LANGUAGE_TYPE_CN) {
            return displayNameCN;
        } else {
            return displayNameEN;
        }
    }

    public void setDisplayNameCN(String displayNameCN) {
        this.displayNameCN = displayNameCN;
    }

    public void setDisplayNameEN(String displayNameEN) {
        this.displayNameEN = displayNameEN;
    }

    @Override
    public String toString() {
        return String.format("command = %s, displayCN = %s, displyEN = %s", command, displayNameCN,
                displayNameEN);
    }
}
