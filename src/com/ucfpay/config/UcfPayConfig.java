package com.ucfpay.config;
/**
 * 
* @ClassName: UcfPayConfig 
* @Description: 配置 
* @author gaoyanlong@ucfgroup.com  
* @date 2015年5月26日 下午2:15:23 
*
 */
public class UcfPayConfig {
	
	
	/**默认值**/
	public static String sign_type = "MD5";
		
	public static String cny = "CNY";
		
	public static String default_token = "token";
	
	public static String input_charset = "utf-8";
	
	
 
	
	/**先锋支付提供的商户ID**/
	public static String merchantId = "MT10000000";
	
	/**先锋支付提供的秘钥**/
	public static String key = "hmYB5Ue6OPoHsW2YX5VlaQ";
	
	/**日志输出目录**/
	public static String log_path = "D:\\";
	
	/**异步结果通知地址**/
	public static String NOTIFY_URL = "http://test.xxx.com/notify";
	
	/**同步跳转地址--地址中带参数需URLencode**/
	public static String RETURN_URL = "http://test.xxx.com/return";
	
	
	/**商户提供的注册并登陆地址**/
	public static String GETTOKEN_URL ="";
	
}
