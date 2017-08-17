<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ucfpay.config.*"%>
<%@ page import="com.ucfpay.util.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>商户创建订单接口</title>
	</head>
	<%
		////////////////////////////////////请求参数//////////////////////////////////////
		
		//服务器异步通知页面路径 商户自行修改
		String notify_url = UcfPayConfig.NOTIFY_URL;
		//需http://格式的完整路径，不允许加?id=123这类自定义参数		String return_url = UcfPayConfig.RETURN_URL;
		String goodsName = new String(request.getParameter("goodsName").getBytes("ISO-8859-1"),"UTF-8");
		//付款账号
		String amount = new String(request.getParameter("amount").getBytes("ISO-8859-1"),"UTF-8");
		//转换金额为分
		amount = AmountUtils.changeY2F(amount);
		String merchantId = UcfPayConfig.merchantId;
		String userId = new String(request.getParameter("userId").getBytes("ISO-8859-1"),"UTF-8");
		
		String merchantName = new String(request.getParameter("merchantName").getBytes("ISO-8859-1"),"UTF-8");
		
		//必填		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		//外部订单号
		sParaTemp.put("outOrderId", UtilDate.getOrderNum());
		//商户id
		sParaTemp.put("merchantId", merchantId);
		//用户id
		sParaTemp.put("userId", userId);
		//金额 单位为分
		sParaTemp.put("amount", amount);
		//商品名称
		sParaTemp.put("goodsName", goodsName);
		//前台返回url
		sParaTemp.put("returnURL", return_url);
		//后台通知url
		sParaTemp.put("notifyURL", notify_url);
		//商户名称
		sParaTemp.put("merchantName", merchantName);
		//建立请求
		String sHtmlText = UcfPaySubmit.buildRequest(sParaTemp,"get","确认");
		out.println(sHtmlText);
	%>
	<body>
	</body>
</html>
