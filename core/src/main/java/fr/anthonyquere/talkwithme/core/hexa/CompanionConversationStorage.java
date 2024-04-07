package fr.anthonyquere.talkwithme.core.hexa;

import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionConversation;
import fr.anthonyquere.talkwithme.core.hexa.domains.Message;

public interface CompanionConversationStorage {
  CompanionConversation getConversationByCompanionAndUser(String companionId, String userId);

  void saveMessage(CompanionConversation.Id conversationId, Message message);

  void setIntroductionMessage(CompanionConversation.Id conversationId, Message message);

  void clearDiscussion(CompanionConversation.Id conversationId);
}
