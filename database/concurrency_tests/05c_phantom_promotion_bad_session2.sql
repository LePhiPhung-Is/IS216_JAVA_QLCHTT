-- =====================================================
-- 05c_phantom_promotion_bad_session2.sql
-- PHANTOM READ - TÌNH HUỐNG 3 - SESSION 2
-- Thêm khuyến mãi mới đang áp dụng
-- =====================================================

-- BƯỚC 2:
-- Session 2 thêm một khuyến mãi mới đang áp dụng.
INSERT INTO KHUYENMAI (
    MaKM, TenKM, NgayBatDau, NgayKetThuc,
    PhanTramGiam, GiaTriToiThieu, GiamToiDa, TrangThai
)
VALUES (
    'KM_PH1', N'Khuyến mãi demo Phantom', SYSDATE - 1, SYSDATE + 10,
    10, 100000, 50000, N'Đang áp dụng'
);
COMMIT;
SELECT MaKM, TenKM, TrangThai, NgayBatDau, NgayKetThuc
FROM KHUYENMAI
WHERE MaKM = 'KM_PH1';