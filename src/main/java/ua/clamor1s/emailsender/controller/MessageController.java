package ua.clamor1s.emailsender.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.clamor1s.emailsender.dto.MessageSaveDto;
import ua.clamor1s.emailsender.service.MessageService;

@RestController
@RequestMapping("/api/sendMessage")
public class MessageController {

    @Autowired
    private MessageService service;

    @PostMapping
    public ResponseEntity<?> receiveMessage(@Valid @RequestBody MessageSaveDto message) {
        return service.sendMessage(message);
    }

}
