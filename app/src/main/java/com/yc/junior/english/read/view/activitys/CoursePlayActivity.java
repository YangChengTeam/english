package com.yc.junior.english.read.view.activitys;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.utils.SpeechUtils;
import com.yc.junior.english.base.utils.WakeLockUtils;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.read.common.AudioPlayManager;
import com.yc.junior.english.read.common.ExoPlayer;
import com.yc.junior.english.read.common.OnUiUpdateManager;
import com.yc.junior.english.read.contract.CoursePlayContract;
import com.yc.junior.english.read.model.domain.EnglishCourseInfo;
import com.yc.junior.english.read.model.domain.EnglishCourseInfoList;
import com.yc.junior.english.read.model.domain.UnitInfo;
import com.yc.junior.english.read.presenter.CoursePlayPresenter;
import com.yc.junior.english.read.view.adapter.ReadCourseItemClickAdapter;
import com.yc.junior.english.read.view.fragment.SpeedSettingFragment;
import com.yc.junior.english.read.view.wdigets.PopupWindowFactory;
import com.yc.junior.english.speak.utils.IatSettings;
import com.yc.junior.english.speak.utils.VoiceJsonParser;
import com.yc.junior.english.vip.model.bean.GoodsType;
import com.yc.junior.english.vip.utils.VipDialogHelper;
import com.yc.soundmark.base.constant.SpConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import yc.com.base.StatusBarCompat;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.blankj.utilcode.util.StringUtils;
import yc.com.blankj.utilcode.util.ToastUtils;


