package com.project.virtualteacher.service.contracts;

import jakarta.mail.MessagingException;

public interface MailService {
    public void sendConfirmRegistration (String username, String name, String link,String sendTo) throws MessagingException;
}
