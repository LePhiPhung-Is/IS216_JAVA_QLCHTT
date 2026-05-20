-- =====================================================
-- 05b_phantom_lowstock_bad_session1.sql
-- PHANTOM READ - TÌNH HUỐNG 2 - SESSION 1
-- Nhân viên kho xem danh sách sản phẩm sắp hết hàng
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT MaSP, TenSP, SoLuongTon, TrangThai
FROM SANPHAM
WHERE SoLuongTon <= 5
ORDER BY MaSP;

-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 05b_phantom_lowstock_bad_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 xem lại danh sách sản phẩm sắp hết hàng.
SELECT MaSP, TenSP, SoLuongTon, TrangThai
FROM SANPHAM
WHERE SoLuongTon <= 5
ORDER BY MaSP;
COMMIT;