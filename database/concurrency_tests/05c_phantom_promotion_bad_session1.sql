-- =====================================================
-- 05c_phantom_promotion_bad_session1.sql
-- PHANTOM READ - TÌNH HUỐNG 3 - SESSION 1
-- Nhân viên xem danh sách khuyến mãi đang áp dụng
-- =====================================================

ROLLBACK;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT MaKM, TenKM, TrangThai, NgayBatDau, NgayKetThuc
FROM KHUYENMAI
WHERE TrangThai = N'Đang áp dụng'
  AND SYSDATE BETWEEN NgayBatDau AND NgayKetThuc
ORDER BY MaKM;

-- DỪNG Ở ĐÂY.
-- Chuyển sang chạy file 05c_phantom_promotion_bad_session2.sql.


-- =====================================================
-- CHỈ CHẠY PHẦN DƯỚI SAU KHI SESSION 2 ĐÃ COMMIT
-- =====================================================

-- BƯỚC 3:
-- Session 1 xem lại danh sách khuyến mãi đang áp dụng.
SELECT MaKM, TenKM, TrangThai, NgayBatDau, NgayKetThuc
FROM KHUYENMAI
WHERE TrangThai = N'Đang áp dụng'
  AND SYSDATE BETWEEN NgayBatDau AND NgayKetThuc
ORDER BY MaKM;
COMMIT;