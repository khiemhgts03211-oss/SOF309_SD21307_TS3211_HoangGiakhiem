package utils;

import config.EmailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailUtil {

    public static void sendOTP(String to, String otp)
            throws MessagingException {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
                props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                EmailConfig.EMAIL,
                                EmailConfig.PASSWORD);
                    }
                });

        Message message = new MimeMessage(session);

        message.setFrom(
                new InternetAddress(EmailConfig.EMAIL));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to));

        message.setSubject(
                "Movie Ticket Management - OTP");

        message.setText(
                "Mã OTP của bạn là: "
                        + otp
                        + "\n\nOTP có hiệu lực trong 5 phút.");

        Transport.send(message);
    }

}