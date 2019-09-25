<%
    String basePath = request.getScheme () + "://" + request.getServerName () + ":" + request.getServerPort () + request.getContextPath () + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>提交到alipay请求</title>
    <base href="<%=basePath%>">
</head>
<body>
${toAlipay}
</body>
</html>
