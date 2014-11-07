<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
    <link rel="stylesheet" type="text/css" href="style.css"/>
</head>
<body>
<div class="container">
    <section id="content">
        <form action="ui/CheckAdminAction" method="post">
            <h1>Welcome, Administrator.</h1>

            <div>
                <input type="submit" name="action" value="Show all orders"/>
            </div>
            <div>
                <input type="submit" name="action" value="Show all drivers"/>
            </div>
            <div>
                <input type="submit" name="action" value="Show all trucks"/>
            </div>
            <div>
                <input type="submit" name="action" value="Add new truck"/>
            </div>
            <div>
                <input type="submit" name="action" value="Add new driver"/>
            </div>
            <div>
                <input type="submit" name="action" value="Add new order"/>
            </div>
            <div>
                <input type="submit" name="action" value="Add new goods"/>
            </div>
                     <div>
                <input type="submit" name="action" value="Add fura and drivers to order"/>
            </div>
            <div>
                <input type="submit" name="action" value="Close Order"/>
            </div>
        </form>
    </section>
</div>
</body>
</html>
