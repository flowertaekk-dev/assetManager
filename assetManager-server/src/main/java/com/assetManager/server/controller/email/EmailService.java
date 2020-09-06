package com.assetManager.server.controller.email;

import com.assetManager.server.controller.CommonResponseResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@PropertySource("classpath:application-email.properties")
@RequiredArgsConstructor
@Service
public class EmailService {
    private static Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String adminEmail;

    private final JavaMailSender mailSender;

    public CommonResponseResult sendEmail(String to) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(adminEmail));
                mimeMessage.setSubject("AssetManager입니다!");
                mimeMessage.setText("Hello World!");
            }
        };

        mailSender.send(preparator);

        return CommonResponseResult.SUCCESS;
    }
}
