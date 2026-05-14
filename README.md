# 👗 Hệ Thống Quản Lý Cửa Hàng Thời Trang - Beauty Shop

## 📖 Mô tả dự án

Hệ thống quản lý cửa hàng thời trang **Beauty Shop** là một ứng dụng được thiết kế để phục vụ và hỗ trợ việc thực thi công việc của các tác nhân trong cửa hàng. Hệ thống hướng tới việc tối ưu hóa quy trình làm việc cho:

- **Nhân viên bán hàng:** Lập hóa đơn, quản lý khách hàng.
- **Nhân viên kho:** Nhập/xuất kho, kiểm kê hàng hóa.
- **Quản lý cửa hàng:** Theo dõi doanh thu, quản lý nhân sự, thiết lập chương trình khuyến mãi.

Hệ thống được xây dựng nhằm **tự động hóa các nghiệp vụ cốt lõi**: bán hàng, quản lý kho, quản lý khách hàng, nhà cung cấp và cung cấp hệ thống báo cáo thống kê đa chiều.

---

## 🎯 Mục tiêu dự án

Dự án hướng đến các mục tiêu cụ thể nhằm tạo ra một phần mềm quản lý toàn diện, đồng thời nâng cao trải nghiệm cho khách hàng:

**1. Đối với người dùng:**

- Giao diện thân thiện, trực quan, dễ sử dụng.
- An toàn, bảo mật thông tin dữ liệu tuyệt đối.
- Chạy ổn định trên các nền tảng và hệ điều hành phổ biến.

**2. Đối với chức năng:**

- Cung cấp đầy đủ các chức năng nghiệp vụ cốt lõi của một cửa hàng thời trang.
- Hỗ trợ linh hoạt cả hai hình thức: bán hàng tại quầy (Offline) và bán hàng trực tuyến (Online).

**3. Về hiệu quả quản trị:**

- Quản lý tập trung danh sách khách hàng và nhà cung cấp.
- Cung cấp số liệu thống kê chính xác về doanh thu, sản phẩm bán chạy và tình trạng tồn kho.

---

## 👥 Danh sách thành viên & Phân công công việc

| STT | Họ và tên           |   MSSV   | Vai trò                       | Thiết bị & Công cụ                               | Trách nhiệm chính (Core Responsibilities)                                                                                                                                            |
| :-: | :------------------ | :------: | :---------------------------- | :----------------------------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|  1  | **Lê Văn Hữu Phúc** | 24521381 | Product Owner                 | Asus Vivobook (Windows), Notion, Oracle, VS Code | - **Quản trị Yêu cầu:** Chịu trách nhiệm cao nhất về Product Backlog, chốt yêu cầu nghiệp vụ.<br>- **Giao diện:** Thiết kế UI/UX, đảm bảo trải nghiệm người dùng mượt mà.            |
|  2  | **Lê Hoàng Nhựt**   | 24521284 | Scrum Master / System Analyst | Macbook (macOS), Notion, StarUML, VS Code        | - **Điều phối tiến độ:** Tổ chức Daily Scrum, quản lý tiến độ Sprints và gỡ bỏ rào cản (blockers).<br>- **Phân tích:** Phân tích hệ thống, vẽ sơ đồ UML (Use Case, Sequence, Class). |
|  3  | **Phùng Lê Phi**    | 24521324 | Backend Developer             | Dell (Windows), Java, GitHub, VS Code            | - **Lập trình lõi:** Xây dựng Business Logic, phát triển API và xử lý luồng dữ liệu.<br>- **Tích hợp:** Quản lý mã nguồn trên GitHub và xử lý xung đột code.                         |
|  4  | **Đoàn Xuân Chiến** | 24520217 | DBA, Tester                   | MSI (Windows), Java, Oracle, SQL Server, VS Code | - **Dữ liệu:** Thiết kế, triển khai và tối ưu hóa hệ quản trị CSDL (Oracle).<br>- **Kiểm thử:** Thực hiện Unit Test, test lỗi giao diện và đối chiếu luồng Database.                 |

---

## ⚙️ Hướng dẫn cài đặt (Installation)

Để chạy dự án trên máy cá nhân (Local environment), yêu cầu hệ thống phải cài đặt sẵn các phần mềm sau:

- **Ngôn ngữ lập trình:** Java Development Kit (JDK) phiên bản **19 trở lên**.
- **Cơ sở dữ liệu:** **Oracle Database**.
- **IDE khuyên dùng:** VS Code hoặc IntelliJ IDEA.

---

## 🛠 Định nghĩa hoàn thành (Definition of Done - DoD)

Để đảm bảo chất lượng mã nguồn, nhóm quy định mọi tính năng trước khi gộp vào nhánh `main` phải thỏa mãn các tiêu chí sau:

| STT | Tiêu chí (DoD)   | Yêu cầu bắt buộc                                                        |
| :-- | :--------------- | :---------------------------------------------------------------------- |
| 1   | **Đã code xong** | Mã nguồn (Java/Oracle) không lỗi cú pháp, giao diện đúng Figma.         |
| 2   | **Đã Unit Test** | Đã chạy thử nghiệm luồng cơ bản thành công trên máy Local.              |
| 3   | **Đã Review**    | Mã nguồn được tạo Pull Request và có ít nhất 1 thành viên khác Approve. |

## ⚠️ Quy trình tạo Pull Request (Bắt buộc)

Khi một thành viên hoàn thành task và tạo Pull Request (PR), **VUI LÒNG COPY CHECKLIST DƯỚI ĐÂY** và dán vào phần mô tả của PR, sau đó tick ✅ xác nhận trước khi nhờ người khác review:

- [ ] Tôi xác nhận mã nguồn đã code xong và chạy tốt.
- [ ] Tôi xác nhận đã tự Unit Test thành công trên máy của mình.
- [ ] Tôi yêu cầu thành viên khác kiểm tra (Review) giúp tôi.
