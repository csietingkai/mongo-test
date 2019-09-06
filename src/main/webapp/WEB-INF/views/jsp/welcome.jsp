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

<spring:url value="/resources/core/css/main.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet" />

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
			<a class="navbar-brand" href="#">Spring 4 MVC Ajax Hello World</a>
		</div>
	</div>
</nav>

<div class="container" style="min-height: 500px">
	<div class="starter-template">
		<h1>新北市議會 資源共享區</h1>
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
							<label>部門：</label>
							<select id="deps">
							</select>
							<div class="form-group files color">
								<input id="file" type="file" />
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button id="uploadBtn" type="button" class="btn btn-success">上傳</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var deps = [{
		key: 'secretary',
		value: '秘書室'
	}, {
		key: 'computer',
		value: '資訊室'
	}, {
		key: 'accountant',
		value: '會計室'
	}]
	jQuery(document).ready(function($) {
		setDepOptions();
		getMongoData();
		$("#uploadBtn").click(function(event) {
			event.preventDefault();
			var file = $("#file")[0].files[0];
			var depName = $("#deps :selected").val();
			if (file && depName) {
				upload(file, depName);
			} else {
				alert("");
			}
		});
	});
	
	function setDepOptions() {
		var options = "";
		options += "<option value=\"\">--請選擇--</option>";
		for (var i = 0; i < deps.length; i++) {
			options += "<option value=\"" + deps[i].key + "\">" + deps[i].value + "</option>";
		}
		$("#deps").html(options);
	}

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
		var json = "";
		for (var i = 0; i < deps.length; i++) {
			json += "<h4>" + deps[i].value + "</h4>";
			json += "<table class='table table-striped'>";
			json += 	"<thead>";
			json += 		"<tr>";
			json += 			"<th>檔案名稱</th>";
			json += 			"<th>上傳日期</th>";
			json += 			"<th>檔案大小</th>";
			json += 		"</tr>";
			json += 	"</thead>";
			json += 	"<tbody>";
			var depData = data.filter(function(item) { return item.depName === deps[i].key; });
			if (depData.length > 0) {
				for (var i = 0; i < depData.length; i++) {
					json += "<tr>";;
					json += 	"<td>"+depData[i].filename+"</td>";
					json += 	"<td>"+depData[i].uploadDate+"</td>";
					json += 	"<td>"+depData[i].length+"</td>";
					json += "</tr>";
				}
			} else {
				json += 	"<tr>";
				json += 		"<td colspan=\"3\" align=\"center\">目前無資料</td>";
				json += 	"</tr>";
			}
			json += 	"</tbody>"
			json += "</table>";			
		}
		$('#feedback').html(json);
	}
	
	function upload(file, depName) {
		var formData = new FormData();
		formData.append("file", file);
		$.ajax({
	        type: "POST",
	        url: "${home}mongo/upload?depName="+depName,
	        success : function(result) {
				console.log("SUCCESS: ", result);
				if (result) {
					alert("上傳成功");
					getMongoData();
				}
			},
			error : function(e) {
				console.log("ERROR: ", e);
			},
	        data: formData,
	        cache: false,
	        contentType: false,
	        processData: false,
	        timeout: 60000
	    });
		$("#uploadModal").modal('hide');
	}
</script>

</body>
</html>