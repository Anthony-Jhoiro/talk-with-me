package fr.anthonyquere.talkwithme.core.hexa.domains;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record Message(
  UUID id,
  String message,
  Type type,
  Status status,
  LocalDateTime createdAt
) {

  public enum Type {
    USER,
    SYSTEM,
    AI;
  }

  public enum Status {
    ARCHIVED,
    NOT_ARCHIVED;
  }
}
