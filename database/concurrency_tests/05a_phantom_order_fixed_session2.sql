-- =====================================================
-- 05a_phantom_order_fixed_session2.sql
-- PHANTOM READ - TÌNH HUỐNG 1 - ĐÃ SỬA - SESSION 2
-- =====================================================

INSERT INTO DONHANG (
    MaDH, MaKH, MaNV, MaDC, MaGiamGia,
    LoaiDon, NgayDat, TrangThai, TongTien, PhiShip,
    HinhThucThanhToan, DiemThuong, DiemSuDung, GhiChu
)
VALUES (
    'DH_PH2', 'KH02', 'NV09', 'DC05', NULL,
    'ONLINE', SYSDATE, N'Chờ xác nhận', 300000, 0,
    N'Tiền mặt', 30, 0, N'Demo Phantom Read Fixed - Order'
);
COMMIT;
SELECT MaDH, MaKH, NgayDat, TrangThai, TongTien
FROM DONHANG
WHERE MaDH = 'DH_PH2';