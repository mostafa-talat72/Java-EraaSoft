<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Items</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>All Items</h2>
        <a href="/item/add" class="btn btn-primary">+ Add Item</a>
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
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td class="d-flex gap-1">
                            <a href="/item/show?id=${item.id}" class="btn btn-info btn-sm">Show</a>
                            <a href="/item/update?id=${item.id}" class="btn btn-warning btn-sm">Update</a>
                            <form action="/item/delete" method="post" class="d-inline delete-form">
                                <input type="hidden" name="id" value="${item.id}">
                                <input type="hidden" name="_method" value="DELETE">
                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty items}">
                <div class="alert alert-info">No items found.</div>
            </c:if>
        </div>
    </div>
</div>

<!-- Site modal instead of browser confirm() -->
<div class="modal fade" id="confirmModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered" style="max-width: 420px;">
        <div class="modal-content text-center p-4">
            <h5>Confirm Delete</h5>
            <p class="text-muted">Are you sure you want to delete this item? This will call DELETE /item/delete via HiddenHttpMethodFilter.</p>
            <div class="d-flex justify-content-center gap-2">
                <button id="confirmYes" class="btn btn-danger">Delete</button>
                <button id="confirmNo" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const modalEl = document.getElementById('confirmModal');
        const modal = new bootstrap.Modal(modalEl);
        let pendingForm = null;
        document.querySelectorAll('.delete-form').forEach(form => {
            form.addEventListener('submit', function (e) {
                e.preventDefault();
                pendingForm = form;
                modal.show();
            });
        });
        document.getElementById('confirmYes').addEventListener('click', function () {
            if (pendingForm) {
                const f = pendingForm;
                pendingForm = null;
                modal.hide();
                f.submit();
            }
        });
        document.getElementById('confirmNo').addEventListener('click', () => pendingForm = null);
        modalEl.addEventListener('hidden.bs.modal', () => pendingForm = null);
    });
</script>

</body>
</html>
