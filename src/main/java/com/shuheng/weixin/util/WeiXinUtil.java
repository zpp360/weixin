package com.shuheng.weixin.util;

import com.shuheng.weixin.entity.*;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeiXinUtil {
    /**文本消息*/
    public static final String MESSAGE_TEXT = "text";
    /**图片消息*/
    public static final String MESSAGE_IMAGE = "image";
    /**语音消息*/
    public static final String MESSAGE_VOICE = "voice";
    /**视频消息*/
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    /**关注事件*/
    public static final String EVENT_SUB = "subscribe";
    /**取消关注事件*/
    public static final String EVENT_UNSUB = "unsubscribe";
    /**点击事件*/
    public static final String EVENT_CLICK = "CLICK";
    /**URL链接*/
    public static final String EVENT_VIEW = "VIEW";


    /**
     * xml转为map
     * @param request
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request ) throws DocumentException, IOException {
        Map<String,String> map = new HashMap<String, String>();

        SAXReader reader = new SAXReader();

        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);

        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        ins.close();
        return map;
    }
    /**
     * 对象转xml
     * @param obj
     * @return
     */
    public static String messageToXml(Object obj){
        XStream xstream = new XStream();
        xstream.alias("xml", obj.getClass());
        return xstream.toXML(obj);

    }
    /**
     * 初始化文本对象
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     */
    public static String initText(String toUserName, String fromUserName, String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        return messageToXml(text);
    }
    /**
     * 初始化图片对象
     * @param toUserName
     * @param fromUserName
     * @param mediaId
     * @return
     */
    public static String initImage(String toUserName, String fromUserName, String mediaId){
        ImageMessage image = new ImageMessage();
        image.setFromUserName(toUserName);
        image.setToUserName(fromUserName);
        image.setMsgType(MESSAGE_IMAGE);
        image.setCreateTime(new Date().getTime());
        Image im = new Image(mediaId);
        image.setImage(im);
        return messageToXml(image);
    }
    /**
     * 初始化声音对象
     * @param toUserName
     * @param fromUserName
     * @param mediaId
     * @return
     */
    private static String initVoice(String toUserName, String fromUserName, String mediaId) {
        VoiceMessage voice = new VoiceMessage();
        voice.setFromUserName(toUserName);
        voice.setToUserName(fromUserName);
        voice.setMsgType(MESSAGE_VOICE);
        voice.setCreateTime(new Date().getTime());
        Voice vo = new Voice(mediaId);
        voice.setVoice(vo);
        return messageToXml(voice);
    }
    /**
     * 初始化视频对象
     * @param toUserName
     * @param fromUserName
     * @return
     */
    private static String initVideo(String toUserName, String fromUserName, String mediaId,String title,String description) {
        VideoMessage video = new VideoMessage();
        video.setFromUserName(toUserName);
        video.setToUserName(fromUserName);
        video.setMsgType(MESSAGE_VIDEO);
        video.setCreateTime(new Date().getTime());
        Video vi = new Video(mediaId,title,description);
        video.setVideo(vi);
        return messageToXml(video);
    }

    /**
     * 根据消息类型生成xml
     * @param toUserName
     * @param fromUserName
     * @param map
     * @return
     */
    public static String initXMLTpl(String toUserName, String fromUserName, Map<String,String> map) {
        String messageXml = "";
        if(map!=null){
            String type = map.get("wm_type");
            if(MESSAGE_TEXT.equals(type)){
                messageXml = initText(toUserName,fromUserName,map.get("wm_content"));
            }
            if(MESSAGE_IMAGE.equals(type)){
                messageXml = initImage(toUserName,fromUserName,map.get("wm_media_id"));
            }
            if(MESSAGE_VOICE.equals(type)){
                messageXml = initVoice(toUserName,fromUserName,map.get("wm_media_id"));
            }
            if(MESSAGE_VIDEO.equals(type)){
                messageXml = initVideo(toUserName,fromUserName,map.get("wm_media_id"),map.get("wm_title"),map.get("wm_content"));
            }
        }
        return messageXml;
    }


}