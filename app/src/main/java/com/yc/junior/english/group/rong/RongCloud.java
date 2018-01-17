/**
 * 融云 Server API java 客户端
 * create by kitName
 * create datetime : 2017-03-13 
 * 
 * v2.0.1
 */
package com.yc.junior.english.group.rong;


import com.yc.junior.english.group.rong.methods.Chatroom;
import com.yc.junior.english.group.rong.methods.Group;
import com.yc.junior.english.group.rong.methods.Message;
import com.yc.junior.english.group.rong.methods.Push;
import com.yc.junior.english.group.rong.methods.SMS;
import com.yc.junior.english.group.rong.methods.User;
import com.yc.junior.english.group.rong.methods.Wordfilter;

import java.util.concurrent.ConcurrentHashMap;


public class RongCloud {

	private static ConcurrentHashMap<String, RongCloud> rongCloud = new ConcurrentHashMap<String,RongCloud>();
	
	public User user;
	public Message message;
	public Wordfilter wordfilter;
	public Group group;
	public Chatroom chatroom;
	public Push push;
	public SMS sms;

	private RongCloud(String appKey, String appSecret) {
		user = new User(appKey, appSecret);
		message = new Message(appKey, appSecret);
		wordfilter = new Wordfilter(appKey, appSecret);
		group = new Group(appKey, appSecret);
		chatroom = new Chatroom(appKey, appSecret);
		push = new Push(appKey, appSecret);
		sms = new SMS(appKey, appSecret);

	}

	public static RongCloud getInstance(String appKey, String appSecret) {
		if (null == rongCloud.get(appKey)) {
			rongCloud.putIfAbsent(appKey, new RongCloud(appKey, appSecret));
		}
		return rongCloud.get(appKey);
	}
	 
}