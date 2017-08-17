package com.ucfpay.sign;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

import com.ucfpay.config.UcfPayConfig;

/** 
* 功能：先锋支付MD5签名处理核心文件，不需要修改
* 版本：1.0
* 修改日期：2014-11-09
* 说明：
* 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
* 该代码仅供学习和研究先锋支付接口使用，只是提供一个示例
* */

public class MD5 {

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
    	System.out.println("签名串："+text);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	System.out.println("异步通知验证签名串："+text);
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equalsIgnoreCase(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
    
    public static void main(String args[]){
    	System.out.println(sign("amount=11580&expirationTime=2015-06-04 17:24:12&goodsName=抠电影-复仇者联盟2：奥创纪元&merchantId=MT10000000&merchantName=kokozu&notifyURL=http://test.kokozu.cn/ucf_web_notify.jsp&outOrderId=21592&returnURL=http://wapch.komovie.cn/pay/return?order_no&token=RHQnSusHCQ2T7FFtH0xLQ4dAosFnencE&userId=20000019940&key=",UcfPayConfig.key, UcfPayConfig.input_charset));
    }

}