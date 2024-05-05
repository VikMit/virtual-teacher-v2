package com.project.virtualteacher.service;

import com.project.virtualteacher.service.contracts.MailService;
import com.project.virtualteacher.service.contracts.MailTemplatesGeneratorService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private final Session emailSession;
    private final MailTemplatesGeneratorService templateGenerator;

    public MailServiceImpl(Session emailSession, MailTemplatesGeneratorService templateGenerator) {
        this.emailSession = emailSession;
        this.templateGenerator = templateGenerator;
    }

    @Override
    public void sendConfirmRegistration(String username, String name, String link,String sendTo) throws MessagingException {
        Message message = new MimeMessage(emailSession);
        String template = templateGenerator.generateConfirmationEmail(username, name, link);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
        message.setContent(template, "text/html");
        Transport.send(message);
    }
}
