// JavaScript Document
require.config({
	paths : {
		'jquery' : '../lib/jquery-1.8.3.min',
		'common' : '../js/common',
		'port' :'../js/port',
		'loading' : '../widget/loading',
		'messageBox' : '../widget/messageBox',
		'jqueryTmpl' : '../lib/jquery.tmpl.min',
		'md5' : '../aes/jquery.md5',
		'aes' : '../aes/aes',
		'crypt' : '../aes/crypt',
		'BigInt' : '../RSA/BigInt',
		'Barrett' : '../RSA/Barrett',
		'RSA' : '../RSA/RSA'
	},
	shim : {
		'jqueryTmpl':{
			deps : ['jquery'],
			exports : '_'
		},
		'md5':{
			deps : ['jquery'],
			exports : '_'
		},
		'BigInt':{
			exports : '_'
		},
		'Barrett':{
			exports : '_'
		},
		'RSA':{
			exports : '_'
		}
	}
});
require(['jquery','common','port','loading','messageBox','jqueryTmpl','md5','aes','crypt','BigInt','Barrett','RSA'], function(jquery,common,port,loading,messageBox,jqueryTmpl,md5,aes,crypt,BigInt,Barrett,RSA) {
	
	//缓存元素对象
	var ntip = $("#tip");
	var nsuc = $(".suc");
	var fresh = $(".fresh");
	
	//获取页面传过来的数据
	var nUrl = window.location.href;
	var tip = getQueryString(nUrl,'tip');
	var suc = getQueryString(nUrl,'icon');
	var nR = getQueryString(nUrl,'nR');
	ntip.text(tip);
	nsuc.removeClass('suc1').removeClass('suc2').removeClass('suc3').addClass(suc);
	if(nR == 1){
		fresh.removeClass("on").addClass("on");
	}
	
	var loading = new loading();//构造加载对象
	var msg = new messageBox();//构造弹窗对象
	
	var merchantId = window.localStorage['merchantId'];
	var outOrderId = window.localStorage['outOrderId'];
	var returnURL = window.localStorage['returnURL'];
	var params = 'merchantId='+merchantId+'&outOrderId='+outOrderId;
	
	fresh.bind('click',function(){
		loading.fnShow();
		fresh.attr("disabled","disabled");
		setTimeout(function(){
			fresh.removeAttr('disabled');
		},10000);
		var sUnBindUrl = urlHOST()+"/pay/queryOrderInfo";
		var nkey = getMkey();//定义随机的mkey
		$.ajax({
		        type : "get",
		        async : false,
		        url : sUnBindUrl,
				data :{
					data : getData(params,'',0,nkey),
					skey : getSkey(nkey)
				},
				timeout : 30,
		        dataType : "jsonp",
				mimeType : "text/plain; charset=utf-8",
				jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(默认为:callback)
		        jsonpCallback:"jsonpCallback",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
				success : function(data){
					var responseMessage = data.responseMessage;
					var data2 = Decrypt(responseMessage,nkey);
					var obj = eval('('+data2+')');
					var respCode = obj.respCode;
					var status = obj.status;
					var respMsg = obj.respMsg;
					setTimeout(function(){
						loading.fnHide();
						if(status == '00'){
							freshResult('恭喜您支付成功！','suc1');
						}else if(status == '01'){
							freshResult('支付失败，请返回重新支付！','suc2');
						}else if(status == '02'){
							freshResult('支付请求正在受理，请稍后！','suc3');
						}else{
							//msg.fnShowInfo(respMsg,'','我知道了',function(btn){});
						}
					},2000);
				},
		        error:function(){
					loading.fnHide();
		            msg.fnShowInfo('网络异常，请确认您的网络连接后继续支付','','我知道了',function(btn){});
		        }
		});	
	});
	//刷新支付结果
	function freshResult(vtip,vclass){
		ntip.text(vtip);
		nsuc.removeClass('suc1').removeClass('suc2').removeClass('suc3').addClass(vclass);
	}
	
	//返回到商户页面
	$(".back").bind('click',function(){
		nback();
	});
	$(".btn").bind('click',function(){
		nback();
	});
	function nback(){
		window.location.href = returnURL;
	}
	
});