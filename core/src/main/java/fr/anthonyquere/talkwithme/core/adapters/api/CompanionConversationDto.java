package fr.anthonyquere.talkwithme.core.adapters.api;

import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionConversation;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Builder
public record CompanionConversationDto(
  String userId,
  CompanionDto companion,
  List<MessageDto> messages
) {

  public static CompanionConversationDto fromDomain(CompanionConversation domain) {
    var messages = new ArrayList<>(domain.messages().stream().map(MessageDto::fromDomain).toList());
    Collections.reverse(messages);
    return CompanionConversationDto.builder()
      .userId(domain.userId())
      .companion(CompanionDto.fromDomain(domain.companion()))
      .messages(messages)
      .build();
  }

}
