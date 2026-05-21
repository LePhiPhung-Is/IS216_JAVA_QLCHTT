-- =====================================================
-- 10_demo_sanpham_check_result.sql
-- Kiểm tra kết quả sau khi demo xử lý đồng thời trên sản phẩm
-- =====================================================

SELECT MaSP, TenSP, GiaBan, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP = 'SP01';