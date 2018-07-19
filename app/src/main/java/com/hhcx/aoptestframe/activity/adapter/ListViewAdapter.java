
package com.hhcx.aoptestframe.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhcx.aoptestframe.R;
import com.hhcx.aoptestframe.bean.MainItem;
import com.hhcx.aoptestframe.helper.DBHelper;
import com.hhcx.aoptestframe.helper.SqlConstants;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {


    private List<MainItem> testItems;
    private LayoutInflater inflater;
    private DBHelper dbHelper;
    private int mainItemIndex = INDEX_NONE;
    public static final int INDEX_NONE = -1;

    public ListViewAdapter(Context context, List<MainItem> testItems) {
        this.testItems = testItems;
        this.inflater = LayoutInflater.from(context);
        dbHelper = DBHelper.getInstance();

    }

    @Override
    public int getCount() {
        int count;
        if (mainItemIndex <= INDEX_NONE) {
            count = testItems.size();
        } else {
            count = testItems.get(mainItemIndex).getSubItems().size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object item;
        if (mainItemIndex <= INDEX_NONE) {
            item = testItems.get(position);
        } else {
            item = testItems.get(mainItemIndex).getSubItem(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentview, ViewGroup arg2) {
        Holder holder;
        if (contentview == null) {
            contentview = inflater.inflate(R.layout.item_main, null);
            holder = new Holder();
            holder.txtResult = (TextView) contentview.findViewById(R.id.tv_result);
            holder.txtName = (TextView) contentview.findViewById(R.id.tv_name);
            contentview.setTag(holder);
        } else {
            holder = (Holder) contentview.getTag();
        }

        setDisplayedSignature(position, holder.txtResult);
        setDisplayedButton(position, holder.txtName);

        return contentview;
    }

    public static class Holder {
        TextView txtName;
        TextView txtResult;
    }


    private void setDisplayedSignature(int position, TextView txtSignature) {
        String testResult = getTestResult(position);
        switch (testResult) {
            case SqlConstants.RESULT_SUCCESS:
                txtSignature.setText("√");
                txtSignature.setTextColor(Color.rgb(0, 0, 0));
                break;
            case SqlConstants.RESULT_FAILED:
                txtSignature.setText("X");
                txtSignature.setTextColor(Color.rgb(255, 0, 0));
                break;
            case SqlConstants.RESULT_EXCEPTION:
                txtSignature.setText("O");
                txtSignature.setTextColor(Color.rgb(255, 255, 255));
                break;
            default:
                txtSignature.setText("");
                txtSignature.setTextColor(Color.rgb(0, 0, 0));
                break;
        }
    }

    private void setDisplayedButton(int position, TextView txtButton) {
        MainItem mainItem = getMainItem(position);
        if (mainItemIndex <= INDEX_NONE) {
            txtButton.setText(mainItem.getDisplayName());
            txtButton.setTag(mainItem.getCommand());
        } else {
            txtButton.setText(mainItem.getSubItem(position).getDisplayName());
            txtButton.setTag(mainItem.getSubItem(position).getCommand());
        }
    }

    private String getTestResult(int position) {
        String testResult;
        MainItem mainItem = getMainItem(position);
        if (mainItemIndex <= INDEX_NONE) {
            testResult = dbHelper.queryItemResultByMainItem(mainItem.getCommand());
        } else {
            if (mainItem.getSubItem(position).isNeedTest()) {
                testResult = dbHelper.queryItemResult(mainItem.getCommand(),
                        mainItem.getSubItem(position).getCommand());
            } else {
                testResult = SqlConstants.RESULT_NONE;
            }
        }
        return testResult;
    }

    private MainItem getMainItem(int position) {
        MainItem mainItem;
        if (mainItemIndex <= INDEX_NONE) {
            mainItem = testItems.get(position);
        } else {
            mainItem = testItems.get(mainItemIndex);
        }
        return mainItem;
    }


    public void refreshView(int index) {
        mainItemIndex = index;
        notifyDataSetChanged();
    }
}
