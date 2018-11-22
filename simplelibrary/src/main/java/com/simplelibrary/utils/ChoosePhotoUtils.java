package com.simplelibrary.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.Utils;
import com.simplelibrary.annotation.NeedPermission;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public class ChoosePhotoUtils {
    private static ChoosePhotoUtils sInstance;

    public final static int Request_Camera = 1;
    public final static int Request_Album = 2;
    private int Request_Type;
    private int mAspectX, mAspectY;

    private OnChooseListener mChooseListener;

    public ChoosePhotoUtils() {
        sInstance = this;
    }

    public ChoosePhotoUtils setChooseListener(OnChooseListener chooseListener) {
        mChooseListener = chooseListener;
        return this;
    }

    public ChoosePhotoUtils setAspectXY(int mAspectX, int mAspectY) {
        this.mAspectX = mAspectX;
        this.mAspectY = mAspectY;
        return this;
    }

    public void requestCamera() {
        //启动相机
        Request_Type = Request_Camera;
        startChooseActivity();
    }

    public void requestAlbum() {
        //启动相册
        Request_Type = Request_Album;
        startChooseActivity();
    }

    private void startChooseActivity() {
        ChoosePhotoActivity.start(Utils.getApp());
    }

    public interface OnChooseListener {
        void onChoose(File originalFile, File compressFile);
    }

    @NeedPermission({PermissionConstants.CAMERA, PermissionConstants.STORAGE})
    public static class ChoosePhotoActivity extends AppCompatActivity {
        private File mOriginalFile;//原始地址

        public static void start(final Context context) {
            Intent starter = new Intent(context, ChoosePhotoActivity.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(starter);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            if (sInstance == null) {
                super.onCreate(savedInstanceState);
                Log.e("ChoosePhotoUtils", "request ChoosePhoto failed");
                finish();
                return;
            }
            super.onCreate(savedInstanceState);
            if (sInstance.Request_Type == ChoosePhotoUtils.Request_Camera) {
                openCamera();
            } else {
                openAlbum();
            }
        }

        /**
         * 图片选择完成
         * OriginalFile 原图路径
         *
         * @param compressFile 压缩之后的图片路径
         */
        private void chooseSuccess(File compressFile) {
            if (sInstance.mChooseListener != null) {
                sInstance.mChooseListener.onChoose(mOriginalFile, compressFile);
            }
            finish();
        }

        /***  开启裁剪  ***/
        private void openCrop(Uri originalUri, @IntRange(from = 1) int ratioX, @IntRange(from = 1) int ratioY) {
            UCrop.of(originalUri, Uri.fromFile(getImageFile()))
                    .withAspectRatio(ratioX, ratioY)
                    .withMaxResultSize(1080, 1080 * ratioY / ratioX)
                    .start(this);
        }

        //获得照片的输出保存Uri
        private File getImageFile() {
            File file = new File(ChoosePhotoActivity.this.getExternalCacheDir(), "/image/" + System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            return file;
        }

        /***  开启相机  ***/
        private void openCamera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //指定拍照图片地址
            mOriginalFile = getImageFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file2Uri(mOriginalFile));
            startActivityForResult(intent, ChoosePhotoUtils.Request_Camera);


        }

        /***  开启相册  ***/
        private void openAlbum() {
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            //根据版本号不同使用不同的Action
            if (Build.VERSION.SDK_INT < 19) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
            } else {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            }
            startActivityForResult(intent, ChoosePhotoUtils.Request_Album);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case ChoosePhotoUtils.Request_Camera:   //拍照
                        startCompress();
                        break;
                    case ChoosePhotoUtils.Request_Album:   //选择照片
                        mOriginalFile = new File(getPath(this, data.getData()));
                        startCompress();
                        break;
                    case UCrop.REQUEST_CROP: //裁剪
                        Uri resultUri = UCrop.getOutput(data);
                        chooseSuccess(new File(getPath(this, resultUri)));
                        break;
                }
            } else {
                if (resultCode == UCrop.RESULT_ERROR) {
                    Throwable cropError = UCrop.getError(data);
                    cropError.printStackTrace();
                }
                finish();
            }
        }

        /***  开始压缩  ***/
        private void startCompress() {
            String path = ChoosePhotoActivity.this.getExternalCacheDir() + "/image/";
            File file = new File(path);
            file.mkdirs();
            Luban.with(ChoosePhotoActivity.this)
                    .load(mOriginalFile)
                    .ignoreBy(200)
                    .setTargetDir(path)
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            //执行裁剪
                            if (sInstance.mAspectX > 0 && sInstance.mAspectY > 0) {
                                openCrop(getImageContentUri(file), sInstance.mAspectX, sInstance.mAspectY);
                            } else {
                                chooseSuccess(file);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            finish();
                        }
                    }).launch();
        }


        private Uri getImageContentUri(File imageFile) {
            String filePath = imageFile.getAbsolutePath();
            Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                cursor.close();
                return Uri.withAppendedPath(baseUri, "" + id);
            }
            if (cursor != null) {
                cursor.close();
            }
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }

        }

        //不知道与上一个有什么区别，但是乱用会出错
        private Uri file2Uri(File file) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(ChoosePhotoActivity.this, ChoosePhotoActivity.this.getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            return uri;
        }


        /**
         * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
         */
        @SuppressLint("NewApi")
        public String getPath(final Context context, final Uri uri) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        actualimagecursor.moveToFirst();
                        String img_path = actualimagecursor.getString(actual_image_column_index);
                        File file = new File(img_path);
                    }
                }
                // DownloadsProvider
                else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }


        private String getDataColumn(Context context, Uri uri, String selection,
                                     String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }
    }

}
