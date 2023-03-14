package ua.clamor1s.emailsender.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
@Jacksonized
public class MessageSaveDto {

    private String subject;

    private String content;

    @Email(message = "sender field has to be valid email address.")
    private String sender;

    List<String> receivers;

}
