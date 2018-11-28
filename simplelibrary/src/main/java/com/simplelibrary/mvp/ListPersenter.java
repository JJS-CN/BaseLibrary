package com.simplelibrary.mvp;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.simplelibrary.http.BaseObserver;
import com.simplelibrary.http.IBaseEntity;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 说明：
 * Created by jjs on 2018/11/28
 */

public class ListPersenter extends BasePersenter<ListContract.View, IContract.IModel> implements ListContract.Persenter, SwipeRefreshLayout.OnRefreshListener {
    private int pageNo = 1;
    private int pageSize = 10;
    private SwipeRefreshLayout mSwipe;
    private RecyclerView mRv;
    private BaseQuickAdapter mAdapter;
    private Disposable mDisposable;
    private OnBeforeLoadDataListener mOnBeforeLoadDataListener;
    private OnAfterLoadDataListener mOnAfterLoadDataListener;
    private boolean isBind = false;

    public ListPersenter(ListContract.View view) {
        super(view);
    }

    @Override
    protected IContract.IModel createModel() {
        return null;
    }

    @Override
    public void bindRecycler() {
        if (isBind) {
            return;
        }
        isBind = true;
        this.mSwipe = mView.getSwipeRefreshLayout();
        this.mRv = mView.getRecyclerView();
        if (mRv == null) {
            throw new NullPointerException("SRvPersenter:  RecyclerView is Null");
        } else {
            if (mRv.getLayoutManager() == null) {
                mRv.setLayoutManager(new LinearLayoutManager(mRv.getContext()));
            }
            if (mRv.getAdapter() == null) {
                mAdapter = new BaseQuickAdapter<Object, BaseViewHolder>(mView.getItemRes(), null) {

                    @Override
                    protected void convert(BaseViewHolder helper, Object item) {
                        if (mView != null)
                            mView.loadItemData(helper, helper.getAdapterPosition(), item);
                    }
                };
                mRv.setAdapter(mAdapter);
            } else {
                mAdapter = (BaseQuickAdapter) mRv.getAdapter();
            }
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    ++pageNo;
                    if (mView != null)
                        mView.reLoadData();
                }
            }, mRv);
            mAdapter.disableLoadMoreIfNotFullPage();
            if (mSwipe != null) {
                mSwipe.setColorSchemeColors(Color.parseColor("#FA9E6F"), Color.parseColor("#FEF193"), Color.parseColor("#C0FBCF"), Color.parseColor("#C5D1FB"));
                mSwipe.setOnRefreshListener(this);
                mSwipe.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipe.setRefreshing(true);
                        onRefresh();
                    }
                });
            } else {
                onRefresh();
            }
        }
    }

    @Override
    public <T extends IBaseEntity> void loadData(Observable<T> observable) {
        if (observable != null) {
            observable.subscribe(new BaseObserver<T>(mView) {
                @Override
                protected void onSuccess(T t) {
                    if (mOnAfterLoadDataListener != null) {
                        mOnAfterLoadDataListener.onResult(t);
                    }
                    if (pageNo == 1) {
                        mAdapter.setNewData(t == null ? null : t.getList());
                    } else {
                        if (t != null) {
                            mAdapter.addData(t.getList());
                        }
                    }
                    if (t != null && t.getList() != null && t.getList().size() >= pageSize) {
                        mAdapter.loadMoreComplete();
                    } else {
                        mAdapter.loadMoreEnd();
                    }
                    if (mOnBeforeLoadDataListener != null) {
                        mOnBeforeLoadDataListener.onResult(t);
                    }
                }

                @Override
                public void onSubscribe(Disposable d) {
                    super.onSubscribe(d);
                    mDisposable = d;
                }

                @Override
                protected void onError(int status, String msg) {
                    super.onError(status, msg);
                    if (pageNo > 1) {
                        pageNo--;
                    }
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if (mSwipe != null)
                        mSwipe.setRefreshing(false);
                    mAdapter.setEnableLoadMore(true);
                }
            });
        }
    }

    @Override
    public void refreshData() {
        pageNo = 1;
        mView.reLoadData();
    }

    @Override
    public int getPage() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setOnBeforeLoadDataListener(OnBeforeLoadDataListener onBeforeLoadDataListener) {
        mOnBeforeLoadDataListener = onBeforeLoadDataListener;
    }

    @Override
    public void setOnAfterLoadDataListener(OnAfterLoadDataListener onAfterLoadDataListener) {
        mOnAfterLoadDataListener = onAfterLoadDataListener;
    }


    @Override
    public BaseQuickAdapter getAdapter() {
        return mAdapter;
    }


    @Override
    public void onRefresh() {
        pageNo = 1;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mAdapter.setEnableLoadMore(false);
        mView.reLoadData();

    }

    public interface OnBeforeLoadDataListener {
        void onResult(IBaseEntity resp);
    }

    public interface OnAfterLoadDataListener {
        void onResult(IBaseEntity resp);
    }

}
