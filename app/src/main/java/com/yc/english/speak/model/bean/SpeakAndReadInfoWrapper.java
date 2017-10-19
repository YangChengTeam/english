package com.yc.english.speak.model.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wanglin  on 2017/10/18 18:09.
 */

public class SpeakAndReadInfoWrapper {

    /**
     * code : 0
     * data : {"list":[{"add_date":"20171019","add_time":"1508373922","flag":"0","flag_name":"普通","id":"43","img":"/Upload/Picture/2017-10-19/59e7f5a215bc9.png","sort":"5","title":"赛车总动员 英文版","type_ico":"","type_id":"3","type_name":"经典影视","view_num":"0"},{"add_date":"20171019","add_time":"1508373887","flag":"0","flag_name":"普通","id":"42","img":"/Upload/Picture/2017-10-19/59e7f57f82ac1.png","sort":"4","title":"怪物史瑞克4 英文版","type_ico":"","type_id":"3","type_name":"经典影视","view_num":"0"},{"add_date":"20171019","add_time":"1508373851","flag":"0","flag_name":"普通","id":"41","img":"/Upload/Picture/2017-10-19/59e7f55b85fb1.png","sort":"3","title":"机器人总动员 英文版","type_ico":"","type_id":"3","type_name":"经典影视","view_num":"0"},{"add_date":"20171019","add_time":"1508373801","flag":"0","flag_name":"普通","id":"40","img":"/Upload/Picture/2017-10-19/59e7f52951241.png","sort":"2","title":"冰川时代4 英文版","type_ico":"","type_id":"3","type_name":"经典影视","view_num":"0"},{"add_date":"20171019","add_time":"1508374323","flag":"0","flag_name":"普通","id":"47","img":"/Upload/Picture/2017-10-19/59e7f73341225.png","sort":"1","title":"超人总动员 英文版","type_ico":"","type_id":"5","type_name":"生活情趣","view_num":"0"},{"add_date":"20171019","add_time":"1508374260","flag":"0","flag_name":"普通","id":"46","img":"/Upload/Picture/2017-10-19/59e7f6f47f2b7.png","sort":"1","title":"昆塔盒子总动员","type_ico":"","type_id":"4","type_name":"卡通动漫","view_num":"0"},{"add_date":"20171018","add_time":"1508319325","flag":"0","flag_name":"普通","id":"37","img":"/Upload/Picture/2017-10-18/59e7205d4e28c.png","sort":"1","title":"功夫熊猫3 英文版","type_ico":"","type_id":"3","type_name":"经典影视","view_num":"0"}]}
     * msg :
     */
    private List<SpeakAndReadItemInfo> list;
    private List<SpeakAndReadInfo> sortList = new ArrayList<>();

    public List<SpeakAndReadItemInfo> getList() {
        return list;
    }

    private void sortItemList(List<SpeakAndReadItemInfo> list) {
        Collections.sort(list, new Comparator<SpeakAndReadItemInfo>() {
            @Override
            public int compare(SpeakAndReadItemInfo o1, SpeakAndReadItemInfo o2) {
                return Integer.parseInt(o1.getType_id()) - Integer.parseInt(o2.getType_id());
            }
        });

        sortList(list);
    }

    public List<SpeakAndReadInfo> getSortList() {
        sortItemList(getList());
        return sortList;
    }


    public void setList(List<SpeakAndReadItemInfo> list) {
        this.list = list;
    }

    private int currentPos = 0;

    private void sortList(List<SpeakAndReadItemInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (currentPos == list.size() - 1) {
            return;
        }

        /**
         * 计算策略：
         * 1.先取第一个条目，并创建一个SpeakAndReadInfo的对象
         * 2.拿第一个条目和其他的条目进行比较，如果type_id相同，将这个条目放入第一个SpeakAndReadInfo的itemInfoList列表中
         * 3.将不相同条目的位置保存，从该处进行下一侧循环，
         */

        String type = list.get(currentPos).getType_id();


        SpeakAndReadInfo speakAndReadInfo = new SpeakAndReadInfo(list.get(currentPos).getType_id(), list.get(currentPos).getType_name());
        List<SpeakAndReadItemInfo> itemList = new ArrayList<>();
        itemList.add(list.get(currentPos));
        speakAndReadInfo.setItemInfoList(itemList);

        for (int i = currentPos + 1; i < list.size(); i++) {
            currentPos = i;
            SpeakAndReadItemInfo speakAndReadItemInfo = list.get(i);
            if (type.equals(speakAndReadItemInfo.getType_id())) {
                itemList.add(speakAndReadItemInfo);
            } else {
                //list size 7 currentPos 为6是进入死循环
                break;
            }
        }
        sortList.add(speakAndReadInfo);

        sortList(list);
    }


}
