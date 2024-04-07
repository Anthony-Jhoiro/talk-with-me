package fr.anthonyquere.talkwithme.core;

public interface CompanionConversationStorage {
  CompanionConversation getConversationByCompanionAndUser(String companionId, String userId);
}
