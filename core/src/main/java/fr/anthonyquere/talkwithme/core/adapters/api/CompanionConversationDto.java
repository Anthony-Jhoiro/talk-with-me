package fr.anthonyquere.talkwithme.core.adapters.api;

import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionConversation;
import lombok.Builder;

import java.util.List;


@Builder
public record CompanionConversationDto(
  String userId,
  CompanionDto companion,
  List<MessageDto> messages
) {

  public static CompanionConversationDto fromDomain(CompanionConversation domain) {
    return CompanionConversationDto.builder()
      .userId(domain.userId())
      .companion(CompanionDto.fromDomain(domain.companion()))
      .messages(domain.messages().stream().map(MessageDto::fromDomain).toList())
      .build();
  }

}
