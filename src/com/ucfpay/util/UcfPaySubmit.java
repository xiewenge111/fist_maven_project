/**
 * Copyright: Copyright (c)2014
 * Company: UCFGROUP
 */
package com.ucfpay.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.ucfpay.config.UcfPayConfig;
import com.ucfpay.httpclient.HttpProtocolHandler;
import com.ucfpay.httpclient.HttpRequest;
import com.ucfpay.httpclient.HttpResponse;
import com.ucfpay.httpclient.HttpResultType;
import com.ucfpay.sign.AES;
import com.ucfpay.sign.MD5;

/** 
 * @ClassName: UcfPaySubmit 
 * @Description: 先锋支付各接口请求提交类
 * @author gaoyanlong@ucfgroup.com  
 * @date 2014年11月9日 下午2:18:37 
 *  
 */
public class UcfPaySubmit {
	
	
	 /**生产地址**/
     //获取token地址
	// private static final String UCFPAY_GET_TOKEN = "https://m.ucfpay.com/mobilepay-wallet/oneClickOperate/getToken?";
	//创建订单地址
   //  private static final String UCFPAY_GATEWAY = "https://m.ucfpay.com/mobilepay-wallet/oneClickOperate/createOrder?";
     
	 /**测试地址**/
     //获取token地址
	// private static final String UCFPAY_GET_TOKEN = "http://106.39.35.10:9222/mobilepay-wallet/oneClickOperate/getToken?";
	 private static final String UCFPAY_GET_TOKEN = "http://111.203.205.26:8082/mobilepay-wallet/oneClickOperate/getToken?";
	//创建订单地址
    // private static final String UCFPAY_GATEWAY = "http://106.39.35.10:9222/mobilepay-wallet/oneClickOperate/createOrder?";
     private static final String UCFPAY_GATEWAY = "http://111.203.205.26:8082/mobilepay-wallet/oneClickOperate/createOrder?";
 
	 
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = UcfPayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	//将mKey拼接到参数末尾，再做MD5
    	prestr += "&key=";
    	//签名结果
        String mysign = "";
        if(UcfPayConfig.sign_type.equals("MD5") ) {
        	mysign = MD5.sign(prestr, UcfPayConfig.key, UcfPayConfig.input_charset);
        }
        return mysign;
    }
	/**
	 * 
	* @Title: verifyResponseSign 
	* @Description: 验证返回数据签名结果
	* @param @param sPara
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean verifyResponseSign(Map<String, String> sPara){
		//签名串
		String sign = (String) sPara.get("sign");
		//验证签名
		Map<String, String> map = UcfPayCore.paraFilter(sPara);
		String prestr = UcfPayCore.createLinkString(map); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	//将mKey拼接到参数末尾，再做MD5
    	prestr += "&key=";
    	return MD5.verify(prestr, sign, UcfPayConfig.key, UcfPayConfig.input_charset);
    	
	}
	
	
	
	
	 /**
     * 生成要请求给先锋的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildCreateOrderRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = UcfPayCore.paraFilter(sParaTemp);
        //增加token
        String token = getToken(sPara);
        //获取token失败-商户进行后续业务处理无须跳转到订单创建接口
        if(UcfPayConfig.default_token.equals(token)){
        	/** 
        	 *商户自己的业务流程
        	 *商户自己的业务流程
        	 *商户自己的业务流程
        	 *商户自己的业务流程
        	 * 
        	 */
        	System.out.println("获取token服务异常.......................");
        	
        }
        sPara.put("token", token);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);
        //签名结果 加入请求提交参数组中
        sPara.put("sign", mysign);
        //AES加密
        String encryptStr = buildRequestMyAes(sPara);
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("data", encryptStr);
        returnMap.put("merchantId", sParaTemp.get("merchantId"));
        return returnMap;
    }
    /**
     * 生成要请求给先锋的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildTokenRequestPara(Map<String, String> sParaTemp) {
    
    	//除去数组中的空值和签名参数
    	Map<String, String> sPara = UcfPayCore.paraFilter(sParaTemp);
    	//生成签名结果
    	String mysign = buildRequestMysign(sPara);
    	//签名结果 加入请求提交参数组中
    	sPara.put("sign", mysign);
    	//AES加密
    	String encryptStr = buildRequestMyAes(sPara);
    	Map<String, String> returnMap = new HashMap<String, String>();
    	returnMap.put("data", encryptStr);
    	returnMap.put("merchantId", sParaTemp.get("merchantId"));
    	return returnMap;
    }
    
    /**
     * 
    * @Title: buildRequestMyAes 
    * @Description: 对request数据aes加密
    * @param @param sPara
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String buildRequestMyAes(Map<String, String> sPara) {
    	String prestr = UcfPayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	System.out.println(prestr);
    	return AES.aesEncrypt(prestr);
    }
    /**
     * 
    * @Title: buildResponseMyAes 
    * @Description: 对response数据aes解密 
    * @param @param input
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    private static String buildResponseMyAes(String input){
    	
    	return AES.aesDecrypt(input);
    	
    }
    
    /**
     * 
    * @Title: getToken 
    * @Description: 获取token 
    * @param @param sPara
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    private static String getToken(Map<String, String> sPara){
    	
    	String token = UcfPayConfig.default_token;
    	String merchantId = sPara.get("merchantId");
    	String userId = sPara.get("userId");
    	Map<String, String> tokenMap = new HashMap<String, String>();
    	tokenMap.put("merchantId", merchantId);
    	tokenMap.put("userId", userId);
    	try {
			String responseData =getTokenResponse("","",tokenMap);
			JSONObject json = JSONObject.parseObject(responseData);
			responseData = (String) json.get("data");
			//解密
			String decryptData = buildResponseMyAes(responseData);
			System.out.println(decryptData);
			Map<String, String> sParaTemp = split(decryptData);
			if(verifyResponseSign(sParaTemp)){
				token =	sParaTemp.get("token");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
       
    	return token;
    }
    
    
    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName) {
    	
        //增加md5签名和AES加密
        Map<String, String> sPara = buildCreateOrderRequestPara(sParaTemp);
        
        
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" +UCFPAY_GATEWAY
                      + "_input_charset=" + UcfPayConfig.input_charset + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

        return sbHtml.toString();
    }
    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @param sParaTemp 请求参数数组
     * @return 处理结果
     * @throws Exception
     */
    public static String getTokenResponse(String strParaFileName, String strFilePath,Map<String, String> sParaTemp) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = buildTokenRequestPara(sParaTemp);

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(UcfPayConfig.input_charset);

        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(UCFPAY_GET_TOKEN+"_input_charset="+UcfPayConfig.input_charset);

        HttpResponse response = httpProtocolHandler.execute(request,strParaFileName,strFilePath);
        if (response == null) {
            return null;
        }
        String strResult = replaceBlank(response.getStringResult());

        return strResult;
    }
    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
    
    /**
     * 
    * @Title: replaceBlank 
    * @Description: 替换响应参数中字符
    * @param @param str
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
	/***
	 * 
	* @Title: split 
	* @Description: &截取数据
	* @param @param str
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws
	 */
	public static Map<String,String> split(String str){
		Map<String,String> map = new HashMap<String,String>();
		if(null != str){
			String[] s = str.split("&");
			for(String skv : s){
				String[] skvs = skv.split("=");
				if(skvs.length >= 2 && null != skvs[0] && null != skvs[1]){
					map.put(skvs[0], skvs[1]);
				}
			}
		}
		return map;
	}
	
	
}
