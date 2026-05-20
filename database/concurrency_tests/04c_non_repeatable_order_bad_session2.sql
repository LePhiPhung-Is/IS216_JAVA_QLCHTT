-- =====================================================
-- 04c_non_repeatable_order_bad_session2.sql
-- NON-REPEATABLE READ - TÌNH HUỐNG 3 - SESSION 2
-- =====================================================

-- BƯỚC 2:
-- Session 2 cập nhật trạng thái đơn hàng DH01.
UPDATE DONHANG
SET TrangThai = N'Đang giao'
WHERE MaDH = 'DH01';
COMMIT;
SELECT MaDH, MaKH, TrangThai, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';