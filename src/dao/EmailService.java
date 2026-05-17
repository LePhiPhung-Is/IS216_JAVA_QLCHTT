package src.dao;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    
    // ĐIỀN THÔNG TIN TÀI KHOẢN GỬI VÀO ĐÂY
    private static final String FROM_EMAIL = "hnhutgm@gmail.com"; 
    private static final String PASSWORD = "egcfjyvpcaskbqsu";// 16 ký tự viết liền không dấu cách

    public static boolean sendOTP(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã OTP Khôi Phục Mật Khẩu - Beauty Shop");
            
            // Thiết kế nội dung Email bằng HTML cho đẹp mắt
            String htmlContent = "<h3>Xin chào!</h3>"
                    + "<p>Bạn đã yêu cầu khôi phục mật khẩu tại hệ thống Beauty Shop.</p>"
                    + "<p>Mã OTP của bạn là: <b style='color:red; font-size:24px;'>" + otp + "</b></p>"
                    + "<p>Mã này có hiệu lực sử dụng 1 lần. Vui lòng không chia sẻ cho bất kỳ ai.</p>";
            
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Đã gửi OTP thành công tới: " + toEmail);
            return true;

        } catch (Exception e) {
            System.out.println("LỖI GỬI MAIL: " + e.getMessage());
            return false;
        }
    }
}