-- =====================================================
-- 04a_non_repeatable_price_bad_session1.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 1 - SESSION 1
-- Giá sản phẩm thay đổi trong cùng một giao tác
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
-- BƯỚC 1:
-- Session 1 đọc giá sản phẩm SP02 lần thứ nhất.
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP02';

-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 04a_non_repeatable_price_bad_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đọc lại giá sản phẩm SP02 lần thứ hai.
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP02';
COMMIT;
