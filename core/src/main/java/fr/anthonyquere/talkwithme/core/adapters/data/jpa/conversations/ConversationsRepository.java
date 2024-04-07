package fr.anthonyquere.talkwithme.core.adapters.data.jpa.conversations;

import fr.anthonyquere.talkwithme.core.hexa.CompanionConversation;
import fr.anthonyquere.talkwithme.core.hexa.CompanionConversationStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
      .messages(messages)
      .build();
  }

  @Override
  public void saveMessage(CompanionConversation.Id conversationId, Message message) {
    message.setCompanionId(conversationId.companionId());
    message.setUserId(conversationId.userId());
    message.setCreatedAt(LocalDateTime.now());
    messageJpaRepository.save(message);
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
      .orElse(Message.builder()
        .companionId(conversationId.companionId())
        .userId(conversationId.userId())
        .createdAt(LocalDateTime.now())
        .type("SYSTEM")
        .status(Message.Status.NOT_ARCHIVED)
        .build());

    systemMessage.setMessage(message.getMessage());
    messageJpaRepository.save(systemMessage);
  }

}
