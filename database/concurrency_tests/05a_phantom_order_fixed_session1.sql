-- =====================================================
-- 05a_phantom_order_fixed_session1.sql
-- PHANTOM READ - TÌNH HUỐNG 1 - ĐÃ SỬA - SESSION 1
-- Dùng SERIALIZABLE để đọc tập dữ liệu nhất quán
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
SELECT COUNT(*) AS SoDonHangTrongNgay
FROM DONHANG
WHERE TRUNC(NgayDat) = TRUNC(SYSDATE);

-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 05a_phantom_order_fixed_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đếm lại số đơn hàng trong ngày.
-- Với SERIALIZABLE, Session 1 vẫn nhìn thấy tập dữ liệu ban đầu.
SELECT COUNT(*) AS SoDonHangTrongNgay
FROM DONHANG
WHERE TRUNC(NgayDat) = TRUNC(SYSDATE);
COMMIT;