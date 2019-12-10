package pl.msc.demoboot.Email.emailInterface;

public interface EmailSender {
    void sendEmail(String to, String subject, String content);
}
