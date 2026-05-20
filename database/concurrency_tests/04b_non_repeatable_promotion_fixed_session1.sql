-- =====================================================
-- 04b_non_repeatable_promotion_fixed_session1.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 2 - ĐÃ SỬA - SESSION 1
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
-- BƯỚC 1:
-- Session 1 đọc trạng thái khuyến mãi lần thứ nhất.
SELECT MaKM, TenKM, TrangThai
FROM KHUYENMAI
WHERE MaKM = 'KM01';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy:
-- 04b_non_repeatable_promotion_bad_session2.sql


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đọc lại trạng thái khuyến mãi lần thứ hai.
-- Với SERIALIZABLE, Session 1 vẫn nhìn thấy trạng thái ban đầu.
SELECT MaKM, TenKM, TrangThai
FROM KHUYENMAI
WHERE MaKM = 'KM01';
COMMIT;