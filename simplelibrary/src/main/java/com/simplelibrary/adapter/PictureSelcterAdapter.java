package com.simplelibrary.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.simplelibrary.R;
import com.simplelibrary.dialog.ChoosePhotoDialog;
import com.simplelibrary.utils.ChoosePhotoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangdang on 2018/11/24.
 */

public class PictureSelcterAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<PhotoEntity> mChooseList = new ArrayList<>();//原始图片


    private FragmentActivity mFragmentActivity;

    private int layoutResId;
    private int mDeleteViewId;

    public int selectLimit;//限制数量
    private ChoosePhotoDialog mPhotoDialog;
    private boolean hasOrginalFile = false;



    public PictureSelcterAdapter(FragmentActivity mFragmentActivity) {
        this(mFragmentActivity, R.layout.item_photo_sel_default,null);
    }


    public PictureSelcterAdapter(FragmentActivity mFragmentActivity, int layoutResId, ChoosePhotoDialog  dialog) {
        this.mFragmentActivity = mFragmentActivity;
        mPhotoDialog = new ChoosePhotoDialog();
        this.layoutResId = layoutResId;
        if (dialog == null){
            mPhotoDialog = new ChoosePhotoDialog();
        }else {
             mPhotoDialog=dialog;
        }

        mPhotoDialog.setOnChooseListener(new ChoosePhotoUtils.OnChooseListener() {
            @Override
            public void onChoose(File originalFile, File compressFile) {
                mChooseList.add(new PhotoEntity(hasOrginalFile ? originalFile : compressFile));
                notifyDataSetChanged();
            }
        });

        mPhotoDialog.hasBottomUP(true);
    }


    public List<PhotoEntity> getPictureList() {
        return mChooseList;
    }

    public void setDeleteViewId(@IdRes int deleteViewId) {
        mDeleteViewId = deleteViewId;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mChooseList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }



    public void setSelectLimit(@IntRange(from = 0, to = 20) int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public void setAspectXY(int x, int y) {
        mPhotoDialog.setAspectXY(x, y);
    }


    protected void convertPhoto(BaseViewHolder helper, PhotoEntity item) {

    }

    ;

    protected void convertAdd(BaseViewHolder helper) {
    }

    ;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutResId == R.layout.item_photo_sel_default && mDeleteViewId == 0) {

            mDeleteViewId = R.id.iv_photo_delete_default;
        }
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder helper, final int position) {
        View deleteView = helper.getView(mDeleteViewId);
        if (helper.getAdapterPosition() < mChooseList.size() || mChooseList.size() == selectLimit) {
            helper.itemView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
            PhotoEntity entity = mChooseList.get(position);

            if (deleteView != null) {
                deleteView.setVisibility(View.VISIBLE);
                deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeData(position);
                    }
                });
            }
            if (layoutResId == R.layout.item_photo_sel_default) {
                Glide.with(mFragmentActivity).load(entity.isFile() ? entity.getFile() : entity.getUrl()).into((ImageView) helper.getView(R.id.iv_photo_default));
            }
            convertPhoto(helper, entity);
        } else {

            helper.itemView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mPhotoDialog.show(mFragmentActivity);

                        }
                    });
            if (deleteView != null) {
                deleteView.setVisibility(View.GONE);
            }
            if (layoutResId == R.layout.item_photo_sel_default) {
                helper.setImageResource(R.id.iv_photo_default, R.mipmap.ic_choose_photo_add);
            }
            convertAdd(helper);
        }

    }

    @Override
    public int getItemCount() {
        return mChooseList.size() < selectLimit ? mChooseList.size() + 1 : mChooseList.size();
    }

    public static class PhotoEntity {
        private String mUrl;
        private File mFile;
        private boolean isFile;

        public PhotoEntity(String url) {
            mUrl = url;
        }

        public PhotoEntity(File file) {
            mFile = file;
            isFile = true;
        }

        public String getUrl() {
            return mUrl;
        }

        public File getFile() {
            return mFile;
        }

        public boolean isFile() {
            return isFile;
        }
    }
}