package com.yc.junior.english.setting.view.activitys;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseActivity;
import com.yc.junior.english.group.model.bean.TaskUploadInfo;
import com.yc.junior.english.setting.contract.CameraTaskContract;
import com.yc.junior.english.setting.presenter.CameraTaskPresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.blankj.utilcode.util.SDCardUtils;

import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;

/**
 * Created by wanglin  on 2017/12/8 16:11.
 */

public class CameraTaskActivity extends BaseActivity<CameraTaskPresenter> implements CameraTaskContract.View {

    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.rl_take_photo)
    RelativeLayout rlTakePhoto;
    @BindView(R.id.rl_task_album)
    RelativeLayout rlTaskAlbum;
    @BindView(R.id.iv_switch)
    ImageView mIvSwitch;
    @BindView(R.id.rl_exit)
    RelativeLayout rlExit;

    @BindView(R.id.iv_take_photo)
    ImageView mIvTakePhoto;
    private Camera c;

    private Camera.Size mBestPictureSize;
    private Camera.Size mBestPreviewSize;

    private boolean mIsSurfaceReady;

    public static final int REQUEST_CODE = 100;
    private static final int ACTION_CROP = 200;

    @Override
    public void init() {

        mPresenter = new CameraTaskPresenter(this, this);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new MyCallBack());
        initListener();


    }

    private void initListener() {
        RxView.clicks(rlTakePhoto).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                c.takePicture(null, null, mJepgCallback);
            }
        });
        RxView.clicks(rlTaskAlbum).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        RxView.clicks(mIvSwitch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isFlashlightOn()) {
                    closeShoudian();
                } else {
                    openShoudian();
                }

            }
        });
        RxView.clicks(rlExit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.post(new Runnable() {
            @Override
            public void run() {
                openCamera();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
    }

    private void closeCamera() {
        if (c == null) {
            return;
        }
        c.cancelAutoFocus();
        stopPreview();
        c.release();
        c = null;
    }

    private void stopPreview() {
        if (c != null) {
            c.stopPreview();
        }
    }

    @Override
    public int getLayoutId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_photograph_task;
    }

    @Override
    public void showUploadResult(TaskUploadInfo data) {

    }


    private class MyCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mIsSurfaceReady = true;
            startPreview();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            mIsSurfaceReady = false;
        }
    }


    private Camera.PictureCallback mJepgCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            String fileName = SDCardUtils.getSDCardPath() + UUID.randomUUID().toString() + ".jpg";
            File file = new File(fileName);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);

                fos.write(data);
                fos.flush();

                mPresenter.uploadFile(file, fileName, "");
