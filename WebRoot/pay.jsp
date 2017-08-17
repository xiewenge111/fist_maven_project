<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<title>商户页面</title>
<link href="css/reset.css" rel="stylesheet" type="text/css" />
<link href="css/pay.css" type="text/css" rel="stylesheet" />
</head>
 <body>
<div class="warpper">
 
<header id="header_weixin"  >
	<span>商户充值或支付页</span>
</header>
<form accept-charset="utf-8" action="ucfpayapi.jsp" method="post" id="myform">
<input type="hidden" name="outOrderId" id="outOrderId" />
<input type="hidden" name="data" id="data" />
<div class="main">
    <section class="con">  
    	<div class="put"  style="display: none">
            <span class="tit">商户Id</span>
            <span class="in">
                <input id="merchantId" name="merchantId" type="text" value="10000003813" />
            </span>
        </div> 
    	<div class="put" style="display: none">
            <span class="tit">商户名</span>
            <span class="in">
                <input id="merchantName" name="merchantName" type="text" value="商户名称" />
            </span>
        </div> 
        <div class="put"  >
            <span class="tit">用户Id</span>
            <span class="in">
                <input id="userId" name="userId" type="text" value="${param.username}"  />
            </span>
        </div>     
        <div class="put"  >
            <span class="tit">商品名</span>
            <span class="in">
                <input id="goodsName" name="goodsName" type="text" value="彩票" />
            </span>
        </div>
        <div class="put">
            <span class="tit">金额</span>
            <span class="in">
                <input id="amount" name="amount" type="text" value="0.1" readonly="readonly"/>
            </span>
        </div>
        <input type="submit" class="btn btn3" id="nextStep" value="充值" />
    </section>
</div>
</form>
<div class="footerPush"><!--空div 为了让footer能永远置于页面底部--></div>
</div>
<footer class="fot">本服务由先锋支付提供 for 商户测试</footer>
</body>
</html>