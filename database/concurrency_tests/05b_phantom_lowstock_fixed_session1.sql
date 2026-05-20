-- =====================================================
-- 05b_phantom_lowstock_fixed_session1.sql
-- PHANTOM READ - TÌNH HUỐNG 2 - ĐÃ SỬA - SESSION 1
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
SELECT MaSP, TenSP, SoLuongTon, TrangThai
FROM SANPHAM
WHERE SoLuongTon <= 5
ORDER BY MaSP;

-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 05b_phantom_lowstock_fixed_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 xem lại danh sách sản phẩm sắp hết hàng.
-- Với SERIALIZABLE, dòng SP_PH2 chưa xuất hiện trong giao tác này.
SELECT MaSP, TenSP, SoLuongTon, TrangThai
FROM SANPHAM
WHERE SoLuongTon <= 5
ORDER BY MaSP;
COMMIT;