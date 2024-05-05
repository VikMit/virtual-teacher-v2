package com.project.virtualteacher.service.contracts;

public interface MailTemplatesGeneratorService {

    String generateConfirmationEmail(String username, String name, String link);
}