public class CoursePlayActivity extends FullScreenActivity<CoursePlayPresenter> implements CoursePlayContract.View, OnUiUpdateManager {


    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.layout_content)
    FrameLayout mLayoutContext;

    @BindView(R.id.rv_course_list)
    RecyclerView mCourseRecyclerView;

    @BindView(R.id.iv_course_play)
    ImageView mCoursePlayImageView;

    @BindView(R.id.tv_language)
    TextView mLanguageTextView;

    @BindView(R.id.iv_next_unit)
    ImageView mNextUnitImageView;

    @BindView(R.id.iv_language_change)
    ImageView mLanguageChangeImageView;


    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.ll_speed)
    LinearLayout llSpeed;
    @BindView(R.id.toolbarWrapper)
    FrameLayout toolbarWrapper;


    //播放位置
    private int playPosition = -1;

    private LinearLayoutManager linearLayoutManager;

    private boolean isContinue = false;

    private int languageType = 1; //1:中英,2:英,3:中

    private PublishSubject<Integer> mTsSubject;

    private String unitId;

    private String unitTitle = "";

    private int currentPage = 1;

    private boolean isNext = false;


    private int position;

    /**
     * 一本书的单元总数
     */
    private List<UnitInfo> unitInfoList;

    private boolean isRead;

    // 语音听写对象
    private SpeechRecognizer mIat;

    /**
     * 函数调用返回值
     */
    private int ret = 0;

    /**
     * 用HashMap存储听写结果
     */
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();


    private SharedPreferences mSharedPreferences;
    /**
     * 引擎类型
     */
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private boolean mTranslateEnable = false;

    private MediaPlayer mPlayer;

    private File audioFile;

    private String audioFilePath;

    private String voiceText;

    private int lastPosition = 0;


    private ImageView mTapeImageView;

    private PopupWindowFactory mTapePop;
    private UserInfo userInfo;

    private AudioPlayManager manager;

    private ReadCourseItemClickAdapter mItemAdapter;

    private boolean isPlay = true;//是否点读

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_course_play;
    }

    @Override
    public void init() {

        StatusBarCompat.compat(this, toolbarWrapper, mToolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
            unitInfoList = bundle.getParcelableArrayList("unitInfoList");
            if (unitInfoList != null) {
                UnitInfo unitInfo = unitInfoList.get(position);
                unitId = unitInfo.getId();
                unitTitle = unitInfo.getName();
            }
        }

        WakeLockUtils.acquireWakeLock(this);

        initMediaPlayer();

        int anInt = SPUtils.getInstance().getInt(SpConstant.PLAY_SPEED, 40);

        float speed = (anInt * 2 + 20) / (100 * 1.0f);
        BigDecimal bd = new BigDecimal(speed);

        tvSpeed.setText(String.format(getString(R.string.play_speed_result),
                String.valueOf(bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue())));


        mPresenter = new CoursePlayPresenter(this, this);

        mToolbar.setTitle(unitTitle);
        mToolbar.showNavigationIcon();
        mToolbar.setTitleColor(ContextCompat.getColor(this, R.color.black_333));

        initRecyclerView();

        initIat();

        mPresenter.getCourseListByUnitId(currentPage, 0, unitId);


        if (SpeechUtils.getAppids() == null || SpeechUtils.getAppids().size() <= 0) {
            SpeechUtils.setAppids(this);
        }

        userInfo = UserInfoHelper.getUserInfo();
        initListener();
    }

    private void initListener() {
        mTsSubject = PublishSubject.create();

        mTsSubject.delay(800, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(position -> {

            if (playPosition < mItemAdapter.getData().size()) {
                enableState(playPosition);

                startPlay(position);
            } else {
                isContinue = false;
                disableState();
            }
        });


        //下一单元
        RxView.clicks(mNextUnitImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (manager != null)
                manager.stop();
            if (unitInfoList != null && position + 1 < unitInfoList.size()) {
                UnitInfo unitInfo = unitInfoList.get(position + 1);
                position++;
                //1是免费，2是收费
                if (unitInfo.getFree() == 1) {
                    isRead = true;
                } else {
                    if (userInfo != null) {
                        isRead = UserInfoHelper.isVip(userInfo);
                    } else {
                        UserInfoHelper.isGotoLogin(CoursePlayActivity.this);
                        return;
                    }
                }
                if (!isRead) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_GENERAL_VIP);
                    VipDialogHelper.showVipDialog(getSupportFragmentManager(), "", bundle);
                    return;
                }

                isNext = true;
                resetPlayState();
                mToolbar.setTitle(unitInfo.getName());
                unitId = unitInfo.getId();
                currentPage = 1;
                mPresenter.getCourseListByUnitId(currentPage, 0, unitId);

            } else {
                TipsHelper.tips(CoursePlayActivity.this, "已经是最后一个单元");
            }

        });

        //播放
        RxView.clicks(mCoursePlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (playPosition == -1) {
                playPosition = 0;
            }
            isContinue = !isContinue;
            isPlay = !isPlay;
            if (isContinue) {
                enableState(playPosition);

                startPlay(playPosition);
            } else {
                disableState();
            }
        });

        //语言切换
        RxView.clicks(mLanguageChangeImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            languageType++;

            if (languageType > 3) {
                languageType = 1;
            }
            if (playPosition > -1 && playPosition < mItemAdapter.getData().size()) {
                mItemAdapter.getData().get(playPosition).setPlay(true);
            }
            switch (languageType) {
                case 1:
                    mLanguageTextView.setText(getString(R.string.read_course_language_blend_text));
                    break;
                case 2:
                    mLanguageTextView.setText(getString(R.string.read_course_language_en_text));
                    break;
                case 3:
                    mLanguageTextView.setText(getString(R.string.read_course_language_cn_text));
                    break;
                default:
                    break;
            }

            mItemAdapter.setLanguageType(languageType);
            mItemAdapter.notifyDataSetChanged();
        });

        mItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            /*if (position == playPosition) {
                return;
            }*/
            isContinue = false;
            playPosition = position;
            itemChoose(playPosition);
            //startSynthesizer(playPosition);
        });

        //点击跟读按钮后操作
        mItemAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            playPosition = position;
            if (view.getId() == R.id.iv_tape) {

                lastPosition = position;
                isContinue = false;
                disableState();

                //弹出录音界面
                //mIatDialog.setListener(mRecognizerDialogListener);
                //mIatDialog.show();

                /*View currentView = linearLayoutManager.findViewByPosition(playPosition);
                if (currentView != null) {
                    ((ImageView) currentView.findViewById(R.id.iv_tape)).setImageResource(R.drawable.record_microphone);
                }*/

                final View tapeView = View.inflate(CoursePlayActivity.this, R.layout.layout_microphone, null);
                mTapePop = new PopupWindowFactory(CoursePlayActivity.this, tapeView);

                //PopupWindow布局文件里面的控件
                mTapeImageView = tapeView.findViewById(R.id.iv_recording_icon);
                mTapePop.showAtLocation(mLayoutContext, Gravity.CENTER, 0, 0);

                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    ToastUtils.showLong("听写失败,错误码：" + ret);
                } else {
                    //ToastUtils.showLong("开始");
                }
            }

            if (view.getId() == R.id.iv_play) {


                isPlay = !isPlay;

                isContinue = !isContinue;
                if (manager != null && isPlay) {
                    manager.stop();
                    disableState();
                } else {
//                    isContinue = false;
                    enableState(playPosition);


                    startPlay(playPosition);
                }
            }

            if (view.getId() == R.id.iv_play_tape) {
                if (mItemAdapter.getData().get(lastPosition).isShow()) {
                    if (mPlayer != null && mPlayer.isPlaying()) {
                        stopPlayTape();
                        View currentView = linearLayoutManager.findViewByPosition(position);
                        if (currentView != null) {
                            Glide.with(CoursePlayActivity.this).load(R.mipmap.item_tape_play_normal_icon).into((ImageView) currentView.findViewById(R.id.iv_play_tape));
                        }
                    } else {
                        playTape(position);
                    }
                } else {
                    ToastUtils.showLong("请先录音评测后再回放");
                }
            }

        });

        mCourseRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    currentPage++;
                    mPresenter.getCourseListByUnitId(currentPage, 0, unitId);
                }
            }
        });


        RxView.clicks(llSpeed).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            SpeedSettingFragment speedSettingFragment = new SpeedSettingFragment();
            speedSettingFragment.show(getSupportFragmentManager(), "");
            speedSettingFragment.setOnDissmissListener(() -> {

                int speed = SPUtils.getInstance().getInt(SpConstant.PLAY_SPEED, 1);

                float result = (speed * 2 + 20) / (100 * 1.0f);
                BigDecimal bd = new BigDecimal(result);

                tvSpeed.setText(String.format(getString(R.string.play_speed_result), String.valueOf(bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue())));
            });
        });
    }

    /**
     * 初始化recyclerView 和adapter
     */
    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        mCourseRecyclerView.setLayoutManager(linearLayoutManager);

        mItemAdapter = new ReadCourseItemClickAdapter(null);
        mCourseRecyclerView.setAdapter(mItemAdapter);
    }

    /**
     * 初始化录音和评测
     */
    private void initIat() {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(CoursePlayActivity.this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
//        mIatDialog = new RecognizerDialog(CoursePlayActivity.this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);

        setParam();
    }


    private void initMediaPlayer() {
//        manager = new MediaPlayerPlayer(this);
        manager = new ExoPlayer(this, this);
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = code -> {
        LogUtils.e("SpeechRecognizer init() code = " + code);
        if (code != ErrorCode.SUCCESS) {
            ToastUtils.showLong("初始化失败，错误码：" + code);
        }
    };


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //ToastUtils.showLong("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            if (mTapePop != null && mTapePop.getPopupWindow().isShowing()) {
                mTapePop.dismiss();
            }
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                ToastUtils.showLong(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                ToastUtils.showLong("听写识别错误，请重试");
                if (mIat != null) {
                    mIat.stopListening();
                }
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //ToastUtils.showLong("结束说话");
            if (mTapePop != null && mTapePop.getPopupWindow().isShowing()) {
                mTapePop.dismiss();
            }
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtils.e("results string" + results.getResultString());

            if (mTapePop != null && mTapePop.getPopupWindow().isShowing()) {
                mTapePop.dismiss();
            }

            if (mTranslateEnable) {
                //printTransResult(results);
            } else {
                printResult(results);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //ToastUtils.showLong("当前正在说话，音量大小：" + volume);
            LogUtils.e("音量大小--->" + volume);
            mTapeImageView.getDrawable().setLevel((int) (3000 + 6000 * volume * 18 / 100));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void resetPlayState() {
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
        playPosition = -1;
        isContinue = false;
        disableState();
    }

    @Override
    public void hide() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mCourseRecyclerView, "网络不给力", v -> mPresenter.getCourseListByUnitId(currentPage, 0, unitId));
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(mCourseRecyclerView);
    }

    @Override
    public void showLoading() {
        mStateView.showLoading(mCourseRecyclerView, 2);
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean(this.getString(R.string.pref_key_translate), false);
        if (mTranslateEnable) {
            LogUtils.e("translate enable");
            mIat.setParameter(SpeechConstant.ASR_SCH, "1");
            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "en_us");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "en");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));


        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        audioFilePath = Environment.getExternalStorageDirectory() + "/msc/iat.wav";
        audioFile = new File(audioFilePath);
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, audioFilePath);
    }

    private void printResult(RecognizerResult results) {
        String text = VoiceJsonParser.parseIatResult(results.getResultString());

        if (StringUtils.isEmpty(text)) {
            return;
        }
        LogUtils.e("the text--->" + text);

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        voiceText = resultBuffer.toString();
        if (!StringUtils.isEmpty(voiceText)) {
            LogUtils.e("result text --->" + voiceText);
        }

        LogUtils.i("first --->" + mItemAdapter.getData().get(lastPosition).getSubTitle());

        mItemAdapter.getData().get(lastPosition).setShow(true);

        if (compareResult(mItemAdapter.getData().get(lastPosition).getSubTitle(), voiceText)) {
            LogUtils.i("result --->" + true);
            mItemAdapter.getData().get(lastPosition).setSpeakResult(true);
        } else {
            LogUtils.i("result --->" + false);
            mItemAdapter.getData().get(lastPosition).setSpeakResult(false);
        }

        mItemAdapter.notifyDataSetChanged();

        if (mIat != null) {
            mIat.stopListening();
        }
    }

    /**
     * 将录入的语音与源语音进行对比
     *
     * @param sourceSen
     * @param speakSen
     * @return
     */
    public boolean compareResult(String sourceSen, String speakSen) {

        try {
            if (StringUtils.isEmpty(sourceSen) || StringUtils.isEmpty(speakSen)) {
                return false;
            }

            String regEx = " |、|，|。|；|？|！|,|\\.|;|\\?|!|]|:|：|\"|-";
            Pattern p = Pattern.compile(regEx);

            //按照句子结束符分割句子
            String[] words = p.split(sourceSen);
            List<String> sourceList = new ArrayList<>();
            for (int i = 0; i < words.length; i++) {
                if (!StringUtils.isTrimEmpty(words[i])) {
                    sourceList.add(words[i]);
                }
            }

            List<String> speakList = new ArrayList<>();
            String[] speakWords = p.split(speakSen);
            for (int m = 0; m < speakWords.length; m++) {
                if (!StringUtils.isTrimEmpty(speakWords[m])) {
                    speakList.add(speakWords[m]);
                }
            }

            int matchCount = 0;
            float percent = 0;
            for (String str : sourceList) {
                if (speakList.contains(str)) {
                    matchCount++;
                }
            }

            if (matchCount > 0) {
                percent = (float) matchCount / (float) sourceList.size() * 100;
            } else {
                return false;
            }

            mItemAdapter.getData().get(lastPosition).setPercent(percent + "");

            if (percent >= 60) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void startPlay(int position) {
        if (position < 0 || position >= mItemAdapter.getData().size()) {
            return;
        }

        String url = mItemAdapter.getData().get(position).getMp3url();

        Log.e("TAG", "startPlay: " + url);
        if (!url.endsWith(".mp3")) {
            if (position < mItemAdapter.getData().size() - 1)
                url = mItemAdapter.getData().get(position + 1).getMp3url();
            else {
                onCompleteUI();
                return;
            }
        }
        if (manager == null) manager = new ExoPlayer(this, this);
        manager.start(url);

    }


    public void speekContinue(int index) {


        if (isContinue) {
            isPlay = false;
            mTsSubject.onNext(index);
        } else {
            isPlay = true;
            disableState();
        }
    }

    public void resetState() {
        for (EnglishCourseInfo englishCourseInfo : mItemAdapter.getData()) {
            englishCourseInfo.setPlay(false);
            englishCourseInfo.setShow(false);
        }
    }

    public void itemChoose(int position) {
        startState(position, false);
    }

    public void enableState(int position) {
        startState(position, true);

    }

    private void startState(int position, boolean isPlay) {
        if (position < 0 || position >= mItemAdapter.getData().size()) {
            return;
        }

        if (playPosition > 2) {
            linearLayoutManager.scrollToPositionWithOffset(playPosition - 2, 0);
        }
        try {

            if (manager != null) manager.stop();
            resetState();

            if (isPlay) {
                mCoursePlayImageView.setBackgroundResource(R.drawable.read_playing_course_btn_selector);
                mItemAdapter.getData().get(position).setPlay(true);
            }
            mItemAdapter.setLastPosition(playPosition);
            mItemAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.msg("e: " + e.getMessage());
        }
    }

    public void disableState() {
        if (!isContinue) {
            mCoursePlayImageView.setBackgroundResource(R.drawable.read_play_course_btn_selector);
        }

        if (manager != null) manager.stop();
        resetState();
        mItemAdapter.setLastPosition(playPosition);
        mItemAdapter.notifyDataSetChanged();
    }

    /**
     * 播放录音
     */
    public void playTape(final int position) {
        try {
            if (audioFile != null && audioFile.exists()) {
                final View currentView = linearLayoutManager.findViewByPosition(position);
                if (currentView != null) {
                    Glide.with(CoursePlayActivity.this).load(R.mipmap.item_read_press_icon).into((ImageView) currentView.findViewById(R.id.iv_play_tape));
                }

                mPlayer = new MediaPlayer();

                //设置要播放的文件
                mPlayer.setDataSource(audioFile.getAbsolutePath());
                mPlayer.prepare();
                //播放
                mPlayer.start();

//                mPlayer.getPlaybackParams().setSpeed(1.2f);
                mPlayer.setOnCompletionListener(mp -> {
                    disableState();
                    stopPlayTape();

//                        View currentView = linearLayoutManager.findViewByPosition(position);
                    if (currentView != null) {
                        Glide.with(CoursePlayActivity.this).load(R.mipmap.item_tape_play_normal_icon).into((ImageView) currentView.findViewById(R.id.iv_play_tape));
                    }
                });

            }
        } catch (Exception e) {
            LogUtils.e("prepare() failed");
        }
    }

    /**
     * 停止播放录音
     */
    public void stopPlayTape() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null)
            manager.onDestroy();
        WakeLockUtils.releaseWakeLock();
    }


    @Override
    public void showCourseListData(EnglishCourseInfoList englishCourseInfoList) {
        if (englishCourseInfoList != null) {
            if (isNext) {
                mItemAdapter.setNewData(englishCourseInfoList.list);
                isNext = false;
            } else {
                mItemAdapter.addData(englishCourseInfoList.list);
            }
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)
            }
    )
    public void getInfo(String loginInfo) {
        userInfo = UserInfoHelper.getUserInfo();
    }


    @Override
    public void onCompleteUI() {
        runOnUiThread(() -> {

//                Log.e("TAG", "run: " + playPosition + "  size: " + mItemAdapter.getData().size());

            speekContinue(isContinue ? ++playPosition : playPosition);

//            isPlay = true;

            if (playPosition == mItemAdapter.getData().size() - 1) {
                playPosition = -1;
                isContinue = false;
            }
        });
    }

    @Override
    public void onErrorUI(int what, int extra, String msg) {
        speekContinue(isContinue ? ++playPosition : playPosition);
        LogUtil.msg("code: " + what + "  msg:  " + msg);
    }

    @Override
    public void onStopUI() {

    }

    @Override
    public void onStartUI(int duration) {

    }

}
