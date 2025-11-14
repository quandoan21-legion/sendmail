package org.t2404e.sendmail.Controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.t2404e.sendmail.DTO.MailCampaignRequest;
import org.t2404e.sendmail.Service.MailQueueService;
import org.t2404e.sendmail.Service.MailService;
import org.t2404e.sendmail.Service.SendEmailService;

@Controller
@RequestMapping("/mail-campaign")
public class MailCampaignController {
    private final MailQueueService mailQueueService;
    private final MailService mailService;
    private final SendEmailService sendEmailService;
    public MailCampaignController(MailQueueService mailQueueService, MailService mailService, SendEmailService sendEmailService) {
        this.mailQueueService = mailQueueService;
        this.mailService = mailService;

        this.sendEmailService = sendEmailService;
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("mailCampaignRequest", new MailCampaignRequest());
        return "mail-form";
    }

    @PostMapping("/send")
    public String sendCampaign(@ModelAttribute MailCampaignRequest request, Model model) {
        try {
            // Thêm mail vào queue
            mailQueueService.addMail(request);
            // Bắt đầu gửi mail trong thread
            mailService.processMailQueue();
            // Lưu email vào lịch sử
                String[] emails = request.getRecipients().split(",");
            for (String e : emails) {
                sendEmailService.addEmail(e.trim());
            }

            model.addAttribute("message", "Mail campaign queued and sending started!");
        } catch (Exception e) {
            model.addAttribute("message", "Failed: " + e.getMessage());
        }
        return "mail-result";
    }

}
