-- =====================================================
-- 06b_deadlock_customer_order_session1.sql
-- DEADLOCK - TÌNH HUỐNG 2 - SESSION 1
-- Session 1 khóa KHACHHANG trước, sau đó khóa DONHANG
-- =====================================================

ROLLBACK;

-- BƯỚC 1:
-- Session 1 cập nhật khách hàng KH02.
UPDATE KHACHHANG
SET DiemTichLuy = DiemTichLuy
WHERE MaKH = 'KH02';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 khóa DONHANG DH01.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ KHÓA DH01
-- =====================================================

-- BƯỚC 3:
-- Session 1 cố gắng cập nhật đơn hàng DH01.
UPDATE DONHANG
SET GhiChu = N'Session 1 xử lý đơn hàng của khách'
WHERE MaDH = 'DH01';

ROLLBACK;