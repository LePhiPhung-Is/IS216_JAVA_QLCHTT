-- =====================================================
-- 06c_deadlock_two_products_session2.sql
-- DEADLOCK - TÌNH HUỐNG 3 - SESSION 2
-- Session 2 khóa SP02 trước, sau đó khóa SP01
-- =====================================================

ROLLBACK;

-- BƯỚC 2:
-- Session 2 khóa sản phẩm SP02.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP02';

-- DỪNG Ở ĐÂY.
-- Quay lại Session 1 chạy bước cập nhật SP02.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 1 ĐANG CHỜ SP02
-- =====================================================

-- BƯỚC 4:
-- Session 2 cố gắng cập nhật SP01.
-- Oracle sẽ báo deadlock.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP01';

ROLLBACK;