//                mPresenter.uploadFile(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //拍照后重新开始预览


        }
    };

    private String savePhotoAlbum(byte[] data, String fileName) {
        //将图片保存至相册
        ContentResolver resolver = getContentResolver();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return MediaStore.Images.Media.insertImage(resolver, bitmap, fileName, "des");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (c != null) {
            c.stopPreview();
        }
        switch (requestCode) {
            case REQUEST_CODE:
                String imagePath = getImageAbsolutePath(CameraTaskActivity.this, data.getData());
                File file = new File(imagePath);
                Uri uri = Uri.fromFile(file);
//                cropImage(uri);
                mPresenter.uploadFile(file, imagePath, imagePath.substring(imagePath.lastIndexOf("/") + 1));
                break;

            case ACTION_CROP:
//                getContentResolver().openInputStream(cropUri);
                Uri cropUri = data.getData();
                File file1 = new File(cropUri.getPath());
                mPresenter.uploadFile(file1, cropUri.getPath(), "");

                break;
        }


//        Uri uri = data.getData();
//        if (uri != null) {
//            String path = uri.getPath();
//            File file = new File(path);
//            mPresenter.uploadFile(file, path, path.substring(path.lastIndexOf("/") + 1));
//        } else {
//            Bundle bundle = data.getExtras();
//            Bitmap photo = null;
//            if (bundle != null) {
//                photo = bundle.getParcelable("data");
//            }
//        }


    }

    public void openShoudian() {
        if (c != null) {
            //打开闪光灯
            Camera.Parameters parameter = c.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            c.setParameters(parameter);
        }
    }

    public void closeShoudian() {

        if (c != null) {
            //关闭闪光灯
            Camera.Parameters parameter = c.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            c.setParameters(parameter);

        }
    }

    /*    *
         * 是否开启了闪光灯
         * @return
         */
    public boolean isFlashlightOn() {
        try {
            Camera.Parameters parameters = c.getParameters();
            String flashMode = parameters.getFlashMode();
            if (flashMode.equals(android.hardware.Camera.Parameters.FLASH_MODE_TORCH)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    private void openCamera() {
        if (c == null) {
            try {
                c = Camera.open();
            } catch (RuntimeException e) {

                finish();
                return;
            }
        }

        final Camera.Parameters cameraParams = c.getParameters();
        cameraParams.setPictureFormat(ImageFormat.JPEG);
        cameraParams.setRotation(90);
        try {

            cameraParams.setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE);
        } catch (Exception e) {
        }
        List<int[]> list = cameraParams.getSupportedPreviewFpsRange();

        cameraParams.setPreviewFpsRange(list.get(0)[0], list.get(0)[1]);

        // 短边比长边
        final float ratio = (float) surfaceView.getWidth() / surfaceView.getHeight();

        // 设置pictureSize
        List<Camera.Size> pictureSizes = cameraParams.getSupportedPictureSizes();
        if (mBestPictureSize == null) {
            mBestPictureSize = findBestPictureSize(pictureSizes, cameraParams.getPictureSize(), ratio);
        }
        final Camera.Size pictureSize = mBestPictureSize;
        cameraParams.setPictureSize(pictureSize.width, pictureSize.height);

        // 设置previewSize
        List<Camera.Size> previewSizes = cameraParams.getSupportedPreviewSizes();
        if (mBestPreviewSize == null) {
            mBestPreviewSize = findBestPreviewSize(previewSizes, cameraParams.getPreviewSize(),
                    pictureSize, ratio);
        }
        final Camera.Size previewSize = mBestPreviewSize;
        cameraParams.setPreviewSize(previewSize.width, previewSize.height);


        try {
            c.setParameters(cameraParams);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        if (mIsSurfaceReady) {
            startPreview();
        }
    }


    /**
     * 找到短边比长边大于于所接受的最小比例的最大尺寸
     *
     * @param sizes       支持的尺寸列表
     * @param defaultSize 默认大小
     * @param minRatio    相机图片短边比长边所接受的最小比例
     * @return 返回计算之后的尺寸
     */
    private Camera.Size findBestPictureSize(List<Camera.Size> sizes, Camera.Size defaultSize, float minRatio) {
        final int MIN_PIXELS = 320 * 480;

        sortSizes(sizes);

        Iterator<Camera.Size> it = sizes.iterator();
        while (it.hasNext()) {
            Camera.Size size = it.next();
            //移除不满足比例的尺寸
            if ((float) size.height / size.width <= minRatio) {
                it.remove();
                continue;
            }
            //移除太小的尺寸
            if (size.width * size.height < MIN_PIXELS) {
                it.remove();
            }
        }

        // 返回符合条件中最大尺寸的一个
        if (!sizes.isEmpty()) {
            return sizes.get(0);
        }
        // 没得选，默认吧
        return defaultSize;
    }

    /**
     * @param sizes
     * @param defaultSize
     * @param pictureSize 图片的大小
     * @param minRatio    preview短边比长边所接受的最小比例
     * @return
     */
    private Camera.Size findBestPreviewSize(List<Camera.Size> sizes, Camera.Size defaultSize,
                                            Camera.Size pictureSize, float minRatio) {
        final int pictureWidth = pictureSize.width;
        final int pictureHeight = pictureSize.height;
        boolean isBestSize = (pictureHeight / (float) pictureWidth) > minRatio;
        sortSizes(sizes);

        Iterator<Camera.Size> it = sizes.iterator();
        while (it.hasNext()) {
            Camera.Size size = it.next();
            if ((float) size.height / size.width <= minRatio) {
                it.remove();
                continue;
            }

            // 找到同样的比例，直接返回
            if (isBestSize && size.width * pictureHeight == size.height * pictureWidth) {
                return size;
            }
        }

        // 未找到同样的比例的，返回尺寸最大的
        if (!sizes.isEmpty()) {
            return sizes.get(0);
        }

        // 没得选，默认吧
        return defaultSize;
    }

    private static void sortSizes(List<Camera.Size> sizes) {
        Collections.sort(sizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                return b.height * b.width - a.height * a.width;
            }
        });
    }

    private void startPreview() {
        if (c == null) {
            return;
        }
        try {
            c.setPreviewDisplay(surfaceView.getHolder());
            c.setDisplayOrientation(90);
            c.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, ACTION_CROP);
    }

    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
