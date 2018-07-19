
package com.hhcx.aoptestframe.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hhcx.aoptestframe.activity.AppApplication;
import com.hhcx.aoptestframe.bean.MainItem;
import com.hhcx.aoptestframe.bean.TestItem;
import com.hhcx.aoptestframe.bean.TestResultItem;
import com.hhcx.aoptestframe.util.SystemProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class DBHelper {

    private static final String TAG = "DBHelper";
    private static String databaseFilePath;

    private SQLiteDatabase database;

    // 数据库名
    private static DBHelper instance;
    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
            databaseFilePath = AppApplication.localPath + "/test_result.db";
        }
        return instance;
    }

    public void initDatabase() {
        try {
            Log.e("123", databaseFilePath);
            database = SQLiteDatabase.openOrCreateDatabase(databaseFilePath, null);
            database.execSQL("DROP TABLE IF EXISTS " + SqlConstants.TABLE_NAME);
            database.execSQL(SqlConstants.SQL_CREATE_TABLE);
            database.beginTransaction();
            for (TestItem testItem : AppApplication.testItems) {
                MainItem mainItem = (MainItem) testItem;
                insertItem(mainItem.getCommand(), mainItem.getCommand(), SqlConstants.RESULT_NONE, "");
                Log.d("123", "mainItem:" + mainItem.toString());
//                List<SubItem> list = mainItem.getSubItems();
//                for (SubItem subItem : mainItem.getSubItems()) {
//                    insertItem(mainItem.getCommand(), subItem.getCommand(), SqlConstants.RESULT_NONE, "");
//                }
            }
            database.setTransactionSuccessful();
            database.endTransaction();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean queryItem(String mainItem, String subItem) {
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns,
                SqlConstants.COLUMN_ITEM_MAIN + " = '" + mainItem + "' and "
                        + SqlConstants.COLUMN_ITEM_SUB + " = '" + subItem + "'", null, null, null,
                null);
        boolean isExist = cursor.getCount() != 0;
        cursor.close();
        return isExist;
    }



    /**
     * 查询列表项数量
     *
     * @return count
     */
    public int queryItemCount() {
        int count;
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns,
                null, null, null, null, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public synchronized int queryRightItemCount() {
        int count;
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns,
                SqlConstants.COLUMN_ITEM_RESULT + " = '" + SqlConstants.RESULT_SUCCESS + "'", null, null, null,
                null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public synchronized int queryWrongItemCount() {
        int count;
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_ID, SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns,
                SqlConstants.COLUMN_ITEM_RESULT + " = '" + SqlConstants.RESULT_FAILED + "'", null, null, null,
                null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public synchronized int queryNoneItemCount() {
        int count;
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns,
                SqlConstants.COLUMN_ITEM_RESULT + " = '" + SqlConstants.RESULT_NONE + "'", null, null, null,
                null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public String queryItemResult(String mainItem, String subItem) {
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns,
                SqlConstants.COLUMN_ITEM_MAIN + " = '" + mainItem + "' and "
                        + SqlConstants.COLUMN_ITEM_SUB + " = '" + subItem + "'", null, null, null,
                null);
        String result = SqlConstants.RESULT_NONE;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(0);
        }
        cursor.close();
        return result;
    }

    public synchronized String queryItemResultByMainItem(String mainItem) {
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns,
                SqlConstants.COLUMN_ITEM_MAIN + " = '" + mainItem + "' and "
                        + SqlConstants.COLUMN_ITEM_SUB + " = '" + "autoTest'", null, null, null,
                null);
        String result = SqlConstants.RESULT_NONE;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            result = cursor.getString(0);
        }
        cursor.close();
        return result;
    }

    public void updateItem(String mainItem, String subItem, String testResult, String time) {
        ContentValues values = new ContentValues();
        values.put(SqlConstants.COLUMN_ITEM_RESULT, testResult);
        values.put(SqlConstants.COLUMN_ITEM_TESTTIME, time);
        database.update(SqlConstants.TABLE_NAME, values,
                SqlConstants.COLUMN_ITEM_MAIN + " = '" + mainItem + "' and "
                        + SqlConstants.COLUMN_ITEM_SUB + " = '" + subItem + "'", null);
    }

    public void insertItem(String mainItem, String subItem, String testResult, String time) {
        if (testResult == null) {
            testResult = "0";
        }
        ContentValues values = new ContentValues();
        values.put(SqlConstants.COLUMN_ITEM_RESULT, testResult);
        values.put(SqlConstants.COLUMN_ITEM_MAIN, mainItem);
        values.put(SqlConstants.COLUMN_ITEM_SUB, subItem);
        values.put(SqlConstants.COLUMN_ITEM_TESTTIME, time);
        database.insert(SqlConstants.TABLE_NAME, null, values);
    }

    /**
     * 保存测试结果
     */
    public synchronized void saveChangedItem(String mainItem, String subItem, String testResult) {
        subItem = SqlConstants.SUB_ITEM_DEFAULT;
        String time = SystemProperties.getTimeWithFormat();
        if (queryItem(mainItem, subItem)) {
            updateItem(mainItem, subItem, testResult, time);
        } else {
            insertItem(mainItem, subItem, testResult, time);
        }
    }

    public synchronized void clear() {
        deleteDatabaseFile();
        initDatabase();
    }

    private void deleteDatabaseFile() {
        File db = new File(databaseFilePath);
        db.delete();
    }

    public synchronized String getAllTestResult() {
        Log.e(TAG, "getAllTestResult");
        JSONArray allTestResult = null;
        String[] columns = new String[] {
                SqlConstants.COLUMN_ITEM_ID, SqlConstants.COLUMN_ITEM_MAIN,
                SqlConstants.COLUMN_ITEM_RESULT
        };
        Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns, null, null, null, null,
                null);
        allTestResult = convertToJson(cursor);
        cursor.close();
        Log.e(TAG, "allTestResult.length = " + allTestResult.toString().length()
                + " allTestResult = "
                + allTestResult);
        return allTestResult.toString();
    }

    private static final String EVENT_NAME = "n";
    private static final String EVENT_RESULT = "r";
    private static final String EVENT_DESCRIPTION = "d";

    private JSONArray convertToJson(Cursor allTestResult) {
        JSONArray resultArray = new JSONArray();
        // resultArray.
        while (allTestResult.moveToNext()) {
            JSONObject testResult = new JSONObject();
            int index = allTestResult.getInt(0) - 1;
            try {
                testResult.put(EVENT_NAME, allTestResult.getString(1));
                testResult.put(EVENT_RESULT, allTestResult.getString(2));
                testResult.put(EVENT_DESCRIPTION, AppApplication.testItems.get(index).getCommand());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultArray.put(testResult);
        }
        return resultArray;
    }

    public String[] queryTestResultFromDB() {
        String isAllPass = "1";
        String[] columns = {SqlConstants.COLUMN_ITEM_MAIN, SqlConstants.COLUMN_ITEM_RESULT, SqlConstants.COLUMN_ITEM_TESTTIME};
        ArrayList<TestResultItem> items = new ArrayList<>();
        try {
            Cursor cursor = database.query(SqlConstants.TABLE_NAME, columns, null, null, null,
                    null, null);
            while (cursor.moveToNext()) {
                TestResultItem item = new TestResultItem();
                item.setItemName(cursor.getString(0));
                item.setItemTime(cursor.getString(2));
                String result = cursor.getString(1);
                if (result.equalsIgnoreCase("0")) {
                    isAllPass = "0";
                    item.setItemResult("NoTest");
                } else if (result.equalsIgnoreCase("1")) {
                    isAllPass = "0";
                    item.setItemResult("Fail");
                } else if (result.equalsIgnoreCase("2")) {
                    item.setItemResult("Pass");
                }
                items.add(item);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (TestResultItem item : items) {

            sb.append(item.toString());
        }
        return new String[] {isAllPass, sb.toString()};
    }

    private void openDatabase() {
        try {
            database = SQLiteDatabase.openDatabase(databaseFilePath, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
            database = null;
        }
    }

    public void closeDatabase() {
        try {
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
