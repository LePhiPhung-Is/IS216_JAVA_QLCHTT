-- =====================================================
-- 04a_non_repeatable_price_fixed_session1.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 1 - ĐÃ SỬA - SESSION 1
-- Dùng SERIALIZABLE để đọc dữ liệu nhất quán
-- =====================================================
ROLLBACK;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP02';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy lại file:
-- 04a_non_repeatable_price_bad_session2.sql


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đọc lại giá sản phẩm SP02 lần thứ hai.
-- Do đang ở SERIALIZABLE, Session 1 vẫn thấy dữ liệu như lần đọc đầu.
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP02';
COMMIT;