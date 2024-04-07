package fr.anthonyquere.talkwithme.core.data.jpa.conversations;

import fr.anthonyquere.talkwithme.core.CompanionConversation;
import fr.anthonyquere.talkwithme.core.CompanionConversationStorage;
import fr.anthonyquere.talkwithme.core.CompanionStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationsRepository implements CompanionConversationStorage {
  private final MessageJpaRepository messageJpaRepository;
  private final CompanionStorage companionStorage;

  @Override
  public CompanionConversation getConversationByCompanionAndUser(String companionId, String userId) {
    var messages = messageJpaRepository.getMessagesByCompanionIdAndUserIdOrderByCreatedAtDesc(companionId, userId, Pageable.ofSize(Integer.MAX_VALUE));

    return CompanionConversation.builder()
      .companionId(companionId)
      .userId(userId)
      .messages(messages)
      .build();
  }
}
