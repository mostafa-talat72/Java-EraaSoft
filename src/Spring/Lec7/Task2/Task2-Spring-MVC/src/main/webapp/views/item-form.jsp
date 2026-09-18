<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Item</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Add New Item</h4>
                </div>
                <div class="card-body">

                    <%-- Spring form bound to modelAttribute "item" so BindingResult errors can be shown via form:errors --%>
                    <form:form action="/item/save" method="post" modelAttribute="item">

                        <%-- Global errors (e.g. from service SystemException if needed) --%>
                        <form:errors path="*" cssClass="alert alert-danger d-block" element="div"/>

                        <div class="mb-3">
                            <label for="name" class="form-label">Item Name</label>
                            <form:input path="name" id="name" cssClass="form-control" placeholder="Enter item name"/>
                            <form:errors path="name" cssClass="text-danger small d-block mt-1"/>
                        </div>

                        <button type="submit" class="btn btn-primary">Add Item</button>
                        <a href="/item/list" class="btn btn-secondary">Back</a>
                    </form:form>

                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
