package com.shuheng.weixin.controller;

import com.shuheng.weixin.entity.ApiData;
import com.shuheng.weixin.util.WeiXinUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.*;

@PropertySource({"classpath:/application.properties"})
@RestController
public class WeixinController {
    @Value("${weixin.appId}")
    private String appId;
    @Value("${weixin.appSecret}")
    private String appSecret;
    @Value("${weixin.token}")
    private String token;
    @Value("${server.url.welcome}")
    private String urlWelcome;
    @Value("${server.url.message}")
    private String urlMessage;

    @Autowired
    private RestTemplate restTemplate;

    private static final int LIST_SIZE = 1000;
    private static List<String> repeatList = new ArrayList<String>(LIST_SIZE);

    private static final String CODE_SUCESS="200";

    /**
     * 处理get请求，接入微信公众号
     */
    @GetMapping("/devModelValidata")
    public void getMethod(@RequestParam("signature") String signature,
                          @RequestParam("timestamp") String timestamp,
                          @RequestParam("nonce") String nonce,
                          @RequestParam("echostr")String echostr,
                          HttpServletResponse response,
                          HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String checkSignature = createSignature(token,timestamp,nonce);
        if(checkSignature.equals(signature)){
            out.write(echostr);
        }
        out.close();

    }

    /**
     * 处理post请求
     */
    @PostMapping("/devModelValidata")
    public void postMethod(HttpServletResponse response,HttpServletRequest request) throws IOException, DocumentException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String message = "";
        Map<String,String> map = WeiXinUtil.xmlToMap(request);
        String toUserName = map.get("ToUserName");
        String fromUserName = map.get("FromUserName");
        String msgType = map.get("MsgType");
        String createTime = map.get("CreateTime");
        if(isRepeatRequest(fromUserName,createTime)){
            out.write(message);
        }else{
            //判断请求是否事件类型 event
            if(WeiXinUtil.MESSAGE_EVENT.equals(msgType)){
                String eventType = map.get("Event");
                //若是关注事件  subscribe
                if(WeiXinUtil.EVENT_SUB.equals(eventType)){
                    Map<String,String> param = new HashMap<String,String>();
                    param.put("appId",appId);
                    param.put("appSecret",appSecret);
                    ApiData data = restTemplate.getForObject(urlWelcome,ApiData.class,param);
                    if(data!=null&&CODE_SUCESS.equals(data.getErrorCode())){
                        String content = data.getData().get("wx_welcome").toString();
                        message = WeiXinUtil.initText(toUserName, fromUserName, content);
                    }
                    out.print(message);
                }
                //取消关注
                if(WeiXinUtil.EVENT_UNSUB.equals(eventType)){
                    out.print(message);
                }
                //自定义菜单点击事件
                if(WeiXinUtil.EVENT_CLICK.equals(eventType)){
                    String key = map.get("EventKey");
                    Map<String,String> param = new HashMap<String,String>();
                    param.put("appId",appId);
                    param.put("appSecret",appSecret);
                    param.put("wm_key",key);
                    ApiData data = restTemplate.getForObject(urlMessage,ApiData.class,param);
                    if(data!=null&&CODE_SUCESS.equals(data.getErrorCode())&&data.getData()!=null&&!data.getData().isEmpty()){
                        message = WeiXinUtil.initXMLTpl(toUserName, fromUserName, data.getData());
                    }
                    out.write(message);
                }
            }
            if(WeiXinUtil.MESSAGE_TEXT.equals(msgType)){
                //普通消息返回空字符串，避免“该公众号暂时无法提供服务，请稍后再试”
                out.print("");
            }
        }
        out.close();
    }


    /**
     * 创建signature
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    private String createSignature(String token,String timestamp,String nonce){
        List<String> list = new ArrayList<String>();
        String checksignature = "";
        if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce)){
            list.add(token);
            list.add(timestamp);
            list.add(nonce);
            Collections.sort(list);
            checksignature=getSha1(list.get(0)+list.get(1)+list.get(2));
        }
        return checksignature;
    }

    /**
     * sha1签名
     * @param str
     * @return
     */
    private String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *  微信消息排重,当list达到1000时清空
     * @param fromUserName
     * @param createTime
     * @return
     */
    private boolean isRepeatRequest(String fromUserName,String createTime){
        String sign = fromUserName+createTime;
        if(repeatList.contains(sign)){
            return true;
        }
        if(repeatList.size()>=LIST_SIZE){
            repeatList.clear();
        }
        repeatList.add(sign);
        return false;
    }

}
