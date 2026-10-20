<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Items</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="d-flex justify-content-between align-items-center mb-4">

        <h2>All Items</h2>

        <a href="/item/add"
           class="btn btn-primary">
            + Add Item
        </a>

    </div>

    <div class="card shadow">

        <div class="card-body">

            <table class="table table-hover table-bordered align-middle">

                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach var="item" items="${items}">

                    <tr>

                        <td>
                            ${item.id}
                        </td>

                        <td>
                            ${item.name}
                        </td>

                        <td>

                            <a href="/item/show?id=${item.id}"
                               class="btn btn-info btn-sm">
                                Show
                            </a>

                            <a href="/item/update?id=${item.id}"
                               class="btn btn-warning btn-sm">
                                Update
                            </a>

                            <!--
                                Tells HiddenHttpMethodFilter to treat this POST request as a DELETE request.
                            -->
                            <form action="/item/delete" method="post">
                                <input type="hidden" name="id" value="${item.id}">
                                <input type="hidden" name="_method" value="DELETE">

                                <button type="submit">Delete</button>
                            </form>

                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>

            <c:if test="${empty items}">
                <div class="alert alert-info">
                    No items found.
                </div>
            </c:if>

        </div>
    </div>

</div>

</body>
</html>