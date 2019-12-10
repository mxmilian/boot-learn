<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="s"  uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css" />
    <title><s:message code="menu.users"/></title>
    <script type="text/javascript">
        function changeTrBg(row) {
            row.style.backgroundColor = "#e6e6e6";
        }
        function defaultTrBg(row) {
            row.style.backgroundColor = "white";
        }
        function startSearch(pageParam) {
            var searchWord = document.getElementById('searchString').value;
            var page = parseInt(document.getElementById('cp').value)+parseInt(pageParam);
            if(searchWord.length < 1){
                document.getElementById("errorSearch").innerHTML = "<s:message code="error.searchString.toShort"/>";
                return false;
            } else {
                document.getElementById("errorSearch").innerHTML = "";
                var searchLink = '${pageContext.request.contextPath}/admin/users/search/' + searchWord + '/' + 1;
                window.location.href=searchLink;
            }
        }
    </script>
</head>
<body>
<%@include file="/WEB-INF/webincl/navbar.html"%>
<h1><s:message code="menu.users"/></h1>

<c:set var="count" value="0" scope="page"/>

<c:if test="${currentPage > 1}">
    <c:set var="count" value="${(currentPage - 1) * 5}" scope="page"/>
</c:if>

<div align="center">
    <div align="right" style="width: 1000px; padding: 2px;">
        <input type="hidden" name="cp" id="cp" value="${currentPage}"/>
        <input type="text" id="searchString"/>
        <input type="button" value="<s:message code="button.search"/>" onclick="startSearch(0)"/>
        <span id="errorSearch" style="color: red;"></span>
    </div>
    <table width="1000" border="0" cellpadding="6" cellspacing="0" class="tableUsers">
        <tr bgcolor="#ffddcc">
            <td width="40" align="center"></td>
            <td width="40" align="center"><s:message code="admin.user.id"/> </td>
            <td width="200" align="center"><s:message code="register.name"/> </td>
            <td width="200" align="center"><s:message code="register.lastName"/> </td>
            <td width="220" align="center"><s:message code="register.email"/> </td>
            <td width="90" align="center"><s:message code="profil.czyAktywny"/> </td>
            <td width="200" align="center"><s:message code="profil.rola"/> </td>
            <td width="50"></td>
        </tr>

        <c:forEach var="user" items="${usersList}">
            <c:set var="count" value="${count + 1}" scope="page"/>
            <tr onmouseover="changeTrBg(this)" onmouseout="defaultTrBg(this)">
                <td align="right"><c:out value="${count}"/></td>

                <td align="right"><a href="edit/${user.id}"> <c:out value="${user.id}"/></a></td>
                <td align="right"><a href="edit/${user.id}"><c:out value="${user.name}"/></a></td>
                <td align="left"><a href="edit/${user.id}"><c:out value="${user.lastName}"/></a></td>
                <td align="left"><a href="edit/${user.id}"><c:out value="${user.email}"/></a></td>

                <td align="center">
                    <c:choose>
                        <c:when test="${user.active == 1}">
                            <font color="green"> <s:message code="word.tak"/></font>
                        </c:when>
                        <c:otherwise>
                            <font color="red"> <s:message code="word.nie"/></font>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${user.nrRoll == 1}">
                            <font color="green"> <s:message code="word.admin"/></font>
                        </c:when>
                        <c:otherwise>
                            <font color="red"> <s:message code="word.user"/></font>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${user.nrRoll == 1}">
                            <img src="/resources/images/deliconinact.png" width="16" height="16"/>
                        </c:when>
                        <c:otherwise>
                            <a href="delete/${user.id}">
                                <img src="/resources/images/delicon.png" width="16" height="16" title="<s:message code="delete.user"/>"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
    
    <table width="1000" border="0" cellpadding="6" cellspacing="0" class="tableUsers">
        <tr>
            <td align="center">
                <c:set var="totalPageOfPage" value="${currentPage}" scope="page"/>
                <c:set var="currentPageOfPage" value="${totalPages}" scope="page"/>
                <h4><c:out value="${totalPageOfPage}"/> / <c:out value="${currentPageOfPage}"/></h4>
            </td>
            <td align="right">
                <c:if test="${currentPage > 1}">
                    <input type="button" onclick="window.location.href='${pageContext.request.contextPath}/admin/users/${currentPage - 1}'" value="<s:message code="link.poprzedni"/>"/>
                </c:if>
                <c:if test="${currentPage < totalPages}">
                    <input type="button" onclick="window.location.href='${pageContext.request.contextPath}/admin/users/${currentPage + 1}'" value="<s:message code="link.nastepny"/>"/>
                </c:if>
            </td>
        </tr>
    </table>
</div>
</body>
</html>