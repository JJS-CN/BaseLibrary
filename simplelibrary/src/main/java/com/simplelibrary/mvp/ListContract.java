package com.simplelibrary.mvp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.simplelibrary.http.IBaseEntity;

import io.reactivex.Observable;

/**
 * 说明：
 * Created by jjs on 2018/11/28
 */

public interface ListContract {
    interface View extends IContract.IView {
        void loadItemData(BaseViewHolder holder, int position, Object data);

        int getItemRes();

        void reLoadData();

        SwipeRefreshLayout getSwipeRefreshLayout();

        RecyclerView getRecyclerView();
    }

    interface Persenter extends IContract.IPersenter {
        void bindRecycler();

        <T extends IBaseEntity> void loadData(Observable<T> observable);

        void refreshData();

        int getPage();

        int getPageSize();

        void setOnBeforeLoadDataListener(ListPersenter.OnBeforeLoadDataListener onBeforeLoadDataListener);

        void setOnAfterLoadDataListener(ListPersenter.OnAfterLoadDataListener onAfterLoadDataListener);

        BaseQuickAdapter getAdapter();
    }
}
