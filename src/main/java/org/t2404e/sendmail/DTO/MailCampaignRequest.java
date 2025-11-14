package org.t2404e.sendmail.DTO;

import lombok.Data;

@Data
public class MailCampaignRequest {
    private String recipients;
    private String title;
    private String content;
    private String product;
}
