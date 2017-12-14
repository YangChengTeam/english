package com.yc.english.setting.view.activitys;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SDCardUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.AvatarHelper;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.setting.contract.CameraTaskContract;
import com.yc.english.setting.presenter.CameraTaskPresenter;

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
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent, 1);
                AvatarHelper.openAlbum(CameraTaskActivity.this);
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
        Intent intent = new Intent(this, TaskShowActivity.class);
        intent.putExtra("data", NetConstant.baidu_url + data.getFile_path());
        startActivity(intent);
        finish();
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

//                mPresenter.uploadFile(file, fileName, "");
                mPresenter.uploadFile(file);

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
        Uri uri = data.getData();
        if (uri != null) {
            String path = uri.getPath();
            File file = new File(path);
            mPresenter.uploadFile(file, path, path.substring(path.lastIndexOf("/") + 1));
        }


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


}
