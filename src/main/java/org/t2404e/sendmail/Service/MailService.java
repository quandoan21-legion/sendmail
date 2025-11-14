package org.t2404e.sendmail.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.t2404e.sendmail.DTO.MailCampaignRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final MailQueueService mailQueueService;

    public MailService(JavaMailSender mailSender, MailQueueService mailQueueService) {
        this.mailSender = mailSender;
        this.mailQueueService = mailQueueService;
    }

    // Gửi mail từ Queue
    @Async
    public void processMailQueue() {
        while (mailQueueService.hasMail()) {
            MailCampaignRequest request = mailQueueService.getNextMail();
            if (request != null && request.getRecipients() != null && !request.getRecipients().isBlank()) {
                try {
                    sendMail(request);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Gửi mail đơn lẻ
    public void sendMail(MailCampaignRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Chuyển chuỗi recipients thành danh sách hợp lệ
        List<String> recipientList = Arrays.stream(request.getRecipients().split(","))
                .map(String::trim)          // loại bỏ khoảng trắng đầu cuối
                .filter(s -> !s.isEmpty()) // loại bỏ chuỗi rỗng
                .collect(Collectors.toList());

        if (recipientList.isEmpty()) {
            throw new MessagingException("Recipients list is empty or invalid");
        }

        helper.setTo(recipientList.toArray(new String[0]));
        helper.setSubject(request.getTitle() != null ? request.getTitle() : "(No Title)");

        String html = "<h3>" + request.getTitle() + "</h3>"
                + "<p>" + request.getContent() + "</p>"
                + "<p><b>Product:</b> " + (request.getProduct() != null ? request.getProduct() : "") + "</p>";
        helper.setText(html, true);

        mailSender.send(message);
        logger.info("Mail sent to: {}", recipientList);
    }
}
