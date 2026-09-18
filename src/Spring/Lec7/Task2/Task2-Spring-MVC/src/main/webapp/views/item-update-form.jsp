<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Item</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header bg-warning">
                    <h4 class="mb-0">Update Item</h4>
                </div>
                <div class="card-body">

                    <form:form action="/item/update" method="post" modelAttribute="item">
                        <%-- _method=PUT + id must be inside form:form so BindingResult stays on same object --%>
                        <input type="hidden" name="_method" value="PUT">
                        <form:hidden path="id"/>

                        <form:errors path="*" cssClass="alert alert-danger d-block" element="div"/>

                        <div class="mb-3">
                            <label for="name" class="form-label">Item Name</label>
                            <form:input path="name" id="name" cssClass="form-control" />
                            <form:errors path="name" cssClass="text-danger small d-block mt-1"/>
                        </div>

                        <button type="submit" class="btn btn-warning">Update</button>
                        <a href="/item/list" class="btn btn-secondary">Back</a>
                    </form:form>

                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
