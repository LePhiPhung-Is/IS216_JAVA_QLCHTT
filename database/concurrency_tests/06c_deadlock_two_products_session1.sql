-- =====================================================
-- 06c_deadlock_two_products_session1.sql
-- DEADLOCK - TÌNH HUỐNG 3 - SESSION 1
-- Session 1 khóa SP01 trước, sau đó khóa SP02
-- =====================================================

ROLLBACK;

-- BƯỚC 1:
-- Session 1 khóa sản phẩm SP01.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP01';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 khóa SP02.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ KHÓA SP02
-- =====================================================

-- BƯỚC 3:
-- Session 1 cố gắng cập nhật SP02.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP02';

ROLLBACK;