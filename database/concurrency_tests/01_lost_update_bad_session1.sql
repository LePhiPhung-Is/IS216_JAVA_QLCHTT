-- 01_lost_update_bad_session1.sql
-- LOST UPDATE - SESSION 1 - PHIÊN BẢN GÂY LỖI

-- BƯỚC 1:
-- Session 1 đọc số lượng tồn kho của sản phẩm SP01.
-- Giả sử SP01 đang có SoLuongTon = 20.
SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP = 'SP01';

-- Session 1 giả sử bán 3 sản phẩm.
-- Vì đọc được tồn kho là 20 nên Session 1 tự tính:
-- 20 - 3 = 17.
--
-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 01_lost_update_bad_session2.sql ở một session khác.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 cập nhật lại tồn kho dựa trên giá trị cũ đã đọc.
UPDATE SANPHAM
SET SoLuongTon = 17
WHERE MaSP = 'SP01';

COMMIT;

-- BƯỚC 4:
-- Kiểm tra tồn kho cuối cùng.
SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP = 'SP01';