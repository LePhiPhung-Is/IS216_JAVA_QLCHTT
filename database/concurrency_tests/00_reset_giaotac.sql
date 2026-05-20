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


COMMIT;

SELECT MaDH, TongTien, GhiChu
FROM DONHANG
WHERE MaDH = 'DH01';

SELECT MaSP, TenSP, GiaBan, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP IN ('SP01', 'SP02', 'SP03');