<%
    String basePath = request.getScheme () + "://" + request.getServerName () + ":" + request.getServerPort () + request.getContextPath () + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>提交到支付工程</title>
    <base href="<%=basePath%>">
</head>
<body>

<form action="http://localhost:8083/pay/api/alipay" method="post">
    <input type="hidden" name="out_trade_no" value="${out_trade_no}"/>
    <input type="hidden" name="total_amount" value="${total_amount}"/>
    <input type="hidden" name="subject" value="${subject}"/>

</form>
<script>document.forms[0].submit();</script>

</body>
</html>
