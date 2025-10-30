<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Thêm Khóa học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
</head>
<body class="container mt-4">

<h3 class="text-center fw-bold mb-4">THÊM KHÓA HỌC MỚI</h3>

<!-- Show loi -->
<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<!-- Form thêm khóa học -->
<form action="/courses/save" method="post">
    <div class="mb-3">
        <label class="form-label">Mã khóa học</label>
        <input type="text" name="courseCode" value="${course.courseCode}"
               class="form-control" maxlength="20" required>
    </div>

    <div class="mb-3">
        <label class="form-label">Tên khóa học</label>
        <input type="text" name="courseName" value="${course.courseName}"
               class="form-control" maxlength="100" required>
    </div>

    <div class="mb-3">
        <label class="form-label">Số tín chỉ</label>
        <input type="number" name="credit" value="${course.credit}"
               class="form-control" min="1" max="10" required>
    </div>

    <div class="text-end">
        <a href="/courses" class="btn btn-secondary">Hủy</a>
        <button type="submit" class="btn btn-primary">Lưu</button>
    </div>
</form>

</body>
</html>
