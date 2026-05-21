-- =====================================================
-- 09_demo_sanpham_lock_session1.sql
-- Demo xử lý đồng thời trên chức năng Quản lý sản phẩm
-- Session 1 khóa sản phẩm SP01
-- =====================================================

ROLLBACK;

-- BƯỚC 1:
-- Khóa dòng sản phẩm SP01.
-- Khi dòng này đang bị khóa, các giao tác khác muốn sửa SP01 sẽ phải chờ.
SELECT MaSP, TenSP, GiaBan, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP = 'SP01'
FOR UPDATE;

-- BƯỚC 2:
-- Giả lập nhân viên kho đang cập nhật sản phẩm nhưng chưa hoàn tất.
UPDATE SANPHAM
SET SoLuongTon = SoLuongTon - 2
WHERE MaSP = 'SP01';

-- KHÔNG COMMIT VỘI.
-- Dừng ở đây, mở app Java ở Session 2.
-- Vào màn hình Quản lý sản phẩm và thử sửa SP01.
-- App Java sẽ bị chờ vì SP01 đang bị khóa bởi Session 1.


-- =====================================================
-- SAU KHI ĐÃ CHỤP HÌNH APP JAVA BỊ CHỜ, CHẠY LỆNH DƯỚI
-- =====================================================

COMMIT;
SELECT MaSP, TenSP, GiaBan, SoLuongTon, TrangThai
FROM SANPHAM
WHERE MaSP = 'SP01';