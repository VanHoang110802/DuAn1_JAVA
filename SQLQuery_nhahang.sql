CREATE DATABASE NhaHang;
USE NhaHang;

-- Bảng Bàn
CREATE TABLE Ban (
    MaBan VARCHAR(50) PRIMARY KEY,
    SoGhe INT NOT NULL,
    TinhTrang BIT NOT NULL DEFAULT 0
);

-- Bảng Người dùng (quản lý + nhân viên)
CREATE TABLE NguoiDung (
    MaND VARCHAR(50) PRIMARY KEY,
    TenND NVARCHAR(50),
    VaiTro NVARCHAR(20),   -- 'QuanLy' hoặc 'NhanVien'
    Username VARCHAR(50) UNIQUE,
    Password VARCHAR(50)
);

-- Bảng Khách hàng (chỉ mã, tên, địa chỉ)
CREATE TABLE KhachHang (
    MaKH VARCHAR(50) PRIMARY KEY,
    TenKH NVARCHAR(100) NOT NULL,
    DiaChi NVARCHAR(200)
);

-- Bảng Hóa đơn (liên kết bàn, nhân viên, khách hàng)
CREATE TABLE HoaDon (
    MaHD VARCHAR(50) PRIMARY KEY,
    NgayLap DATE,
    TongTien DECIMAL(18,2),
    TrangThai NVARCHAR(20),
    MaBan VARCHAR(50),
    MaND VARCHAR(50),
    MaKH VARCHAR(50),
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan),
    FOREIGN KEY (MaND) REFERENCES NguoiDung(MaND),
    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH)
);

-- Bảng Thực đơn
CREATE TABLE ThucDon (
    MaItem VARCHAR(50) PRIMARY KEY,
    TenItem NVARCHAR(100),
    Gia DECIMAL(12,2),
    Loai NVARCHAR(50),       -- Món ăn / Đồ uống
    DonViTinh NVARCHAR(50)   -- Đĩa, bát, chai, lon...
);

-- Bảng Chi tiết hóa đơn
CREATE TABLE ChiTietHoaDon (
    MaHD VARCHAR(50),
    MaItem VARCHAR(50),
    SoLuong INT,
    DonGia DECIMAL(12,2),
    PRIMARY KEY (MaHD, MaItem),
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
    FOREIGN KEY (MaItem) REFERENCES ThucDon(MaItem)
);

-- Dữ liệu mẫu Người dùng
INSERT INTO NguoiDung VALUES 
('ND01', N'Nguyễn Văn F', N'NhanVien', 'nv1', '123'),
('ND02', N'Admin', N'QuanLy', 'ql1', '123'),
('ND03', N'Lê Văn H', N'NhanVien', 'nv2', '123');

-- Dữ liệu mẫu Bàn
INSERT INTO Ban VALUES ('B001', 4, 0);
INSERT INTO Ban VALUES ('B002', 6, 0);
INSERT INTO Ban VALUES ('B003', 2, 0);
INSERT INTO Ban VALUES ('B004', 8, 0);
INSERT INTO Ban VALUES ('B005', 10, 0);

-- Dữ liệu mẫu Thực đơn
INSERT INTO ThucDon VALUES ('TD01', N'Phở bò', 40000, N'Món ăn', N'Bát');
INSERT INTO ThucDon VALUES ('TD02', N'Cơm gà', 50000, N'Món ăn', N'Đĩa');
INSERT INTO ThucDon VALUES ('TD03', N'Coca-Cola', 15000, N'Đồ uống', N'Lon');
INSERT INTO ThucDon VALUES ('TD04', N'Trà đá', 5000, N'Đồ uống', N'Cốc');
INSERT INTO ThucDon VALUES ('TD05', N'Bia Hà Nội', 20000, N'Đồ uống', N'Chai');

-- Dữ liệu mẫu Khách hàng
INSERT INTO KhachHang VALUES 
('KH01', N'Nguyễn Văn A', N'Hà Nội'),
('KH02', N'Trần Thị B', N'Hồ Chí Minh');
