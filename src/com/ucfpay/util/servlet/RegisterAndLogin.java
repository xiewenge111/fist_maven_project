/**
 * Copyright: Copyright (c)2014
 * Company: UCFGROUP
 */
package com.ucfpay.util.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ucfpay.config.UcfPayConfig;
import com.ucfpay.sign.MD5;
import com.ucfpay.util.HttpUtils;

/**
 * 
* @ClassName: RegisterAndLogin 
* @Description: 商户
* @author gaoyanlong@ucfgroup.com  
* @date 2015年8月31日 上午10:41:53 
*
 */
public class RegisterAndLogin  extends HttpServlet implements Servlet{
	
  
	private static final long serialVersionUID = -8794963152555187789L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		 
		 doPost(request, response);
	 }
 
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		 
		 	/**基础参数**/
		    String userId = request.getParameter("userId"); //先锋支付用户ID
			String key  = UcfPayConfig.key; //先锋支付分配给商户的秘钥
			String signData = "userId=" + userId +"&key=";
			String sign = MD5.sign(signData, key, "utf-8");
			
			
			/**发送请求*/
			Map<String, String> data = new HashMap<String, String>();
			data.put("userId", userId);
			data.put("channel", "ucfpay");
			data.put("sign", sign);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/plain;charset=utf-8");
			response.sendRedirect(HttpUtils.getRequestURLWithParameter(UcfPayConfig.GETTOKEN_URL,data));
			
	 }

}
