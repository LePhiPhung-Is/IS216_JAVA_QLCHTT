-- =====================================================
-- 04b_non_repeatable_promotion_bad_session2.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 2 - SESSION 2
-- =====================================================

-- BƯỚC 2:
-- Session 2 tạm ngưng mã khuyến mãi KM01.
UPDATE KHUYENMAI
SET TrangThai = N'Tạm ngưng'
WHERE MaKM = 'KM01';
COMMIT;
SELECT MaKM, TenKM, TrangThai
FROM KHUYENMAI
WHERE MaKM = 'KM01';