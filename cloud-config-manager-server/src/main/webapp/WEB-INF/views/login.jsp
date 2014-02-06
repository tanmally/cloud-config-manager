
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<title>Cloud Config Login</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css"
	rel="stylesheet">
<style type="text/css">
body {
	background: url(http://mymaplist.com/img/parallax/back.png);
	background-color: #444;
}

.vertical-offset-100 {
	padding-top: 100px;
}

.errorblock {
 color : red;
}
</style>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
<body onload='document.f.j_username.focus();'
	style="background-position: 115px 20px, 61px 17px, 30px 8px;">




	<div class="container">
		<div class="row vertical-offset-100">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Please log in</h3>
					</div>
					<div class="panel-body">
						<c:if test="${not empty error}">
							<div class="errorblock">
								Your login attempt was not successful, try again.<br /> Invalid
								username or password
							</div>
							</br>
						</c:if>	

						<form name='f' action="<c:url value='j_spring_security_check' />"
							method='POST'>
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="username"
										name='j_username' type="text">
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="password"
										name='j_password' type="password" value="">
								</div>
								<input class="btn btn-lg btn-success btn-block"  type="submit"
									value="Login">
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>


</body>
</html>