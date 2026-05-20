-- =====================================================
-- 03_dirty_read_session1.sql
-- DIRTY READ - SESSION 1
-- Oracle không cho phép Dirty Read
-- =====================================================

-- BƯỚC 1:
-- Xem giá ban đầu của sản phẩm SP03.
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP03';
-- BƯỚC 2:
-- Session 1 cập nhật giá SP03 nhưng KHÔNG COMMIT.
UPDATE SANPHAM
SET GiaBan = 999999
WHERE MaSP = 'SP03';
-- BƯỚC 3:
-- Trong chính Session 1, ta thấy giá đã thay đổi.
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP03';

-- KHÔNG COMMIT.
-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy file 03_dirty_read_session2.sql.


-- =====================================================
-- SAU KHI SESSION 2 ĐÃ ĐỌC XONG, QUAY LẠI ĐÂY
-- =====================================================

ROLLBACK;

-- Kiểm tra lại sau rollback.
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP03';