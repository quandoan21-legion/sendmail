package org.t2404e.sendmail.Controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.t2404e.sendmail.DTO.MailCampaignRequest;
import org.t2404e.sendmail.Service.MailQueueService;
import org.t2404e.sendmail.Service.MailService;
import org.t2404e.sendmail.Service.SaveEmailService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mail-campaign")
public class MailCampaignController {
    private final MailQueueService mailQueueService;
    private final MailService mailService;
    private final SaveEmailService saveEmailService;

    public MailCampaignController(MailQueueService mailQueueService, MailService mailService, SaveEmailService saveEmailService) {
        this.mailQueueService = mailQueueService;
        this.mailService = mailService;
        this.saveEmailService = saveEmailService;
    }

    private boolean isValidEmailList(String emails) {
        if (emails == null || emails.trim().isEmpty()) return false;

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        String[] list = emails.split(",");
        for (String e : list) {
            if (!e.trim().matches(emailRegex)) {
                return false;
            }
        }
        return true;
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("mailCampaignRequest", new MailCampaignRequest());
        return "mail-form";
    }

    @PostMapping("/send")
    public String sendCampaign(@ModelAttribute MailCampaignRequest request, Model model) {
        String recipients = request.getRecipients();

        if (recipients == null || recipients.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập email.");
            return "mail-form";
        }

        if (!isValidEmailList(recipients)) {
            model.addAttribute("error", "Email không hợp lệ. Vui lòng nhập đúng định dạng email.");
            return "mail-form";
        }

        try {
            // Gửi mail
            mailQueueService.addMail(request);
            mailService.processMailQueue();

            // Lưu email vào service
            for (String email : recipients.split(",")) {
                saveEmailService.addEmail(email.trim());
            }

            model.addAttribute("message", "Mail campaign queued and sending started!");
        } catch (Exception e) {
            model.addAttribute("message", "Failed: " + e.getMessage());
        }

        return "mail-result";
    }

    // Endpoint gợi ý email khi người dùng nhập ký tự
    @GetMapping("/suggest-emails")
    @ResponseBody
    public List<String> suggestEmails(@RequestParam("q") String query) {
        String lowerQuery = query.toLowerCase();
        return saveEmailService.getSavedEmails().stream()
                .filter(email -> email.toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}
