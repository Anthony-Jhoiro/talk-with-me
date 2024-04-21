package fr.anthonyquere.talkwithme.core.adapters.data.jpa.conversations;

import fr.anthonyquere.talkwithme.core.hexa.CompanionConversationStorage;
import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionConversation;
import fr.anthonyquere.talkwithme.core.hexa.domains.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConversationsRepository implements CompanionConversationStorage {
  private final MessageJpaRepository messageJpaRepository;

  @Override
  public CompanionConversation getConversationByCompanionAndUser(String companionId, String userId) {
    var messages = messageJpaRepository.getMessagesByCompanionIdAndUserIdOrderByCreatedAtDesc(companionId, userId, Pageable.ofSize(Integer.MAX_VALUE));

    return CompanionConversation.builder()
      .companionId(companionId)
      .userId(userId)
      .messages(messages.stream().map(MessageEntity::toDomain).toList())
      .build();
  }

  @Override
  public void saveMessage(CompanionConversation.Id conversationId, Message message) {
    var messageEntity = MessageEntity.builder()
      .id(UUID.randomUUID())
      .message(message.message())
      .status(Optional.ofNullable(message.status()).map(status -> MessageEntity.Status.valueOf(status.name())).orElse(MessageEntity.Status.NOT_ARCHIVED))
      .type(message.type().name())

      .companionId(conversationId.companionId())
      .userId(conversationId.userId())
      .createdAt(LocalDateTime.now())
      .build();

    messageJpaRepository.save(messageEntity);
  }

  @Override
  public void clearDiscussion(CompanionConversation.Id conversationId) {
    messageJpaRepository.deleteByCompanionIdAndUserId(
      conversationId.companionId(),
      conversationId.userId()
    );
  }

  @Override
  public void setIntroductionMessage(CompanionConversation.Id conversationId, Message message) {
    var systemMessage = messageJpaRepository.findByCompanionIdAndUserIdAndType(conversationId.companionId(), conversationId.userId(), "SYSTEM")
      .orElse(MessageEntity.builder()
        .companionId(conversationId.companionId())
        .userId(conversationId.userId())
        .createdAt(LocalDateTime.now())
        .type("SYSTEM")
        .status(MessageEntity.Status.NOT_ARCHIVED)
        .build());

    systemMessage.setMessage(message.message());
    messageJpaRepository.save(systemMessage);
  }

}
