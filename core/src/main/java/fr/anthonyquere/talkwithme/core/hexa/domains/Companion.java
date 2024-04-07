package fr.anthonyquere.talkwithme.core.hexa.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Companion {
  private String id;
  private String name;
  private String background;
  private String gender;
  private String species;

  public String id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public String background() {
    return this.background;
  }

  public String gender() {
    return this.gender;
  }

  public String species() {
    return this.species;
  }

  public void id(String id) {
    this.id = id;
  }

  public void name(String name) {
    this.name = name;
  }

  public void background(String background) {
    this.background = background;
  }

  public void gender(String gender) {
    this.gender = gender;
  }

  public void species(String species) {
    this.species = species;
  }
}
