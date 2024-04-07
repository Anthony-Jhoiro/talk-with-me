package fr.anthonyquere.talkwithme.core.api;

import fr.anthonyquere.talkwithme.core.CompanionConversation;
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
