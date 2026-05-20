-- =====================================================
-- 04a_non_repeatable_price_bad_session2.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 1 - SESSION 2
-- =====================================================

-- BƯỚC 2:
-- Session 2 cập nhật giá sản phẩm SP02 và COMMIT.
UPDATE SANPHAM
SET GiaBan = 400000
WHERE MaSP = 'SP02';

COMMIT;

SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP02';