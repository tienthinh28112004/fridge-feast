package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailSenderService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.application.name}")
    private String webName;//apiWebManga

    @Value("${spring.mail.username}")
    private String senderAddress;//tienthinh28112004@gmail.com

    public void sendEmailUser(User user) throws MessagingException, MailException {//bắt lối gửi mail
        try{
            //String otp = user.getEmailVerificationToken().getToken(); // Lấy mã OTP từ token

            // Tạo nội dung HTML trực tiếp
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head><meta charset=\"UTF-8\"/><title>Email Verification</title></head>" +
                    "<body style=\"font-family: Arial, sans-serif; text-align: center;\">" +
                    "<h2 style=\"color: #333;\">Xác minh tài khoản của bạn</h2>" +
                    "<p>Cảm ơn bạn đã đăng ký! Dưới đây là mã xác minh (OTP) của bạn:</p>" +
                    //"<h3 style=\"font-size: 24px; color: #007bff;\">" + otp + "</h3>" +
                    "<p>Vui lòng nhập mã này trên trang web của chúng tôi để hoàn tất xác minh.</p>" +
                    "<p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>" +
                    "<hr style=\"margin: 20px 0;\">" +
                    "<p style=\"font-size: 12px; color: #666;\">© 2024 " + webName + ". Mọi quyền được bảo lưu.</p>" +
                    "</body>" +
                    "</html>";
            MimeMessage message=javaMailSender.createMimeMessage();//tạo đối tượng MIME giúp hỗ trợ các ngôn ng văn bản tệp đính kèm
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(message,true);//true ở đây tức là cho phép hỗ trợ nhiều dạng như văn abnr thuần hay html
            mimeMessageHelper.setFrom(senderAddress);//địa chỉ gửi
            mimeMessageHelper.setTo(user.getEmail());//địa chỉ email người nhận
            mimeMessageHelper.setSubject("Verify Email Your:");
            mimeMessageHelper.setText(htmlContent,true);//true là để xác nhận gửi bằng html

            javaMailSender.send(message);//gửi thông tin
        }catch (RuntimeException e){
            log.error("xác minh email thất bại");
        }
    }

}
