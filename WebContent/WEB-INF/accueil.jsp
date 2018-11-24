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
		<strong>Bienvenue sur le site d'édition de sous-titre</strong>
	</h2>
	<h4>
		<strong>Sélection des sous-titres à modifier</strong>
	</h4>
	<ul>
		<c:forEach items="${ subtitlesListName }" var="name"
			varStatus="status">
			<form method="GET" action="edit">
				<li><c:out value="${name}"></c:out> <input hidden="true"
					name="name" value="<c:out value="${ name }" />" />
					<button type="submit">Editer</button></li>
			</form>
			<form action="download" method="POST">
				<input hidden="true" name="name" value="<c:out value="${ name }" />" />
				<button type="submit">Télécharger traduction</button>
			</form>
		</c:forEach>
	</ul>
	<p style="color: red;">${errorMessage}</p>
	<h4>
		<strong>Upload de sous-titres</strong>
	</h4>
	<form action="upload" method="POST" enctype="multipart/form-data">
		<p>
			<label for="fichier">Fichier à envoyer : </label> <input type="file"
				name="fichier" id="fichier" />
		</p>
		<input type="submit" />
	</form>
</body>
</html>