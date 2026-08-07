<%-- Thống kê cho quản lý: tổng doanh thu, doanh thu theo nhân viên, doanh thu theo ngày, top món --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Thống kê</title>
    <link rel="stylesheet" href="assets/app.css" />
    <style>
        body { font-family: Arial, sans-serif; background:#f5f5f5; padding:20px; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }

        h2 { color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }
        h3 { color: #555; margin-top: 30px; }

        .form-container { display: flex; gap: 15px; align-items: center; margin: 20px 0; flex-wrap: wrap; }
        .form-container input { padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; }
        .form-container button { padding: 8px 20px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; }
        .form-container button:hover { background: #0056b3; }
        .form-container a { padding: 8px 20px; background: #6c757d; color: white; text-decoration: none; border-radius: 4px; }
        .form-container a:hover { background: #545b62; }

        .total-revenue { background: #e7f3ff; border-left: 4px solid #007bff; padding: 15px; margin: 20px 0; border-radius: 4px; font-size: 18px; font-weight: bold; }
        .total-revenue .value { color: #007bff; font-size: 24px; }

        table { width: 100%; border-collapse: collapse; margin: 15px 0; }
        table thead { background: #007bff; color: white; }
        table th { padding: 12px; text-align: left; font-weight: bold; }
        table td { padding: 10px 12px; border-bottom: 1px solid #ddd; }
        table tbody tr:nth-child(even) { background: #f9f9f9; }
        table tbody tr:hover { background: #f0f0f0; }

        table th:nth-child(1) { width: 15%; }
        table th:nth-child(2) { width: 35%; }
        table th:nth-child(3) { width: 25%; }
        table th:nth-child(4) { width: 25%; }

        /* Cân chỉnh cho bảng top items (4 cột) */
        table:last-of-type th:nth-child(1) { width: 18%; }
        table:last-of-type th:nth-child(2) { width: 38%; }
        table:last-of-type th:nth-child(3) { width: 22%; text-align: center; }
        table:last-of-type th:nth-child(4) { width: 22%; text-align: right; }

        table:last-of-type td:nth-child(3) { text-align: center; }
        table:last-of-type td:nth-child(4) { text-align: right; }

        .error-msg { color: #721c24; background: #f8d7da; padding: 12px; margin: 15px 0; border: 1px solid #f5c6cb; border-radius: 4px; }
    </style>
</head>
<body>
<div class="container">
    <h2>📊 Thống kê Quản Lý</h2>

    <c:if test="${not empty errorMessage}">
        <div class="error-msg"><strong>Lỗi:</strong> ${errorMessage}</div>
    </c:if>

    <form method="get" action="hoadon" class="form-container">
        <input type="hidden" name="action" value="thongke" />
        <label>Từ ngày:</label>
        <input type="date" name="from" value="${from}" />
        <label>Đến ngày:</label>
        <input type="date" name="to" value="${to}" />
        <button type="submit">🔍 Lọc</button>
        <a href="quanly.jsp">← Quay lại</a>
    </form>

    <div class="total-revenue">
        💰 Tổng doanh thu: <span class="value"><fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true" /> VNĐ</span>
    </div>

    <h3>👥 Doanh thu theo nhân viên</h3>
    <table>
        <thead>
            <tr>
                <th>Mã NV</th>
                <th>Tên nhân viên</th>
                <th>Doanh thu</th>
                <th>% Tổng</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="r" items="${byEmployee}">
                <tr>
                    <td>${r.maND}</td>
                    <td>${r.tenND}</td>
                    <td><fmt:formatNumber value="${r.tongDoanhThu}" type="number" groupingUsed="true" /> VNĐ</td>
                    <td>
                        <c:set var="percent" value="${totalRevenue > 0 ? (r.tongDoanhThu / totalRevenue * 100) : 0}" />
                        <fmt:formatNumber value="${percent}" maxFractionDigits="1" />%
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty byEmployee}">
                <tr><td colspan="4" style="text-align:center; color:#999;">Không có dữ liệu</td></tr>
            </c:if>
        </tbody>
    </table>

    <h3>📅 Doanh thu theo ngày</h3>
    <table>
        <thead>
            <tr>
                <th>Ngày</th>
                <th>Doanh thu</th>
                <th>% Tổng</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="d" items="${byDate}">
                <tr>
                    <td>${d.ngay}</td>
                    <td><fmt:formatNumber value="${d.tongDoanhThu}" type="number" groupingUsed="true" /> VNĐ</td>
                    <td>
                        <c:set var="percentDay" value="${totalRevenue > 0 ? (d.tongDoanhThu / totalRevenue * 100) : 0}" />
                        <fmt:formatNumber value="${percentDay}" maxFractionDigits="1" />%
                    </td>
                    <td></td>
                </tr>
            </c:forEach>
            <c:if test="${empty byDate}">
                <tr><td colspan="4" style="text-align:center; color:#999;">Không có dữ liệu</td></tr>
            </c:if>
        </tbody>
    </table>

    <h3>🏆 Top 10 món bán chạy</h3>
    <table>
        <thead>
            <tr>
                <th>Mã món</th>
                <th>Tên món</th>
                <th>Số lượng bán</th>
                <th>Doanh thu</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="m" items="${topItems}">
                <tr>
                    <td>${m.maItem}</td>
                    <td>${m.tenItem}</td>
                    <td style="text-align:center;">${m.soLuongBan}</td>
                    <td><fmt:formatNumber value="${m.doanhThu}" type="number" groupingUsed="true" /> VNĐ</td>
                </tr>
            </c:forEach>
            <c:if test="${empty topItems}">
                <tr><td colspan="4" style="text-align:center; color:#999;">Không có dữ liệu</td></tr>
            </c:if>
        </tbody>
    </table>
</div>
</body>
</html>

