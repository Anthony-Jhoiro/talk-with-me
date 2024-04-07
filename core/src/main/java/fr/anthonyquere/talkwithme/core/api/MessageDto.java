package fr.anthonyquere.talkwithme.core.api;

import fr.anthonyquere.talkwithme.core.data.jpa.conversations.Message;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
  String id,
  String message,
  String type,
  Message.Status status,
  LocalDateTime createdAt
) {
  public static MessageDto fromDomain(Message domain) {
    return MessageDto.builder()
      .createdAt(domain.getCreatedAt())
      .id(domain.getId().toString())
      .message(domain.getMessage())
      .status(domain.getStatus())
      .build();
  }

}
