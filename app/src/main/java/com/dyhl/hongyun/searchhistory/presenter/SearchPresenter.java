package com.dyhl.hongyun.searchhistory.presenter;

import android.content.Context;

/**
 * Created by Zellerpooh on 17/1/18.
 */

public interface SearchPresenter {

    void remove(String key);

    void clear();

    void sortHistory();

    void search(String value);
}
