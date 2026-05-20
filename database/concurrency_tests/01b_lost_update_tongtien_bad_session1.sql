-- =====================================================
-- 01b_lost_update_tongtien_bad_session1.sql
-- LOST UPDATE - TÌNH HUỐNG 2 - SESSION 1
-- Mất cập nhật tổng tiền đơn hàng
-- =====================================================

-- BƯỚC 1:
-- Session 1 đọc tổng tiền ban đầu của đơn hàng DH01.
SELECT MaDH, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';

-- Giả sử TongTien ban đầu = 0.
-- Session 1 thêm một sản phẩm có thành tiền 250000.
-- Session 1 tự tính:
-- 0 + 250000 = 250000.
--
-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 01b_lost_update_tongtien_bad_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 cập nhật tổng tiền dựa trên giá trị cũ đã đọc.
UPDATE DONHANG
SET TongTien = 250000
WHERE MaDH = 'DH01';

COMMIT;

-- BƯỚC 4:
-- Kiểm tra tổng tiền cuối cùng.
SELECT MaDH, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';