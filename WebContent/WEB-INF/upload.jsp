<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Editer les sous-titres</title>
</head>
<body>
	<h2>
		<strong>Résultat de l'upload</strong>
	</h2>
	<c:choose>
		<c:when test="${!empty errorMessage }">
			<p style="color: red;">${ errorMessage }</p>
		</c:when>
		<c:otherwise>
			<p style="color: green;">upload réussi</p>
		</c:otherwise>
	</c:choose>
	<form action="/Subtitlor" method=GET>
		<button type="submit">Retour</button>
	</form>
</body>
</html>