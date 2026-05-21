-- =====================================================
-- 11_demo_dirty_read_java_session1.sql
-- Demo Dirty Read trên ứng dụng Java
-- Oracle không cho Java đọc dữ liệu chưa COMMIT
-- =====================================================

ROLLBACK;

-- BƯỚC 1: Xem giá ban đầu
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP03';

-- BƯỚC 2: Session SQL Developer cập nhật giá nhưng KHÔNG COMMIT
UPDATE SANPHAM
SET GiaBan = 999999
WHERE MaSP = 'SP03';

SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP03';

-- KHÔNG COMMIT.
-- Mở app Java, vào Quản lý sản phẩm, tìm SP03.
-- Java sẽ KHÔNG thấy GiaBan = 999999.
-- Java chỉ thấy giá cũ đã COMMIT.


-- SAU KHI CHỤP HÌNH JAVA XONG, CHẠY:
ROLLBACK;

SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP03';