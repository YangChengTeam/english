package com.yc.soundmark.pay;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglin  on 2018/11/26 18:05.
 */
public class XMLUtil {
    public static Map<String, String> doXMLParse(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map<String, String> m = new HashMap<String, String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
//        SAXBuilder builder = new SAXBuilder();
//        Document doc = builder.build(in);
//        Element root = doc.getRootElement();
//        List list = root.getChildren();
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//            Element e = (Element) it.next();
//            String k = e.getName();
//            String v = "";
//            List children = e.getChildren();
//            if (children.isEmpty()) {
//                v = e.getTextNormalize();
//            } else {
//                v = getChildrenText(children);
//            }
//            m.put(k, v);
//        }
        in.close();
        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
//        if (!children.isEmpty()) {
//            Iterator it = children.iterator();
//            while (it.hasNext()) {
////                Element e = (Element) it.next();
////                String name = e.getName();
////                String value = e.getTextNormalize();
////                List list = e.getChildren();
//                sb.append("<" + name + ">");
//                if (!list.isEmpty()) {
//                    sb.append(getChildrenText(list));
//                }
//                sb.append(value);
//                sb.append("</" + name + ">");
//            }
//        }
        return sb.toString();
    }

}
