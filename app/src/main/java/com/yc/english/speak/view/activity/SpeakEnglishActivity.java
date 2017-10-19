package com.yc.english.speak.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.read.view.wdigets.SpaceItemDecoration;
import com.yc.english.speak.contract.ListenEnglishContract;
import com.yc.english.speak.model.bean.ListenEnglishBean;
import com.yc.english.speak.presenter.ListenEnglishListPresenter;
import com.yc.english.speak.utils.IatSettings;
import com.yc.english.speak.utils.JsonParser;
import com.yc.english.speak.view.adapter.ListenEnglishItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by admin on 2017/10/16.
 *
 * @author admin
 */

public class SpeakEnglishActivity extends FullScreenActivity<ListenEnglishListPresenter> implements ListenEnglishContract.View {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.listen_english_list)
    RecyclerView mListenEnglishRecyclerView;

    ListenEnglishItemAdapter mListenEnglishItemAdapter;

    LinearLayoutManager mLinearLayoutManager;

    private int lastPosition = -1;

    private boolean isTape;

    private boolean isPlayTape;

    private CircularProgressBar progressBar;

    private CircularProgressBar playProgressBar;

    private float progress = 0;

    private Timer timer;

    private TimerTask task;

    private MediaRecorder mediaRecorder;

    private MediaPlayer mPlayer;

    private File audioFile;

    private String audioFilePath;

    int playNum;

    int playCount;

    // 语音听写对象
    private SpeechRecognizer mIat;

    // 语音听写UI
    private RecognizerDialog mIatDialog;

    /**
     * 用HashMap存储听写结果
     */
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Toast mToast;

    private SharedPreferences mSharedPreferences;
    /**
     * 引擎类型
     */
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private boolean mTranslateEnable = false;

    private String voiceText;

    private int countNum = 20;

    /**
     * 函数调用返回值
     */
    int ret = 0;

    @Override
    public int getLayoutId() {
        return R.layout.speak_english_activity;
    }

    @Override
    public void init() {

        List<ListenEnglishBean> list = new ArrayList<ListenEnglishBean>();
        for (int i = 0; i < countNum; i++) {
            ListenEnglishBean listenEnglishBean = new ListenEnglishBean();
            listenEnglishBean.setCnSentence("学习英语天天好心情");
            listenEnglishBean.setEnSentence("learn english happy every day");
            if (i == 0) {
                listenEnglishBean.setShowSpeak(false);
            } else {
                listenEnglishBean.setShowSpeak(false);
            }
            list.add(listenEnglishBean);
        }

        mPresenter = new ListenEnglishListPresenter(this, this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mListenEnglishRecyclerView.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(0.5f)));
        mListenEnglishItemAdapter = new ListenEnglishItemAdapter(this, list);

        mListenEnglishRecyclerView.setLayoutManager(mLinearLayoutManager);
        mListenEnglishRecyclerView.setAdapter(mListenEnglishItemAdapter);

        //mPresenter.getListenEnglish("1", 1, 10);

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(SpeakEnglishActivity.this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(SpeakEnglishActivity.this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);

        mListenEnglishItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == lastPosition) {
                    return;
                }

                progress = 0;
                playNum = 0;
                playCount = 0;
                stopTask();
                tapeStop();
                stopPlayTape();
                enableState(position);
            }
        });

        mListenEnglishItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_speak_tape && !isTape && !isPlayTape) {
                    View currentView = mLinearLayoutManager.findViewByPosition(position);
                    currentView.findViewById(R.id.speak_tape_layout).setVisibility(View.VISIBLE);
                    progressBar = (CircularProgressBar) currentView.findViewById(R.id.progress_bar);
                    view.setVisibility(View.GONE);
                    initTask();
                    timer.schedule(task, 200, 150);
                    tapeStart();
                    isTape = true;
                }

                if (view.getId() == R.id.speak_tape_layout && isTape && !isPlayTape) {
                    View currentView = mLinearLayoutManager.findViewByPosition(position);
                    currentView.findViewById(R.id.iv_speak_tape).setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    stopTask();
                    tapeStop();
                    isTape = false;
                }

                if (view.getId() == R.id.iv_play_self_speak && !isPlayTape && !isTape) {
                    if (audioFile != null && audioFile.exists()) {
                        View currentView = mLinearLayoutManager.findViewByPosition(position);
                        playProgressBar = (CircularProgressBar) currentView.findViewById(R.id.play_progress_bar);
                        currentView.findViewById(R.id.play_speak_tape_layout).setVisibility(View.VISIBLE);
                        view.setVisibility(View.GONE);
                        playTape(position);
                        isPlayTape = true;
                    }
                }

                if (view.getId() == R.id.play_speak_tape_layout && isPlayTape && !isTape) {
                    View currentView = mLinearLayoutManager.findViewByPosition(position);
                    currentView.findViewById(R.id.iv_play_self_speak).setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    stopPlayTape();
                }
                return false;
            }
        });
    }

    public void enableState(int position) {
        mListenEnglishItemAdapter.getData().get(position).setShowSpeak(true);
        if (lastPosition > -1) {
            if (position != lastPosition) {
                mListenEnglishItemAdapter.getData().get(lastPosition).setShowSpeak(false);
                isTape = false;
            }
        }
        lastPosition = position;
        mListenEnglishItemAdapter.notifyDataSetChanged();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (progressBar != null && isTape) {

                    int max = 5;
                    int min = 1;
                    Random random = new Random();
                    int num = random.nextInt(max) % (max - min + 1) + min;

                    if (progress >= 45) {
                        progress = progress - num;
                    } else {
                        progress = progress + num;
                    }
                    progressBar.setProgress(progress);
                }

                if (playProgressBar != null && isPlayTape) {

                    if (progress > 100) {
                        progress = 100;
                    } else {
                        progress = progress + playCount;
                    }

                    playProgressBar.setProgress(progress);
                }

            }
            super.handleMessage(msg);
        }
    };

    public void initTask() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                // 需要做的事:发送消息
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
    }

    public void stopTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer = null;
        }
    }


    /**
     * 开始录音
     */
    public void tapeStart() {
       /* try {
            mediaRecorder = new MediaRecorder();
            // 第1步：设置音频来源（MIC表示麦克风）
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //第2步：设置音频输出格式（默认的输出格式）
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //第3步：设置音频编码方式（默认的编码方式）
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //创建一个临时的音频输出文件
            audioFile = new File(SDCardUtils.getSDCardPath() + "/000english/test.wav");
            if (!audioFile.exists()) {
                audioFile.createNewFile();
            }
            //第4步：指定音频输出文件
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            LogUtils.e("audio path --->" + audioFile.getAbsolutePath());

            //第5步：调用prepare方法
            mediaRecorder.prepare();
            //第6步：调用start方法开始录音
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mIatResults.clear();
        // 设置参数
        setParam();
        boolean isShowDialog = mSharedPreferences.getBoolean(
                getString(R.string.pref_key_iat_show), false);
        if (isShowDialog) {
            // 显示听写对话框
            /*mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();*/
            ToastUtils.showLong("开始");
        } else {
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                ToastUtils.showLong("听写失败,错误码：" + ret);
            } else {
                ToastUtils.showLong("开始");
            }
        }

    }

    /**
     * 停止录音
     */
    public void tapeStop() {
        /*if (mediaRecorder != null ) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }*/
        isTape = false;
    }

    /**
     * 播放录音
     */
    public void playTape(final int position) {
        try {
            if (audioFile != null && audioFile.exists()) {
                initTask();
                timer.schedule(task, 200, 150);

                mPlayer = new MediaPlayer();
                //设置要播放的文件
                mPlayer.setDataSource(audioFile.getAbsolutePath());
                mPlayer.prepare();
                //播放
                mPlayer.start();

                playNum = mPlayer.getDuration() % 150 == 0 ? mPlayer.getDuration() / 150 : mPlayer.getDuration() / 150 + 1;
                playCount = 100 % playNum == 0 ? 100 / playNum : 100 / playNum + 1;
                progress = 0;

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playProgressBar.setProgress(100);

                        View currentView = mLinearLayoutManager.findViewByPosition(position);
                        currentView.findViewById(R.id.play_speak_tape_layout).setVisibility(View.GONE);
                        currentView.findViewById(R.id.iv_play_self_speak).setVisibility(View.VISIBLE);
                        stopPlayTape();
                        stopTask();
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
        isPlayTape = false;
        if (playProgressBar != null) {
            playProgressBar.setProgress(0);
        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtils.e("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.showLong("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }
        }

        /**
         * 识别回调错误.
         */
        @Override
        public void onError(SpeechError error) {
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                ToastUtils.showLong(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                ToastUtils.showLong(error.getPlainDescription(true));
            }
        }

    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            ToastUtils.showLong("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                ToastUtils.showLong(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                ToastUtils.showLong(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            ToastUtils.showLong("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtils.e(results.getResultString());
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //ToastUtils.showLong("当前正在说话，音量大小：" + volume);
            LogUtils.e("返回音频数据：" + data.length);
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

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
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
            LogUtils.e("语音录入结果--->" + voiceText);
            ToastUtils.showLong(voiceText);
        }

        View currentView = mLinearLayoutManager.findViewByPosition(lastPosition);
        currentView.findViewById(R.id.iv_speak_tape).setVisibility(View.VISIBLE);
        currentView.findViewById(R.id.speak_tape_layout).setVisibility(View.GONE);
        currentView.findViewById(R.id.layout_result).setVisibility(View.VISIBLE);
        if (voiceText.equals("Book")) {
            currentView.findViewById(R.id.iv_result).setBackgroundResource(R.mipmap.listen_result_yes);
        } else {
            currentView.findViewById(R.id.iv_result).setBackgroundResource(R.mipmap.listen_result_no);
        }

        stopTask();
        tapeStop();

        if (mIat != null) {
            mIat.stopListening();
        }
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

    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            ToastUtils.showLong("解析结果失败，请确认是否已开通翻译功能。");
        } else {
            voiceText = "原始语言:\n" + oris + "\n目标语言:\n" + trans;
        }
    }

    @Override
    public void hideStateView() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showListenEnglishList(List<ListenEnglishBean> list) {

    }
}
