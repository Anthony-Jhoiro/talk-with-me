package fr.anthonyquere.talkwithme.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter()
public class Companion {
  protected String id;
  protected String name;
  protected String background;
}
