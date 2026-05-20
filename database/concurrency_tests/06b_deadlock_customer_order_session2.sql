-- =====================================================
-- 06b_deadlock_customer_order_session2.sql
-- DEADLOCK - TÌNH HUỐNG 2 - SESSION 2
-- Session 2 khóa DONHANG trước, sau đó khóa KHACHHANG
-- =====================================================

ROLLBACK;

-- BƯỚC 2:
-- Session 2 cập nhật đơn hàng DH01.
UPDATE DONHANG
SET GhiChu = N'Session 2 xử lý đơn hàng'
WHERE MaDH = 'DH01';

-- DỪNG Ở ĐÂY.
-- Quay lại Session 1 chạy bước cập nhật DONHANG.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 1 ĐANG CHỜ DH01
-- =====================================================

-- BƯỚC 4:
-- Session 2 cố gắng cập nhật khách hàng KH02.
-- Oracle sẽ phát hiện deadlock.
UPDATE KHACHHANG
SET DiemTichLuy = DiemTichLuy
WHERE MaKH = 'KH02';

ROLLBACK;