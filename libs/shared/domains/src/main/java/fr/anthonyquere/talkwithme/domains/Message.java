package fr.anthonyquere.talkwithme.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
  protected UUID id;
  protected String message;
  protected String type;

  @Builder.Default
  protected Status status = Status.NOT_ARCHIVED;
  protected LocalDateTime createdAt;

  protected Companion companion;

  public enum Status {
    ARCHIVED,
    NOT_ARCHIVED
  }
}
