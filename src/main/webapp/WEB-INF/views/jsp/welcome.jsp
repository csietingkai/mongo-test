<%@page session="false"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Spring MVC 4 + Ajax Hello World</title>

<c:url var="home" value="/" scope="request" />

<spring:url value="/resources/core/css/hello.css" var="coreCss" />
<spring:url value="/resources/core/css/bootstrap.min.css"
	var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />

<spring:url value="/resources/core/js/jquery.1.10.2.min.js"
	var="jqueryJs" />
<script src="${jqueryJs}"></script>
</head>

<nav class="navbar navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Spring 4 MVC Ajax Hello World</a>
		</div>
	</div>
</nav>

<div class="container" style="min-height: 500px">
	<div class="starter-template">
		<h1>Mongo Test</h1>
		<br>

		<div id="feedback"></div>

		<div class=btn-group>
			<button id="infos" class="btn btn-primary btn-lg">List Info</button>
		</div>
	</div>
</div>

<script>
	jQuery(document).ready(function($) {
		$("#infos").click(function(event) {
			event.preventDefault();
			getMongoData();
		});
	});

	function getMongoData() {
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "${home}mongo/list",
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				display(data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
				display(e);
			},
			done : function(e) {
				console.log("DONE");
			}
		});

	}

	function display(data) {
		data = data.map(function(obj){
			obj.length = Math.ceil(obj.length/1024)+" KB";
			obj.uploadDate = new Date(obj.uploadDate).toLocaleString();;
			return obj;
		});
		var json = "<h4>MongoDB Data</h4>";
		json += "<table class='table table-striped'>";
		json += 	"<thead>"
		json += 		"<tr>";
		json += 			"<th>id</th>";
		json += 			"<th>filename</th>";
		json += 			"<th>upload date</th>";
		json += 			"<th>size</th>";
		json += 		"</tr>";
		json += 	"</thead>";
		json += 	"<tbody>";
		for (var i = 0; i < data.length; i++) {
			json += 	"<tr>";
			json += 		"<td>"+data[i].id+"</td>";
			json += 		"<td>"+data[i].filename+"</td>";
			json += 		"<td>"+data[i].uploadDate+"</td>";
			json += 		"<td>"+data[i].length+"</td>";
			json += 	"</tr>";
		}
		json += 	"</tbody>"
		json += "</table>";
		$('#feedback').html(json);
	}
</script>

</body>
</html>