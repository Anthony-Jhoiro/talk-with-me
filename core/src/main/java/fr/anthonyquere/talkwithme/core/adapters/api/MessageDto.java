package fr.anthonyquere.talkwithme.core.adapters.api;

import fr.anthonyquere.talkwithme.core.hexa.domains.Message;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
  String id,
  String message,
  Message.Type type,
  Message.Status status,
  LocalDateTime createdAt
) {
  public static MessageDto fromDomain(Message domain) {
    return MessageDto.builder()
      .createdAt(domain.createdAt())
      .id(domain.id().toString())
      .message(domain.message())
      .status(domain.status())
      .type(domain.type())
      .build();
  }

}
