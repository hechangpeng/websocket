<%--
  Created by IntelliJ IDEA.
  User: game201708
  Date: 2019/3/19
  Time: 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, user-scalable=no">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>测试推送</title>
    <script type="text/javascript">
        function randomWord(randomFlag, min, max) {
            var str = "",
                range = min,
                arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

            // 随机产生
            if (randomFlag) {
                range = Math.round(Math.random() * (max - min)) + min;
            }
            for (var i = 0; i < range; i++) {
                pos = Math.round(Math.random() * (arr.length - 1));
                str += arr[pos];
            }
            return str;
        };
        var clientId = localStorage.getItem("clientId");
        if (clientId == null || clientId == '') {
            clientId = randomWord(false, 12, 12);
            localStorage.setItem("clientId", clientId);
        }
        console.info("clientId=" + clientId);
        var websocket = null;
        if ('WebSocket' in window) {
            websocket = new WebSocket("ws://192.168.2.88:8080/tlmb/websocket/" + clientId)
        } else {
            alert("not support");
        }
        websocket.onerror = function () {
            document.getElementById("message").innerHTML += "websocket连接出现错误<br>";
        };
        websocket.onopen = function () {
            document.getElementById("message").innerHTML += "连接成功<br>";
        };
        websocket.onmessage = function (event) {
            document.getElementById("message").innerHTML += event.data + "<br>";
        };
        websocket.onclose = function () {
            document.getElementById("message").innerHTML += "连接关闭<br>";
        };
        window.onbeforeunload = function () {
            websocket.close();
        };

        function send() {
            var message = document.getElementById("msg").value;
            websocket.send(message);
        }

        function closeconnnect() {
            websocket.close();
        }
    </script>
</head>
<body>
<br><br><br><br><br>
<center>
    <p><input type="text" id="msg" name="msg" size="50">
        <button onclick="send()">发送消息</button>
    </p>
    <br>
    <div id="message"></div>
    <button onclick="closeconnnect()">手动关闭连接</button>
</center>
</body>
</html>
