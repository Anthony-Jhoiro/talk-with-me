package fr.anthonyquere.talkwithme.core.adapters.data.jpa.conversations;

import fr.anthonyquere.talkwithme.core.hexa.domains.Message;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
public class MessageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(length = 512)
  private String message;

  @Column(length = 24)
  private String type;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private Status status = Status.NOT_ARCHIVED;

  @CreatedDate
  @OrderBy
  private LocalDateTime createdAt;

  private String companionId;
  private String userId;

  @Override
  public String toString() {
    return type + ": " + message;
  }

  public Message toDomain() {
    return Message.builder()
      .id(id)
      .message(message)
      .createdAt(createdAt)
      .type(Message.Type.valueOf(type))
      .status(Message.Status.valueOf(status.name()))
      .build();
  }

  public enum Status {
    ARCHIVED,
    NOT_ARCHIVED
  }
}
