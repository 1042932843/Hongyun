package com.dyhl.hongyun.searchhistory.model;

import com.dyhl.hongyun.searchhistory.bean.SearchHistoryModel;

import java.util.ArrayList;

/**
 * Created by Zellerpooh on 17/1/18.
 */

public interface OnSearchListener {
    void onSortSuccess(ArrayList<SearchHistoryModel> results);

    void searchSuccess(String value);
}
