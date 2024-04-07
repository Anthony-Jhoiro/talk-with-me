package fr.anthonyquere.talkwithme.core.hexa.domains;

import lombok.Builder;

@Builder
public record Companion(
  String id,
  String name,
  String background,
  String gender,
  String species
) {

}
