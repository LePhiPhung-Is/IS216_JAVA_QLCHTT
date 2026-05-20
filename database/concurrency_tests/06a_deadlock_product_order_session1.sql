-- =====================================================
-- 06a_deadlock_product_order_session1.sql
-- DEADLOCK - TÌNH HUỐNG 1 - SESSION 1
-- Session 1 khóa SANPHAM trước, sau đó khóa DONHANG
-- =====================================================

ROLLBACK;
-- BƯỚC 1:
-- Session 1 cập nhật sản phẩm SP01.
-- Câu lệnh UPDATE sẽ giữ khóa trên dòng SP01 cho đến khi COMMIT hoặc ROLLBACK.
UPDATE SANPHAM
SET GiaBan = GiaBan
WHERE MaSP = 'SP01';

-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy bước khóa DONHANG DH01.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ KHÓA DH01
-- =====================================================

-- BƯỚC 3:
-- Session 1 cố gắng cập nhật đơn hàng DH01.
-- Nếu Session 2 đang giữ khóa DH01, Session 1 sẽ bị chờ.
UPDATE DONHANG
SET GhiChu = N'Session 1 cập nhật đơn hàng'
WHERE MaDH = 'DH01';

-- Sau khi Oracle báo deadlock ở một trong hai session,
-- chạy ROLLBACK để giải phóng khóa.
ROLLBACK;