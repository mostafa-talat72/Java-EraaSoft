<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Item</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
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

                    <form action="/item/update" method="post">

                        <!--
                            HTML forms support only GET and POST.
                            This hidden field tells HiddenHttpMethodFilter
                            to treat this POST request as a PUT request.
                        -->
                        <input type="hidden" name="_method" value="PUT">

                        <input type="hidden" name="id" value="${item.id}">

                        <input type="text" name="name" value="${item.name}">

                        <button type="submit">Update</button>

                    </form>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>