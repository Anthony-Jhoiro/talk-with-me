package fr.anthonyquere.talkwithme.core.data.jpa.conversations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OrderBy;
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
public class Message {

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

  public enum Status {
    ARCHIVED,
    NOT_ARCHIVED
  }
}
