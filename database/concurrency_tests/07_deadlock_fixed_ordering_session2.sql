-- =====================================================
-- 07_deadlock_fixed_ordering_session2.sql
-- DEADLOCK - PHIÊN BẢN ĐÃ SỬA - SESSION 2
-- Hai session cùng khóa sản phẩm theo thứ tự SP01 -> SP02
-- =====================================================

ROLLBACK;

-- BƯỚC 3:
-- Session 2 cũng khóa SP01 trước.
-- Nếu Session 1 chưa commit, câu lệnh này sẽ bị chờ.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP01';

-- Sau khi Session 1 commit, Session 2 tiếp tục khóa SP02.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon
WHERE MaSP = 'SP02';

COMMIT;

SELECT MaSP, TenSP, SoLuongTon
FROM SANPHAM
WHERE MaSP IN ('SP01', 'SP02')
ORDER BY MaSP;