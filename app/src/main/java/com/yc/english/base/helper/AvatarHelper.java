package com.yc.english.base.helper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;
import com.yc.english.base.view.BaseActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by zhangkai on 2017/8/8.
 */

public class AvatarHelper {
    public interface IAvatar {
        void uploadAvatar(String image);
    }

    public static void openAlbum(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK); // 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intent, 1);
    }

    public static void uploadAvatar(BaseActivity context, IAvatar iAvatar, final int requestCode, final int
            resultCode,
                                    Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data) {
            try {
                Bundle extras = data.getExtras();
                Bitmap photo = null;
                if (extras != null) {
                    photo = extras.getParcelable("data");
                }

                if (photo == null) {
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    String picturePath = "";
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                    } else {
                        picturePath = selectedImage.getPath();
                    }
                    if (requestCode == 1) {
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(selectedImage, "image/*");
                        intent.putExtra("crop", "true");
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra("outputX", 160);
                        intent.putExtra("outputY", 160);
                        intent.putExtra("return-data", true);
                        context.startActivityForResult(intent, 2);
                        return;
                    }
                    photo = BitmapFactory.decodeFile(picturePath);
                }

                if (photo == null) {
                    TipsHelper.tips(context, "获取图片失败");
                    return;
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 - 100)压缩文件
                byte[] byteArray = stream.toByteArray();
                String streamStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String image = "data:image/png;base64," + streamStr;
                iAvatar.uploadAvatar(image);
            } catch (Exception e) {
                context.dismissLoadingDialog();
                TipsHelper.tips(context, "修改失败" + e);
                LogUtils.i("修改失败" + e);
            }
        }
    }
}
