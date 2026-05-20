-- =====================================================
-- 03_dirty_read_session2.sql
-- DIRTY READ - SESSION 2
-- =====================================================

-- BƯỚC 4:
-- Session 2 đọc SP03 khi Session 1 đã UPDATE nhưng chưa COMMIT.
SELECT MaSP, TenSP, GiaBan
FROM SANPHAM
WHERE MaSP = 'SP03';

-- Kết quả mong đợi:
-- Session 2 KHÔNG thấy GiaBan = 999999.
-- Session 2 chỉ thấy giá trị cũ đã được commit trước đó.