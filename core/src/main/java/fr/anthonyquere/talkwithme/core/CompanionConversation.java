package fr.anthonyquere.talkwithme.core;

import fr.anthonyquere.talkwithme.core.data.jpa.conversations.Message;
import fr.anthonyquere.talkwithme.core.domains.Companion;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record CompanionConversation(
  String userId,
  String companionId,
  Companion companion,
  List<Message> messages
) {
}
