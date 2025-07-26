package com.gaz.demo.bot.controller;

import com.gaz.demo.bot.AutoGasBot;
import com.gaz.demo.bot.configs.BotConfig;
import com.gaz.demo.bot.models.dto.ApplicationDto;
import com.gaz.demo.bot.services.WebApplicationServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@Slf4j
public class ApplicationRestController {

    private final WebApplicationServices services;
    @PostMapping("/sendApp")
    public ResponseEntity<?> receiveWebApp(@RequestBody ApplicationDto dto) {
        log.info("Получена заявка с сайта от: {}", dto.getName());
        services.sendToAdmin(dto);
        return ResponseEntity.ok().body("Заявка успешно отправлена");
    }
}
