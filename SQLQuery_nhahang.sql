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
    NgayLap DATETIME,
    TongTien DECIMAL(18,2),
    TrangThai NVARCHAR(20),
    MaBan VARCHAR(50),
    MaND VARCHAR(50),
    MaKH VARCHAR(50),
    MaVoucher VARCHAR(50),
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

-- Bảng Voucher
CREATE TABLE Voucher (
    MaVoucher VARCHAR(50) PRIMARY KEY,
    TenVoucher NVARCHAR(100),
    LoaiGiamGia NVARCHAR(20),  -- 'TienMat' hoặc 'PhanTram'
    GiaTriGiamGia DECIMAL(12,2),
    DonGiaTuoiNhap DECIMAL(18,2),  -- Giá trị đơn hàng tối thiểu để dùng voucher
    NgayBatDau DATETIME,
    NgayKetThuc DATETIME,
    SoLanDung INT,
    SoLanConLai INT,
    TrangThai BIT DEFAULT 1  -- 1: hoạt động, 0: vô hiệu hóa
);

-- Bảng Lịch sử sử dụng Voucher
CREATE TABLE VoucherUsage (
    MaVoucherUsage VARCHAR(50) PRIMARY KEY,
    MaVoucher VARCHAR(50),
    MaKhachHang VARCHAR(50),
    NgayDung DATETIME,
    MaHoaDon VARCHAR(50),
    GiaTriGiam DECIMAL(12,2),
    FOREIGN KEY (MaVoucher) REFERENCES Voucher(MaVoucher),
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHD)
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
INSERT INTO Ban VALUES ('B006', 4, 0);
INSERT INTO Ban VALUES ('B007', 6, 0);
INSERT INTO Ban VALUES ('B008', 2, 0);
INSERT INTO Ban VALUES ('B009', 8, 0);
INSERT INTO Ban VALUES ('B010', 10, 0);
INSERT INTO Ban VALUES ('B011', 4, 0);
INSERT INTO Ban VALUES ('B012', 6, 0);
INSERT INTO Ban VALUES ('B013', 4, 0);
INSERT INTO Ban VALUES ('B014', 8, 0);
INSERT INTO Ban VALUES ('B015', 10, 0);
INSERT INTO Ban VALUES ('B016', 2, 0);

-- Dữ liệu mẫu Thực đơn
INSERT INTO ThucDon VALUES ('TD01', N'Phở bò', 40000, N'Món ăn', N'Bát');
INSERT INTO ThucDon VALUES ('TD02', N'Cơm gà', 50000, N'Món ăn', N'Đĩa');
INSERT INTO ThucDon VALUES ('TD03', N'Coca-Cola', 15000, N'Đồ uống', N'Lon');
INSERT INTO ThucDon VALUES ('TD04', N'Trà đá', 5000, N'Đồ uống', N'Cốc');
INSERT INTO ThucDon VALUES ('TD05', N'Bia Hà Nội', 20000, N'Đồ uống', N'Chai');
INSERT INTO ThucDon VALUES ('TD06', N'Cơm tấm', 45000, N'Món ăn', N'Đĩa');
INSERT INTO ThucDon VALUES ('TD07', N'Bún chả', 48000, N'Món ăn', N'Bát');
INSERT INTO ThucDon VALUES ('TD08', N'Bánh mì', 25000, N'Món ăn', N'Cái');
INSERT INTO ThucDon VALUES ('TD09', N'Nước cam', 18000, N'Đồ uống', N'Cốc');
INSERT INTO ThucDon VALUES ('TD10', N'Cà phê đen', 12000, N'Đồ uống', N'Cốc');
INSERT INTO ThucDon VALUES ('TD11', N'Gà rán', 55000, N'Món ăn', N'Phần');
INSERT INTO ThucDon VALUES ('TD12', N'Tôm nướng', 60000, N'Món ăn', N'Phần');
INSERT INTO ThucDon VALUES ('TD13', N'Mực xào', 52000, N'Món ăn', N'Phần');
INSERT INTO ThucDon VALUES ('TD14', N'Sinh tố', 22000, N'Đồ uống', N'Cốc');
INSERT INTO ThucDon VALUES ('TD15', N'Nước lọc', 3000, N'Đồ uống', N'Chai');
INSERT INTO ThucDon VALUES ('TD16', N'Rau muống xào', 28000, N'Món ăn', N'Đĩa');

-- Dữ liệu mẫu Khách hàng
INSERT INTO KhachHang VALUES 
('KH01', N'Nguyễn Văn A', N'Hà Nội'),
('KH02', N'Trần Thị B', N'Hồ Chí Minh');

-- Dữ liệu mẫu Voucher
INSERT INTO Voucher VALUES
('V001', N'Giảm 10%', N'PhanTram', 10, 20000, '2026-01-01', '2026-11-11', 100, 100, 1),
('V002', N'Giảm 20k', N'TienMat', 20000, 150000, '2024-01-01', '2024-12-31', 50, 50, 1),
('V003', N'Giảm 15%', N'PhanTram', 15, 200000, '2024-01-01', '2024-12-31', 30, 30, 1),
('V004', N'Giảm 30k', N'TienMat', 30000, 250000, '2024-01-01', '2024-12-31', 40, 40, 1),
('V005', N'Giảm 20%', N'PhanTram', 20, 300000, '2024-01-01', '2024-12-31', 25, 25, 1),
('V006', N'Giảm 50k', N'TienMat', 50000, 500000, '2024-01-01', '2024-12-31', 20, 20, 1),
('V007', N'Giảm 5%', N'PhanTram', 5, 50000, '2024-01-01', '2024-12-31', 200, 200, 1),
('V008', N'Giảm 25%', N'PhanTram', 25, 400000, '2024-01-01', '2024-12-31', 15, 15, 1),
('V009', N'Giảm 40k', N'TienMat', 40000, 350000, '2024-01-01', '2024-12-31', 30, 30, 1),
('V010', N'Giảm 12%', N'PhanTram', 12, 180000, '2024-01-01', '2024-12-31', 50, 50, 1),
('V011', N'Giảm 15k', N'TienMat', 15000, 120000, '2024-01-01', '2024-12-31', 100, 100, 1),
('V012', N'Giảm 8%', N'PhanTram', 8, 80000, '2024-01-01', '2024-12-31', 150, 150, 1),
('V013', N'Giảm 35k', N'TienMat', 35000, 280000, '2024-01-01', '2024-12-31', 35, 35, 1),
('V014', N'Giảm 18%', N'PhanTram', 18, 220000, '2024-01-01', '2024-12-31', 40, 40, 1),
('V015', N'Giảm 60k', N'TienMat', 60000, 600000, '2024-01-01', '2024-12-31', 10, 10, 1),
('V016', N'Giảm 22%', N'PhanTram', 22, 350000, '2024-01-01', '2024-12-31', 20, 20, 1);

-- Thêm Foreign Key cho MaVoucher trong HoaDon
ALTER TABLE HoaDon ADD FOREIGN KEY (MaVoucher) REFERENCES Voucher(MaVoucher);

