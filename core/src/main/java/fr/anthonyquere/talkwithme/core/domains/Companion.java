package fr.anthonyquere.talkwithme.core.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Companion {
  private String id;
  private String name;
  private String background;
  private String gender;
  private String species;
}
