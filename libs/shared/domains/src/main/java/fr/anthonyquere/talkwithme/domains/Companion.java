package fr.anthonyquere.talkwithme.domains;

import lombok.Builder;

@Builder
public record Companion(
  String id,
  String name,
  String background
) {
}
