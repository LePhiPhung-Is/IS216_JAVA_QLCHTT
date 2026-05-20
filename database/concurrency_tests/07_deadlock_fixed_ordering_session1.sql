-- =====================================================
-- 07_deadlock_fixed_ordering_session1.sql
-- DEADLOCK - PHIÊN BẢN ĐÃ SỬA - SESSION 1
-- Hai session cùng khóa sản phẩm theo thứ tự SP01 -> SP02
-- =====================================================

ROLLBACK;

-- BƯỚC 1:
-- Session 1 khóa SP01 trước.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP01';

-- BƯỚC 2:
-- Session 1 khóa SP02 sau.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP02';

-- CHƯA COMMIT.
-- DỪNG Ở ĐÂY.
-- Chuyển sang Session 2 chạy file 07_deadlock_fixed_ordering_session2.sql.
-- Session 2 sẽ bị chờ ở SP01, nhưng không tạo deadlock.


-- =====================================================
-- SAU KHI THẤY SESSION 2 BỊ CHỜ, QUAY LẠI ĐÂY
-- =====================================================

COMMIT;

SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP IN ('SP01', 'SP02')
ORDER BY MaSP;