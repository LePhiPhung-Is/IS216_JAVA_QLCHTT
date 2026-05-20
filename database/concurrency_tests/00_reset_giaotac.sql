-- =====================================================
-- 00_reset_giaotac.sql
-- Reset dữ liệu trước khi mô phỏng giao tác đồng thời
-- =====================================================

SET SERVEROUTPUT ON;

UPDATE SANPHAM
SET SoLuongTon = 20,
    GiaBan = 250000,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP01';

UPDATE SANPHAM
SET GiaBan = 350000,
    SoLuongTon = 15,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP02';

UPDATE SANPHAM
SET GiaBan = 420000,
    SoLuongTon = 12,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP03';

-- Reset đơn hàng dùng để mô phỏng Lost Update tổng tiền
UPDATE DONHANG
SET TongTien = 0,
    GhiChu = N'Demo Lost Update TongTien'
WHERE MaDH = 'DH01';



-- Reset dữ liệu dùng cho Non-repeatable Read
UPDATE SANPHAM
SET GiaBan = 350000,
    SoLuongTon = 15,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP02';

UPDATE KHUYENMAI
SET TrangThai = N'Đang áp dụng'
WHERE MaKM = 'KM01';

UPDATE DONHANG
SET TrangThai = N'Chờ xác nhận',
    GhiChu = N'Demo Non-repeatable Read'
WHERE MaDH = 'DH01';




-- =====================================================
-- Reset dữ liệu dùng cho Phantom Read
-- =====================================================

-- Xóa đơn hàng demo Phantom nếu đã tồn tại
DELETE FROM CHITIET_DONHANG
WHERE MaDH IN ('DH_PH1', 'DH_PH2');

DELETE FROM DONHANG
WHERE MaDH IN ('DH_PH1', 'DH_PH2');

-- Xóa sản phẩm demo Phantom nếu đã tồn tại
DELETE FROM SANPHAM
WHERE MaSP IN ('SP_PH1', 'SP_PH2');

-- Xóa khuyến mãi demo Phantom nếu đã tồn tại
DELETE FROM KHUYENMAI
WHERE MaKM IN ('KM_PH1', 'KM_PH2');

-- Reset dữ liệu đơn hàng DH01
UPDATE DONHANG
SET NgayDat = SYSDATE,
    TrangThai = N'Chờ xác nhận',
    GhiChu = N'Demo Phantom Read'
WHERE MaDH = 'DH01';

-- Reset sản phẩm SP01, SP02, SP03
UPDATE SANPHAM
SET SoLuongTon = 20,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP01';

UPDATE SANPHAM
SET SoLuongTon = 15,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP02';

UPDATE SANPHAM
SET SoLuongTon = 12,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP03';

-- Reset khuyến mãi KM01
UPDATE KHUYENMAI
SET TrangThai = N'Đang áp dụng',
    NgayBatDau = SYSDATE - 1,
    NgayKetThuc = SYSDATE + 30
WHERE MaKM = 'KM01';





-- =====================================================
-- Reset dữ liệu dùng cho Deadlock
-- =====================================================

UPDATE SANPHAM
SET GiaBan = 250000,
    SoLuongTon = 20,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP01';

UPDATE SANPHAM
SET GiaBan = 350000,
    SoLuongTon = 15,
    TrangThai = N'Đang bán'
WHERE MaSP = 'SP02';

UPDATE DONHANG
SET GhiChu = N'Demo Deadlock',
    TrangThai = N'Chờ xác nhận'
WHERE MaDH = 'DH01';

UPDATE KHACHHANG
SET DiemTichLuy = 100
WHERE MaKH = 'KH02';


COMMIT;

SELECT MaSP, TenSP, GiaBan, SoLuongTon
FROM SANPHAM
WHERE MaSP IN ('SP01', 'SP02');

SELECT MaDH, MaKH, TrangThai, GhiChu
FROM DONHANG
WHERE MaDH = 'DH01';

SELECT MaKH, TenKH, DiemTichLuy
FROM KHACHHANG
WHERE MaKH = 'KH02';




SELECT COUNT(*) AS SoDonHangTrongNgay
FROM DONHANG
WHERE TRUNC(NgayDat) = TRUNC(SYSDATE);

SELECT MaSP, TenSP, SoLuongTon, TrangThai
FROM SANPHAM
WHERE SoLuongTon <= 5;

SELECT MaKM, TenKM, TrangThai, NgayBatDau, NgayKetThuc
FROM KHUYENMAI
WHERE TrangThai = N'Đang áp dụng'
  AND SYSDATE BETWEEN NgayBatDau AND NgayKetThuc;






SELECT MaSP, TenSP, GiaBan, TrangThai
FROM SANPHAM
WHERE MaSP = 'SP02';

SELECT MaKM, TenKM, TrangThai
FROM KHUYENMAI
WHERE MaKM = 'KM01';

SELECT MaDH, TrangThai, GhiChu
FROM DONHANG
WHERE MaDH = 'DH01';




SELECT MaDH, TongTien, GhiChu
FROM DONHANG
WHERE MaDH = 'DH01';

SELECT MaSP, TenSP, GiaBan, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP IN ('SP01', 'SP02', 'SP03');