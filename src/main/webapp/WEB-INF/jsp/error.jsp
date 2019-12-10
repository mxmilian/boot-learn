<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css" />
    <title><s:message code="error.errorPage"/> </title>
</head>
<body>
<%@include file="/WEB-INF/webincl/navbar.html"%>

<h2>
    <s:message code="error.defaultErrorMessage"/>
    <input type="button" value="<s:message code="button.cancel"/>" class="formbutton"
           onclick="window.location.href='${pageContext.request.contextPath}/'"/>
</h2>

</body>
</html>