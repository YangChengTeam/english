package com.yc.english.group.view.provider;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.style.TabStopSpan;

import com.blankj.utilcode.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by wanglin  on 2017/7/26 09:06.
 * 自定义消息
 */
@MessageTag(value = "app:custom", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CustomMessage extends MessageContent {
    private String title;//消息标题
    private String content;//消息内容
    private String imgUrl;//消息图像
    private String url = "";
    private String extra = "";


    public CustomMessage(String title, String content, String imgUrl) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;

    }

    public CustomMessage(String title, String content, String imgUrl, String url) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.url = url;
    }

    public static CustomMessage obtain(String title, String content, String imageUrl) {
        return new CustomMessage(title, content, imageUrl);
    }

    public static CustomMessage obtain(String title, String content, String imageUrl, String url) {
        return new CustomMessage(title, content, imageUrl, url);
    }


    public CustomMessage(Parcel in) {
        setContent(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性

        //这里可继续增加你消息的属性
        setTitle(ParcelUtils.readFromParcel(in));
        setUrl(ParcelUtils.readFromParcel(in));
        setImgUrl(ParcelUtils.readFromParcel(in));

        setExtra(ParcelUtils.readFromParcel(in));

        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
        setMentionedInfo(ParcelUtils.readFromParcel(in, MentionedInfo.class));

    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            if (!TextUtils.isEmpty(getExtra()))
                jsonObj.put("extra", getExtra());

            if (getJSONUserInfo() != null)
                jsonObj.putOpt("user", getJSONUserInfo());

            if (getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", getJsonMentionInfo());
            }
            if (getTitle() != null)
                jsonObj.put("title", getExpression(getTitle()));
            if (getContent() != null)
                jsonObj.put("content",getExpression(getContent()));
            if (!TextUtils.isEmpty(getImgUrl()))
                jsonObj.put("imgUrl",getImgUrl());
            if (!TextUtils.isEmpty(getUrl()))
                jsonObj.put("url", getUrl());


        } catch (JSONException var4) {
            LogUtils.e("JSONException", var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }

    private String getExpression(String content) {
        Pattern pattern = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, this.toExpressionChar(matcher.group(1)));
        }

        matcher.appendTail(sb);
        LogUtils.d("getExpression--", sb.toString());
        return sb.toString();
    }

    private String toExpressionChar(String expChar) {
        int inthex = Integer.parseInt(expChar, 16);
        return String.valueOf(Character.toChars(inthex));
    }

    public CustomMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                setContent(jsonObj.optString("content"));

            if (jsonObj.has("extra"))
                setExtra(jsonObj.optString("extra"));

            if (jsonObj.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            if (jsonObj.has("mentionedInfo")) {
                setMentionedInfo(parseJsonToMentionInfo(jsonObj.getJSONObject("mentionedInfo")));
            }

            if (jsonObj.has("title"))
                setContent(jsonObj.optString("title"));

            if (jsonObj.has("imgUrl"))
                setExtra(jsonObj.optString("imgUrl"));

            if (jsonObj.has("url")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("url")));
            }


        } catch (JSONException e) {
            LogUtils.e("JSONException", e.getMessage());
        }


    }


    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<CustomMessage> CREATOR = new Creator<CustomMessage>() {

        @Override
        public CustomMessage createFromParcel(Parcel source) {
            return new CustomMessage(source);
        }

        @Override
        public CustomMessage[] newArray(int size) {
            return new CustomMessage[size];
        }
    };

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getContent());//该类为工具类，对消息中属性进行序列化

        //这里可继续增加你消息的属性
        ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getUrl());

        ParcelUtils.writeToParcel(dest, getImgUrl());
        ParcelUtils.writeToParcel(dest, getExtra());
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, getMentionedInfo());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

//    @Override
//    public List<String> getSearchableWord() {
//        List<String> words = new ArrayList<>();
//        words.add(content);
//        return words;
//
//    }
}
