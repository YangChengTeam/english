package com.yc.junior.english.news.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.yc.junior.english.R;
import com.yc.junior.english.news.adapter.QRCodeScanAdapter;
import com.yc.junior.english.news.contract.QRCodeContract;
import com.yc.junior.english.news.presenter.QRCodePresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import yc.com.base.BaseDialogFragment;
import yc.com.blankj.utilcode.util.ScreenUtils;


/**
 * Created by wanglin  on 2018/10/18 13:47.
 */
public class QRCodeScanFragment extends BaseDialogFragment<QRCodePresenter> implements QRCodeContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String imgUrl;
    private List<String> results;
    private Result result;
    private QRCodeScanAdapter scanAdapter;

    @Override
    public void init() {

        mPresenter = new QRCodePresenter(getActivity(), this);
        results = new ArrayList<>();
        results.add(getString(R.string.saveto_phone));

        if (getArguments() != null && getArguments().getString("imgurl") != null) {
            imgUrl = getArguments().getString("imgurl");
            mPresenter.getBitmap(imgUrl);
        }

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
//        window.setGravity(Gravity.CENTER);
        window.setLayout(ScreenUtils.getScreenWidth() * 2 / 3, attributes.height);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setHasFixedSize(true);


        scanAdapter = new QRCodeScanAdapter(results);
        recyclerView.setAdapter(scanAdapter);

        recyclerView.addItemDecoration(new BaseItemDecoration(getActivity()));


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dialog_qrcode;
    }


    /**
     * bitmap 保存为jpg 图片
     *
     * @param mBitmap 图片源
     * @param bitName 图片名
     */
    File file;

    public void saveMyBitmap(Bitmap mBitmap, String bitName) {
        file = new File(Environment.getExternalStorageDirectory() + "/" + bitName);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 先保存到本地再广播到图库
     */
    public void saveImageToGallery(Context context) {

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), "code", null);
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是否为二维码
     *
     * @return
     */
    private boolean decodeImage(Bitmap bitmap) {


        result = handleQRCodeFormBitmap(bitmap);

        return result != null;

    }


    public static Result handleQRCodeFormBitmap(Bitmap bitmap) {
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        int[] pxies = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pxies, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pxies);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader2 = new QRCodeReader();
        Result result = null;
        try {
            try {
                result = reader2.decode(bitmap1, hints);
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getImagName(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }


    @Override
    public void showBitmap(Bitmap bitmap) {

        if (decodeImage(bitmap)) {
            results.add(getString(R.string.scan_qrcode));
            scanAdapter.setNewData(results);
        }


        saveMyBitmap(bitmap, getImagName(imgUrl));
        scanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    //保存到手机
                    saveImageToGallery(getActivity());
                } else if (position == 1) {
                    //识别二维码
                    discernQrCode();
                }
                QRCodeScanFragment.this.dismiss();
            }
        });
    }

    private void discernQrCode() {
        LogUtil.msg("url: " + result.getText() + "---" + result.toString());
        if (result.getText().contains("wechat")) {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // TODO: handle exception
                ToastUtil.toast2(getActivity(), "检查到您手机没有安装微信，请安装后使用该功能");
            }
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(result.toString()));
        getActivity().startActivity(intent);

    }
}
