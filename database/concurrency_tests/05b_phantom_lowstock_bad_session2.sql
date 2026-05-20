-- =====================================================
-- 05b_phantom_lowstock_bad_session2.sql
-- PHANTOM READ - TÌNH HUỐNG 2 - SESSION 2
-- Thêm sản phẩm mới có tồn kho thấp
-- =====================================================

-- BƯỚC 2:
-- Session 2 thêm một sản phẩm mới có SoLuongTon = 3.
-- Copy MaDM, MaKho từ SP01 để tránh lỗi khóa ngoại.
INSERT INTO SANPHAM (
    MaSP, MaDM, MaKho, TenSP, MauSac, KichCo,
    GiaBan, SoLuongTon, TrangThai, HinhAnh
)
SELECT
    'SP_PH1', MaDM, MaKho, N'Áo demo Phantom', N'Trắng', 'M',
    199000, 3, N'Đang bán', NULL
FROM SANPHAM
WHERE MaSP = 'SP01';
COMMIT;
SELECT MaSP, TenSP, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP = 'SP_PH1';