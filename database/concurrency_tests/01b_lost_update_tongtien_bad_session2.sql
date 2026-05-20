-- =====================================================
-- 01b_lost_update_tongtien_bad_session2.sql
-- LOST UPDATE - TÌNH HUỐNG 2 - SESSION 2
-- =====================================================

-- BƯỚC 2:
-- Session 2 cũng đọc tổng tiền ban đầu của đơn hàng DH01.
SELECT MaDH, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';

-- Giả sử TongTien ban đầu = 0.
-- Session 2 thêm một sản phẩm khác có thành tiền 350000.
-- Session 2 tự tính:
-- 0 + 350000 = 350000.

UPDATE DONHANG
SET TongTien = 350000
WHERE MaDH = 'DH01';

COMMIT;

SELECT MaDH, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';