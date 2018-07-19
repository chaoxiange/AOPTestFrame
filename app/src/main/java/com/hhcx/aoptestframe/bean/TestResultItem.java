package com.hhcx.aoptestframe.bean;

/**
 * Created by gecx on 18-4-12.
 */

public class TestResultItem {

    private String itemName;
    private String itemResult;
    private String itemTime;

    public String getItemName() {
        return itemName;
    }

    public String getItemResult() {
        return itemResult;
    }

    public String getItemTime() {
        return itemTime;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemResult(String itemResult) {
        this.itemResult = itemResult;
    }

    public void setItemTime(String itemTime) {
        this.itemTime = itemTime;
    }

    @Override
    public String toString() {
        return itemName + " = " + itemResult + " , " + itemTime + ";\n";
    }
}
