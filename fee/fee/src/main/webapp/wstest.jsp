<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getServerName() + ":"
			+ request.getServerPort() + request.getContextPath();
%>
<html>
<head>
<title>车辆实时跟踪测试</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="jscript/jquery-2.0.3.min.js"></script>
<script type="text/javascript">
	var ws = null;
	function startServer() {
		var url = $('#url').val();//"ws://localhost:8080/gserver/websocket/getJson4";//"ws://<%=basePath%>/websocket";
		var textBox = document.getElementById('textList');
		if ('WebSocket' in window) {
			ws = new WebSocket(url);
		} else if ('MozWebSocket' in window) {
			ws = new MozWebSocket(url);
		} else {
			alert('Unsupported.');
			return;
		}
		ws.onopen = function() {
			textBox.value = "打开连接成功! ";
		};
		ws.onmessage = function(event) {
			var textMessage = textBox.value;
			textBox.value = event.data + "\n\n" + textMessage;
		};
		ws.onclose = function() {
			var textMessage = textBox.value;
			textBox.value = "连接已经关闭! ";
		};
	}
	function sendMyMessage() {
		var textMessage = document.getElementById('textMessage').value;
		if (ws != null && textMessage != '') {
			ws.send(textMessage);
		}
	}

	function login(demo) {
		startServer();
		return;
		$.getJSON("cheAnGis/client/member_login.action", {
			"demo":"test",
			"login_name" : $("#loginName").val(),
			"login_password" : $("#password").val()
		}, function(data) {
			alert(data['flag'])
			if (data['flag'] == 0) {
				startServer();
			} else {
				alert('用户名密码错误，登录失败!');
			}
		});
	}

	function closeConn() {
		ws.close();
	}
</script>
</head>
<body>
	<div>
		<textarea rows="20" cols="80" id="textList"></textarea>
	</div>
	<div>
	    请求地址:<input type="text" id="url" size="100" value="ws://localhost:8088/gserver/websocket/getJson4"/><br/>
		用户名：<input type="text" id="loginName" size="20" /> 密码：<input
			type="password" id="password" size="20" /> <input type="button"
			onclick="login(0)" value="普通登录"><input type="button"
			onclick="login(1)" value="体验登录">
	</div>
	<div>
		<input type="text" id="textMessage" size="50" /> <input type="button"
			onclick="sendMyMessage()" value="发送命令"> <input type="button"
			onclick="closeConn()" value="关闭连接">
	</div>
</body>
</html>
