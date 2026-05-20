-- =====================================================
-- 02_lost_update_fixed_session2.sql
-- LOST UPDATE - SESSION 2 - PHIÊN BẢN ĐÃ SỬA
-- =====================================================

-- BƯỚC 3:
-- Session 2 bán 5 sản phẩm.
-- Nếu Session 1 chưa commit, câu lệnh này sẽ bị chờ.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon - 5
WHERE MaSP = 'SP01'
  AND SoLuongTon >= 5;

COMMIT;

SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP = 'SP01';