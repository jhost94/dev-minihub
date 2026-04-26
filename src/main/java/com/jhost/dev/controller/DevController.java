package com.jhost.dev.controller;

import center.jhub.data.dto.in.dev.DevRestInDTO;
import com.jhost.dev.service.DevService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/dev")
public class DevController {

    private final DevService devService;

    public DevController(DevService devService) {
        this.devService = devService;
    }

    @GetMapping("/test")
    public String test(final Locale locale){
        return devService.getTestMessage(locale);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<?> getRest(@RequestBody DevRestInDTO dto) {
        return ResponseEntity.ok(devService.getRest(dto));
    }
}
