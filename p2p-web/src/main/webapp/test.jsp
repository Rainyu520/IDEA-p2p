<%
    String basePath = request.getScheme () + "://" + request.getServerName () + ":" + request.getServerPort () + request.getContextPath () + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <base href="<%=basePath%>">
</head>
<body>
动力金融网历史评价年化收益率:${historyAverageRate} <br/>
平台总人数:${allUserCount} <br/>
平台投资总额:${totalBidMoney}<br/>
chanpin
<c:forEach items="${xLoanProductList}" var="loanProduct" >
    ${loanProduct.rate}<br/>e
</c:forEach>
</body>
</html>
