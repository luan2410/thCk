<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Danh sách Khóa học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
</head>
<body class="container mt-4">
<h3 class="text-center fw-bold">THỐNG KÊ GIẢNG VIÊN THEO KHÓA HỌC</h3>

<a href="/courses/add" class="btn btn-primary mb-3 float-end">Thêm mới Khóa học</a>
<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<table class="table table-bordered text-center align-middle">
    <thead class="table-light">
    <tr>
        <th>Mã KH</th>
        <th>Tên Khóa học</th>
        <th>Số tín chỉ</th>
        <th>Số lượng GV</th>
        <th>Hành động</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${courses}">
        <tr>
            <td>${c.courseCode}</td>
            <td>${c.courseName}</td>
            <td>${c.credit}</td>
            <td>${countMap[c.id]}</td>
            <td>
                <a href="/courses/lecturers/${c.id}" class="btn btn-sm btn-info">Xem giảng viên</a>
                <a href="/courses/delete/${c.id}" class="btn btn-sm btn-danger"
                   onclick="return confirm('Xóa khóa học này?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
