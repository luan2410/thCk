#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
JSP Code Generator - Tạo code JSP nhanh chóng
Chạy: python jsp_generator.py
"""

def generate_list_jsp(entity_name, fields):
    """Tạo JSP danh sách"""
    entity_lower = entity_name.lower()
    entity_list = f"{entity_lower}s"
    
    # Generate table headers
    headers = "\n        ".join([f"<th>{field['label']}</th>" for field in fields])
    
    # Generate table cells
    cells = "\n            ".join([f"<td>${{{entity_lower}.{field['name']}}}</td>" for field in fields])
    
    code = f"""<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Danh sách {entity_name}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
</head>
<body class="container mt-4">
<h3 class="text-center fw-bold">DANH SÁCH {entity_name.upper()}</h3>

<a href="/{entity_list}/add" class="btn btn-primary mb-3 float-end">Thêm mới</a>

<c:if test="${{not empty error}}">
    <div class="alert alert-danger">${{error}}</div>
</c:if>

<table class="table table-bordered table-hover text-center align-middle">
    <thead class="table-light">
    <tr>
        {headers}
        <th>Hành động</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="{entity_lower}" items="${{{entity_list}}}">
        <tr>
            {cells}
            <td>
                <a href="/{entity_list}/edit/${{{entity_lower}.id}}" class="btn btn-sm btn-warning">Sửa</a>
                <a href="/{entity_list}/delete/${{{entity_lower}.id}}" class="btn btn-sm btn-danger"
                   onclick="return confirm('Xóa {entity_name} này?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<c:if test="${{empty {entity_list}}}">
    <div class="alert alert-info text-center">Không có dữ liệu</div>
</c:if>

</body>
</html>"""
    return code


def generate_form_jsp(entity_name, fields):
    """Tạo JSP form thêm/sửa"""
    entity_lower = entity_name.lower()
    entity_list = f"{entity_lower}s"
    
    # Generate form fields
    form_fields = []
    for field in fields:
        field_html = f"""    <div class="mb-3">
        <label class="form-label">{field['label']} <span class="text-danger">*</span></label>
        <input type="{field.get('type', 'text')}" name="{field['name']}" class="form-control" 
               value="${{{entity_lower}.{field['name']}}}" required/>
    </div>"""
        form_fields.append(field_html)
    
    form_fields_html = "\n\n".join(form_fields)
    
    code = f"""<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>${{{empty {entity_lower} ? 'Thêm mới' : 'Cập nhật'}}} {entity_name}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
</head>
<body class="container mt-4">
<div class="row justify-content-center">
    <div class="col-md-6">
        <h3 class="text-center fw-bold mb-4">
            ${{empty {entity_lower} ? 'THÊM MỚI' : 'CẬP NHẬT'}} {entity_name.upper()}
        </h3>

        <c:if test="${{not empty error}}">
            <div class="alert alert-danger">${{error}}</div>
        </c:if>

        <form action="/{entity_list}/save" method="post">
            <input type="hidden" name="id" value="${{{entity_lower}.id}}"/>

{form_fields_html}

            <div class="d-flex justify-content-between">
                <a href="/{entity_list}" class="btn btn-secondary">Quay lại</a>
                <button type="submit" class="btn btn-primary">
                    ${{empty {entity_lower} ? 'Thêm mới' : 'Cập nhật'}}
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>"""
    return code


def main():
    print("=" * 60)
    print("JSP CODE GENERATOR".center(60))
    print("=" * 60)
    
    # Input
    entity_name = input("\nNhập tên entity (VD: Khóa học): ").strip()
    
    print("\nNhập các trường (field). Định dạng: tên_field|Nhãn|type")
    print("VD: courseCode|Mã khóa học|text")
    print("Nhấn Enter 2 lần để kết thúc\n")
    
    fields = []
    while True:
        field_input = input(f"Field {len(fields) + 1}: ").strip()
        if not field_input:
            break
        
        parts = field_input.split('|')
        if len(parts) >= 2:
            field = {
                'name': parts[0],
                'label': parts[1],
                'type': parts[2] if len(parts) > 2 else 'text'
            }
            fields.append(field)
    
    if not fields:
        print("❌ Phải có ít nhất 1 field!")
        return
    
    # Generate
    print("\n" + "=" * 60)
    print("ĐANG TẠO CODE...".center(60))
    print("=" * 60)
    
    list_code = generate_list_jsp(entity_name, fields)
    form_code = generate_form_jsp(entity_name, fields)
    
    # Save files
    entity_lower = entity_name.lower()
    list_file = f"{entity_lower}-list.jsp"
    form_file = f"{entity_lower}-form.jsp"
    
    with open(list_file, 'w', encoding='utf-8') as f:
        f.write(list_code)
    
    with open(form_file, 'w', encoding='utf-8') as f:
        f.write(form_code)
    
    print(f"\n✅ Đã tạo file: {list_file}")
    print(f"✅ Đã tạo file: {form_file}")
    print("\n📁 Copy 2 file này vào: src/main/webapp/WEB-INF/views/")
    print("\n" + "=" * 60)


if __name__ == "__main__":
    main()
