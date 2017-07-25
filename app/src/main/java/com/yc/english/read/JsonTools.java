package com.yc.english.read;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.yc.english.read.domain.EnglishCourse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class JsonTools {
    /**
     * filename assets目录下的json文件名
     *
     * @param context
     * @param fileName
     * @return
     */
    public static List<EnglishCourse> jsonData(Context context, String fileName) {
        JSONObject jsonDate = null;
        List<EnglishCourse> resultList = new ArrayList<EnglishCourse>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(fileName), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            jsonDate = JSON.parseObject(stringBuilder.toString());

            Log.e("jsonDate", "==" + jsonDate);

            JSONArray seriesArray = jsonDate.getJSONArray("dict_book_unit_sections");
            LogUtils.e("dict_book_unit_sections---array--->" + seriesArray);
            List<EnglishCourse> listSeries = JSON.parseArray(seriesArray.toString(), EnglishCourse.class);

            for(int i=0;i<listSeries.size();i++){
                if(i%2 == 0){
                    EnglishCourse tempSeries = listSeries.get(i);
                    EnglishCourse englishCourse = new EnglishCourse(EnglishCourse.CLICK_ITEM_VIEW);
                    englishCourse.setSubtitlecn(tempSeries.getSubtitlecn());
                    englishCourse.setSubtitle(tempSeries.getSubtitle());
                    resultList.add(englishCourse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}