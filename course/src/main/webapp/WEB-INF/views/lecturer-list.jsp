<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Danh sách Giảng viên</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
</head>
<body class="container mt-4">
<h3 class="text-center fw-bold">DANH SÁCH GIẢNG VIÊN CỦA KHÓA HỌC: ${course.courseName}</h3>

<a href="/courses" class="btn btn-secondary mb-3">Quay lại danh sách Khóa học</a>

<table class="table table-bordered text-center align-middle">
    <thead class="table-light">
    <tr>
        <th>Mã GV</th>
        <th>Tên Giảng viên</th>
        <th>Email</th>
        <th>Khoa/Bộ môn</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="l" items="${lecturers}">
        <tr>
            <td>${l.lecturerCode}</td>
            <td>${l.lecturerName}</td>
            <td>${l.email}</td>
            <td>${l.department}</td>
        </tr>
    </c:forEach>
    <c:if test="${empty lecturers}">
        <tr><td colspan="4" class="text-muted">Chưa có giảng viên nào</td></tr>
    </c:if>
    </tbody>
</table>
</body>
</html>
