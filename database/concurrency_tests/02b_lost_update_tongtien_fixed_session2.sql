-- =====================================================
-- 02b_lost_update_tongtien_fixed_session2.sql
-- LOST UPDATE TỔNG TIỀN ĐƠN HÀNG - SESSION 2 - ĐÃ SỬA
-- =====================================================

-- BƯỚC 3:
-- Session 2 cộng thêm 350000 vào tổng tiền.
-- Nếu Session 1 chưa commit, câu lệnh này sẽ bị chờ.
UPDATE DONHANG
SET TongTien = TongTien + 350000
WHERE MaDH = 'DH01';

COMMIT;

SELECT MaDH, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';