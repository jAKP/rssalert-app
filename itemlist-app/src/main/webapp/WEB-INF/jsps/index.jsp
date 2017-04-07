<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">

<head>
<title>Manage Work</title>
</head>

<body>
	<div class="container">
		<!--  <header class="jumbotron hero-spacer">
            <h1>A Warm Welcome!</h1>
            <p>Hello</p>
            <p>
                <a class="btn btn-primary btn-large">Details</a>
            </p>
        </header> -->
		<hr>
		<div class="row text-center">
			<c:forEach var="item" items="${itemsList}">
				<jsp:include page="item.jsp">
					<jsp:param name="picture" value="images/chair.jpg" />
					<jsp:param name="name" value="${item.itemName}" />
					<jsp:param name="description" value="${item.itemDescription}" />
				</jsp:include>
			</c:forEach>
			<%--  <jsp:include page="item.jsp">
                    <jsp:param name="picture" value="http://placehold.it/800x500" />
                    <jsp:param name="name" value="Check" />
                    <jsp:param name="description" value="Click to see how many areas you need to change address." />
                </jsp:include> --%>
		</div>
		<hr>
	</div>
	<div id="footer">
		<div id="footerContainer">
			<div id="imginthefooter"></div>
		</div>
	</div>
</body>

</html>