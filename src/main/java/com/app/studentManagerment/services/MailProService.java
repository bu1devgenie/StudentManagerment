package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.MailPro;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Objects;

public interface MailProService {
    JavaMailSender getJavaMailSender(MailPro mailPro);

    void sendSimpleMessage(
            String type, List<Objects> list, MailPro mailPro, JavaMailSender sender, String to, String subject, String text);

    void sendMessageWithHtmlTemplate(
            List<String> emails, MailPro mailPro, JavaMailSender sender, String subject, String pathToHTMLFile);
}
