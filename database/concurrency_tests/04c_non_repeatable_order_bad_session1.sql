-- =====================================================
-- 04c_non_repeatable_order_bad_session1.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 3 - SESSION 1
-- Trạng thái đơn hàng thay đổi trong cùng giao tác
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT MaDH, MaKH, TrangThai, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 cập nhật trạng thái đơn hàng.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 đọc lại trạng thái đơn hàng DH01 lần thứ hai.
SELECT MaDH, MaKH, TrangThai, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';
COMMIT;