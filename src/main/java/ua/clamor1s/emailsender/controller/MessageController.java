package ua.clamor1s.emailsender.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.clamor1s.emailsender.dto.MessageSaveDto;
import ua.clamor1s.emailsender.service.MessageService;
import ua.clamor1s.emailsender.service.MessageServiceImpl;

@RestController
@RequestMapping("/api/sendMessage")
public class MessageController {

    @Autowired
    private MessageServiceImpl service;

    @PostMapping
    public ResponseEntity<?> receiveMessage(@Valid @RequestBody MessageSaveDto message) {
        return service.sendMessage(message);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        service.sendMessage("yaroslavpopovich04@gmail.com", "Sub!", "Content!!!");
        return ResponseEntity.ok("ok");
    }

}
