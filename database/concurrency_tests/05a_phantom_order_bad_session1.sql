-- =====================================================
-- 05a_phantom_order_bad_session1.sql
-- PHANTOM READ - TÌNH HUỐNG 1 - SESSION 1
-- Quản lý đếm số đơn hàng trong ngày
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT COUNT(*) AS SoDonHangTrongNgay
FROM DONHANG
WHERE TRUNC(NgayDat) = TRUNC(SYSDATE);

-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 05a_phantom_order_bad_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đếm lại số đơn hàng trong ngày lần thứ hai.
SELECT COUNT(*) AS SoDonHangTrongNgay
FROM DONHANG
WHERE TRUNC(NgayDat) = TRUNC(SYSDATE);
COMMIT;