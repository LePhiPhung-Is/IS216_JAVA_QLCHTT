-- =====================================================
-- 02b_lost_update_tongtien_fixed_session1.sql
-- LOST UPDATE TỔNG TIỀN ĐƠN HÀNG - SESSION 1 - ĐÃ SỬA
-- Dùng SELECT ... FOR UPDATE để khóa dòng đơn hàng
-- =====================================================

-- BƯỚC 1:
-- Session 1 khóa dòng đơn hàng DH01.
SELECT MaDH, TongTien
FROM DONHANG
WHERE MaDH = 'DH01'
FOR UPDATE;

-- BƯỚC 2:
-- Session 1 cộng thêm 250000 vào tổng tiền.
UPDATE DONHANG
SET TongTien = TongTien + 250000
WHERE MaDH = 'DH01';

-- CHƯA COMMIT.
-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy file 02b_lost_update_tongtien_fixed_session2.sql.
-- Session 2 sẽ bị chờ vì Session 1 đang khóa DH01.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI ĐÃ THẤY SESSION 2 BỊ CHỜ
-- =====================================================

COMMIT;

SELECT MaDH, TongTien
FROM DONHANG
WHERE MaDH = 'DH01';