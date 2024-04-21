package fr.anthonyquere.talkwithme.core.hexa.domains;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record CompanionConversation(
  String userId,
  String companionId,
  Companion companion,
  List<Message> messages
) {

  public record Id(String companionId, String userId) {
  }
}
