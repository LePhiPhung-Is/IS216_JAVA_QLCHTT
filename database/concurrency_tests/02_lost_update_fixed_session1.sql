-- =====================================================
-- 02_lost_update_fixed_session1.sql
-- LOST UPDATE - SESSION 1 - PHIÊN BẢN ĐÃ SỬA
-- Dùng khóa dòng bằng SELECT ... FOR UPDATE
-- =====================================================

-- BƯỚC 1:
-- Session 1 khóa dòng sản phẩm SP01.
SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP = 'SP01'
FOR UPDATE;

-- BƯỚC 2:
-- Session 1 bán 3 sản phẩm.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon - 3
WHERE MaSP = 'SP01'
  AND SoLuongTon >= 3;

-- CHƯA COMMIT.
-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy file 02_lost_update_fixed_session2.sql.
-- Session 2 sẽ bị chờ vì Session 1 đang giữ khóa trên SP01.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI ĐÃ THẤY SESSION 2 BỊ CHỜ
-- =====================================================

COMMIT;

SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP = 'SP01';