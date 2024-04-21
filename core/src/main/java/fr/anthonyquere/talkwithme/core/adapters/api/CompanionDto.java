package fr.anthonyquere.talkwithme.core.adapters.api;

import fr.anthonyquere.talkwithme.core.hexa.domains.Companion;
import lombok.Builder;

@Builder
public record CompanionDto(
  String id,
  String name,
  String background,
  String gender,
  String species
) {
  public static CompanionDto fromDomain(Companion domain) {
    return CompanionDto.builder()
      .id(domain.id())
      .background(domain.background())
      .gender(domain.gender())
      .species(domain.species())
      .name(domain.name())
      .build();
  }
}
