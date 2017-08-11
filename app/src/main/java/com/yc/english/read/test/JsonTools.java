package com.yc.english.read.test;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.english.read.model.domain.EnglishCourseInfo;
import com.yc.english.read.model.domain.WordInfo;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;

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
    public static List<EnglishCourseInfo> jsonData(Context context, String fileName) {
        JSONObject jsonDate = null;
        List<EnglishCourseInfo> resultList = new ArrayList<EnglishCourseInfo>();
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
            List<EnglishCourseInfo> listSeries = JSON.parseArray(seriesArray.toString(), EnglishCourseInfo.class);

            for (int i = 0; i < listSeries.size(); i++) {
                if (i % 2 == 0) {
                    EnglishCourseInfo tempSeries = listSeries.get(i);
                    EnglishCourseInfo englishCourse = new EnglishCourseInfo(EnglishCourseInfo.CLICK_ITEM_VIEW);
                    englishCourse.setMeans(tempSeries.getMeans());
                    englishCourse.setTitle(tempSeries.getTitle());
                    resultList.add(englishCourse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }


    public static List<MultiItemEntity> jsonWordData(Context context, String fileName) {
        JSONObject jsonDate = null;
        List<MultiItemEntity> resultList = new ArrayList<MultiItemEntity>();
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

            Log.e("jsonDate-word--->", "==" + jsonDate);

            JSONArray seriesArray = jsonDate.getJSONArray("dict_book_unit_words");
            LogUtils.e("dict_book_unit_sections---array--->" + seriesArray);
            List<WordInfo> listSeries = JSON.parseArray(seriesArray.toString(), WordInfo.class);

            for (int i = 0; i < listSeries.size(); i++) {
                WordInfo tempSeries = listSeries.get(i);
                WordInfo wordInfo = new WordInfo(ReadWordItemClickAdapter.TYPE_LEVEL_0);
                wordInfo.setWord(tempSeries.getWord());
                wordInfo.setMeans(tempSeries.getMeans());
                resultList.add(wordInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

}