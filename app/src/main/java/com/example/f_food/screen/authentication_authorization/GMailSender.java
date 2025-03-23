package com.example.f_food.screen.authentication_authorization;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSender {
    private final String emailSender = "Bachnvhe172297@fpt.edu.vn";  // Thay bằng email của bạn
    private final String passwordSender = "rxwd pjue cgit pxku";  // Nhập App Password (16 ký tự)

    public void sendEmail(String emailRecipient, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSender, passwordSender);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailSender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRecipient));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
