<%@page session="false"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Spring MVC 4 + Ajax Hello World</title>

<c:url var="home" value="/" scope="request" />

<spring:url value="/resources/core/css/bootstrap.min.css"
	var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />

<spring:url value="/resources/core/js/jquery.1.10.2.min.js"
	var="jqueryJs" />
<script src="${jqueryJs}"></script>

<spring:url value="/resources/core/js/bootstrap.min.js"
	var="bootstrapJs" />
<script src="${bootstrapJs}"></script>
</head>

<nav class="navbar navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">檔案</a>
		</div>
	</div>
</nav>

<div class="container" style="min-height: 500px">
	<div class="starter-template">
		<h1>Mongo Test</h1>
		<br>
		<div class=btn-group>
			<button id="upload" type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#uploadModal">Upload File</button>
		</div>
		<div id="feedback"></div>
		<div id="uploadModal" class="modal fade" role="dialog" >
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title">
							<b>上傳檔案</b>
						</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid">
							</figure>
							<b-form-file :state="fileState" accept=".zip" v-model="file" ref="file"></b-form-file>
							<label id="warnMsg" style="color: red;"></label>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-success" @click="submitModal()">上傳</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	jQuery(document).ready(function($) {
		getMongoData();
		$("#upload").click(function(event) {
			console.log($("#uploadModal"));
			$("#uploadModal");
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
	
	function openUploadModal() {
		
	}
</script>

</body>
</html>