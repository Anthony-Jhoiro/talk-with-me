package fr.anthonyquere.talkwithme.core.api;

import fr.anthonyquere.talkwithme.core.domains.Companion;
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
      .id(domain.getId())
      .background(domain.getBackground())
      .gender(domain.getGender())
      .species(domain.getSpecies())
      .name(domain.getName())
      .build();
  }
}
