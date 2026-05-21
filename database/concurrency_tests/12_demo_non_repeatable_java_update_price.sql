-- =====================================================
-- 12_demo_non_repeatable_java_update_price.sql
-- Demo Non-repeatable Read trên ứng dụng Java
-- SQL Developer cập nhật giá sản phẩm trong khi Java đang xem
-- =====================================================

-- BƯỚC 1:
-- Trước khi chạy file này, mở app Java và xem SP02.
-- Java sẽ thấy GiaBan = 350000 sau khi reset.

-- BƯỚC 2:
-- SQL Developer cập nhật giá SP02 và COMMIT.
UPDATE SANPHAM
SET GiaBan = 400000
WHERE MaSP = 'SP02';

COMMIT;

SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP02';

-- BƯỚC 4:
-- Quay lại app Java, bấm tải lại danh sách / tìm kiếm lại SP02.
-- Java sẽ thấy GiaBan = 400000.