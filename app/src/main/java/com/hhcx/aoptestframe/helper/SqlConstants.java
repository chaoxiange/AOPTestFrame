
package com.hhcx.aoptestframe.helper;

public class SqlConstants {

    public static final String TABLE_NAME = "AopTestResult";

    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "("
            + "item_id integer primary key autoincrement"
            + ", item_main text not null"
            + ", item_sub text"
            + ", item_result text not null"
            + ", item_testTime text"
            + ", item_reserved text);";

    public static final String SUB_ITEM_DEFAULT = "autoTest";
    // column
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_MAIN = "item_main";
    public static final String COLUMN_ITEM_SUB = "item_sub";
    public static final String COLUMN_ITEM_RESULT = "item_result";
    public static final String COLUMN_ITEM_TESTTIME = "item_testTime";
    public static final String COLUMN_ITEM_RESERVED = "item_reserved";

    public static final String RESULT_SUCCESS = "2";
    public static final String RESULT_FAILED = "1";
    public static final String RESULT_EXCEPTION = "-1";
    public static final String RESULT_NONE = "0";
}
