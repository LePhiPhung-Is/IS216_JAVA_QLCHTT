-- =====================================================
-- 04b_non_repeatable_promotion_bad_session1.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 2 - SESSION 1
-- Trạng thái khuyến mãi thay đổi trong cùng giao tác
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT MaKM, TenKM, TrangThai
FROM KHUYENMAI
WHERE MaKM = 'KM01';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 cập nhật trạng thái khuyến mãi.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đọc lại trạng thái khuyến mãi lần thứ hai.
SELECT MaKM, TenKM, TrangThai
FROM KHUYENMAI
WHERE MaKM = 'KM01';
COMMIT;