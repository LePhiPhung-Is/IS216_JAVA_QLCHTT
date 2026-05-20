-- =====================================================
-- 01_lost_update_bad_session2.sql
-- LOST UPDATE - SESSION 2 - PHIÊN BẢN GÂY LỖI
-- =====================================================

-- BƯỚC 2:
-- Session 2 cũng đọc số lượng tồn kho của sản phẩm SP01.
SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP = 'SP01';

-- Session 2 giả sử bán 5 sản phẩm.
-- Vì cũng đọc được tồn kho là 20 nên Session 2 tự tính:
-- 20 - 5 = 15.

UPDATE SANPHAM
SET SoLuongTon = 15
WHERE MaSP = 'SP01';

COMMIT;

-- Kiểm tra kết quả sau khi Session 2 commit.
SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP = 'SP01';