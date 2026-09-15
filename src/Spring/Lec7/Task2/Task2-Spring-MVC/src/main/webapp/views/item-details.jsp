<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Item Details</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-6">

            <div class="card shadow">

                <div class="card-header bg-info text-white">
                    <h4 class="mb-0">Item Details</h4>
                </div>

                <div class="card-body">

                    <div class="mb-3">
                        <strong>ID:</strong>

                        <span class="badge bg-secondary">
                            ${item.id}
                        </span>
                    </div>

                    <div class="mb-3">
                        <strong>Name:</strong>

                        <span>
                            ${item.name}
                        </span>
                    </div>

                    <hr>

                    <a href="/item/update?id=${item.id}"
                       class="btn btn-warning">
                        Update
                    </a>

                    <a href="/item/list"
                       class="btn btn-secondary">
                        Back to List
                    </a>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>