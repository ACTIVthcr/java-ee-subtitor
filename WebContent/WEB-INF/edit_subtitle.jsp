<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://subtitlor.com/functions" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Editer les sous-titres</title>
</head>
<body>
	<form method="post" action="edit">
		<c:if test="${ !empty errorMessage }">
			<p style="color: red;">${ errorMessage }</p>
		</c:if>
		<input type="submit" style="position: fixed; top: 10px; right: 10px;" />
		<input hidden="true" name="name" value="<c:out value="${ name }" />" />
		<table>
			<c:forEach items="${ subtitles }" var="line" varStatus="status">
				<c:if test="${!empty line && f:isModifiable(line) }">
					<tr>
						<td style="text-align: right;"><c:out value="${ line }" /></td>
						<td><input type="text" name="line${ status.index }"
							id="line${ status.index }" size="35"
							value="<c:if test="${ line !=  translatedSubtitles[status.index]}">${ translatedSubtitles[status.index] }</c:if>" /></td>
						<td><c:out value="${ status.index }" /></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
	</form>
	<form action="/Subtitlor" method="GET">
		<button style="position: fixed; bottom: 10px; right: 10px;"submit">Retour
			accueil</button>
	</form>
</body>
</html>