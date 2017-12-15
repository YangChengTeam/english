package com.yc.english.group.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by wanglin  on 2017/12/14 16:39.
 * 拍照，从相册获取图片并裁剪
 */

public class PhotoUtils {

    public static final int CHOOSE_BIG_PICTURE = 100;

    public static void takePhotoFromAlbum(Activity activity) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);

        intent.setType("image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 2);

        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 600);

        intent.putExtra("outputY", 300);

        intent.putExtra("scale", true);

        intent.putExtra("return-data", true);


        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true); // no face detection

        activity.startActivityForResult(intent, CHOOSE_BIG_PICTURE);
    }
}
