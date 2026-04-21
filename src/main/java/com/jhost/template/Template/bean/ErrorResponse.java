package com.jhost.template.Template.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.context.request.WebRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private String contentPath;
    private String description;

    public ErrorResponse(String msg, WebRequest webRequest, boolean includeClientInfo) {
        this.message = msg;
        this.contentPath = webRequest.getContextPath();
        this.description = webRequest.getDescription(includeClientInfo);
    }

}
