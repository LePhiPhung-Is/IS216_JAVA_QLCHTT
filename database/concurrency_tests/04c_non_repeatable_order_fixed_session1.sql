-- =====================================================
-- 04c_non_repeatable_order_fixed_session1.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 3 - ĐÃ SỬA - SESSION 1
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
SELECT MaDH, MaKH, TrangThai, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy:
-- 04c_non_repeatable_order_bad_session2.sql


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đọc lại trạng thái đơn hàng DH01 lần thứ hai.
SELECT MaDH, MaKH, TrangThai, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';
COMMIT;