<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Item</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
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

                    <form action="/item/save"
                          method="post">

                        <div class="mb-3">
                            <label for="name" class="form-label">
                                Item Name
                            </label>

                            <input type="text"
                                   id="name"
                                   name="name"
                                   class="form-control"
                                   placeholder="Enter item name"
                                   required>
                        </div>

                        <button type="submit"
                                class="btn btn-primary">
                            Add Item
                        </button>

                        <a href="/item/list"
                           class="btn btn-secondary">
                            Back
                        </a>

                    </form>

                </div>
            </div>

        </div>
    </div>

</div>

</body>
</html>