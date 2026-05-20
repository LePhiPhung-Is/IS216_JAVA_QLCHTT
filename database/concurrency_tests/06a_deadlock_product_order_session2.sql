-- =====================================================
-- 06a_deadlock_product_order_session2.sql
-- DEADLOCK - TÌNH HUỐNG 1 - SESSION 2
-- Session 2 khóa DONHANG trước, sau đó khóa SANPHAM
-- =====================================================

ROLLBACK;
-- BƯỚC 2:
-- Session 2 cập nhật đơn hàng DH01.
-- Câu lệnh UPDATE sẽ giữ khóa trên dòng DH01.
UPDATE DONHANG
SET GhiChu = N'Session 2 cập nhật đơn hàng'
WHERE MaDH = 'DH01';

-- DỪNG Ở ĐÂY.
-- Quay lại Session 1 chạy bước cập nhật DONHANG DH01.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 1 ĐANG CHỜ DH01
-- =====================================================

-- BƯỚC 4:
-- Session 2 cố gắng cập nhật sản phẩm SP01.
-- Nếu Session 1 đang giữ khóa SP01, hai session sẽ chờ nhau.
-- Oracle sẽ phát hiện deadlock và báo ORA-00060.
UPDATE SANPHAM
SET GiaBan = GiaBan
WHERE MaSP = 'SP01';

ROLLBACK;