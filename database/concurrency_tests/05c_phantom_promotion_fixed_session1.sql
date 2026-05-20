-- =====================================================
-- 05c_phantom_promotion_fixed_session1.sql
-- PHANTOM READ - TÌNH HUỐNG 3 - ĐÃ SỬA - SESSION 1
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
SELECT MaKM, TenKM, TrangThai, NgayBatDau, NgayKetThuc
FROM KHUYENMAI
WHERE TrangThai = N'Đang áp dụng'
  AND SYSDATE BETWEEN NgayBatDau AND NgayKetThuc
ORDER BY MaKM;

-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 05c_phantom_promotion_fixed_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 xem lại danh sách khuyến mãi đang áp dụng.
-- Với SERIALIZABLE, khuyến mãi KM_PH2 chưa xuất hiện trong giao tác này.
SELECT MaKM, TenKM, TrangThai, NgayBatDau, NgayKetThuc
FROM KHUYENMAI
WHERE TrangThai = N'Đang áp dụng'
  AND SYSDATE BETWEEN NgayBatDau AND NgayKetThuc
ORDER BY MaKM;
COMMIT;