package pl.msc.demoboot.Email.emailImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.msc.demoboot.Email.emailInterface.EmailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("emailSender")
public class EmailSenderImpl implements EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendEmail(String to, String subject, String content) {
        MimeMessage mail = javaMailSender.createMimeMessage(); //Tutaj tworzymy wiadomosc, jako tekst czy jako html

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom("noreply@dogs.com");
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException e){
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }
}
