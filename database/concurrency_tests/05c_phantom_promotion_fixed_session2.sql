-- =====================================================
-- 05c_phantom_promotion_fixed_session2.sql
-- PHANTOM READ - TÌNH HUỐNG 3 - ĐÃ SỬA - SESSION 2
-- =====================================================

INSERT INTO KHUYENMAI (
    MaKM, TenKM, NgayBatDau, NgayKetThuc,
    PhanTramGiam, GiaTriToiThieu, GiamToiDa, TrangThai
)
VALUES (
    'KM_PH2', N'Khuyến mãi demo Phantom Fixed', SYSDATE - 1, SYSDATE + 10,
    15, 200000, 70000, N'Đang áp dụng'
);

COMMIT;

SELECT MaKM, TenKM, TrangThai, NgayBatDau, NgayKetThuc
FROM KHUYENMAI
WHERE MaKM = 'KM_PH2';