-- =====================================================
-- 13_demo_phantom_java_insert_product.sql
-- Demo Phantom Read trên ứng dụng Java
-- SQL Developer thêm sản phẩm mới trong khi Java đang xem danh sách sản phẩm
-- =====================================================

-- Xóa sản phẩm demo nếu đã tồn tại
DELETE FROM SANPHAM
WHERE MaSP = 'SP_PH_JAVA';

COMMIT;

-- BƯỚC 1:
-- Mở app Java, vào Quản lý sản phẩm.
-- Kiểm tra chưa có sản phẩm SP_PH_JAVA.

-- BƯỚC 2:
-- SQL Developer thêm sản phẩm mới có tồn kho thấp.
INSERT INTO SANPHAM (
    MaSP, MaDM, MaKho, TenSP, MauSac, KichCo,
    GiaBan, SoLuongTon, TrangThai, HinhAnh
)
SELECT
    'SP_PH_JAVA', MaDM, MaKho, N'Sản phẩm Phantom Java', N'Trắng', 'M',
    199000, 3, N'Đang bán', NULL
FROM SANPHAM
WHERE MaSP = 'SP01';

COMMIT;

SELECT MaSP, TenSP, GiaBan, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP = 'SP_PH_JAVA';

-- BƯỚC 4:
-- Quay lại Java, reload danh sách sản phẩm.
-- Java sẽ thấy xuất hiện thêm SP_PH_JAVA.