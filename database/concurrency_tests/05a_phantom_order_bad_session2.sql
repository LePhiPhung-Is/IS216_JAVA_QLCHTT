-- =====================================================
-- 05a_phantom_order_bad_session2.sql
-- PHANTOM READ - TÌNH HUỐNG 1 - SESSION 2
-- Thêm đơn hàng mới trong ngày
-- =====================================================

-- BƯỚC 2:
-- Session 2 thêm một đơn hàng mới trong ngày.
INSERT INTO DONHANG (
    MaDH, MaKH, MaNV, MaDC, MaGiamGia,
    LoaiDon, NgayDat, TrangThai, TongTien, PhiShip,
    HinhThucThanhToan, DiemThuong, DiemSuDung, GhiChu
)
VALUES (
    'DH_PH1', 'KH02', 'NV09', 'DC05', NULL,
    'ONLINE', SYSDATE, N'Chờ xác nhận', 250000, 0,
    N'Tiền mặt', 25, 0, N'Demo Phantom Read - Order'
);
COMMIT;
SELECT MaDH, MaKH, NgayDat, TrangThai, TongTien
FROM DONHANG
WHERE MaDH = 'DH_PH1';