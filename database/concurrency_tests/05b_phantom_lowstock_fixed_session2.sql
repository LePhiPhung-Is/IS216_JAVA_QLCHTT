-- =====================================================
-- 05b_phantom_lowstock_fixed_session2.sql
-- PHANTOM READ - TÌNH HUỐNG 2 - ĐÃ SỬA - SESSION 2
-- =====================================================

INSERT INTO SANPHAM (
    MaSP, MaDM, MaKho, TenSP, MauSac, KichCo,
    GiaBan, SoLuongTon, TrangThai, HinhAnh
)
SELECT
    'SP_PH2', MaDM, MaKho, N'Quần demo Phantom', N'Đen', 'L',
    299000, 2, N'Đang bán', NULL
FROM SANPHAM
WHERE MaSP = 'SP01';

COMMIT;

SELECT MaSP, TenSP, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP = 'SP_PH2';