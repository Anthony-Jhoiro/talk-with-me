package fr.anthonyquere.talkwithme.core.crud.companions;

import fr.anthonyquere.talkwithme.core.data.md.companions.MarkdownCompanionRepository;
import fr.anthonyquere.talkwithme.core.domains.CompanionRetrieveError;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MarkdownCompanionRepositoryTest {

  @Test
  void getById_neighbor_exists() {
    var name = "vulpis";

    var companion = new MarkdownCompanionRepository().getById(name);

    assertThat(companion.getBackground()).isEqualTo("Vulpis est très curieux mais très timide. De nature casanière, il a appris tout ce qu'il sait grâce aux livres.");
    assertThat(companion.getId()).isEqualTo("vulpis");
    assertThat(companion.getName()).isEqualTo("Vulpis");
    assertThat(companion.getGender()).isEqualTo("male");
    assertThat(companion.getSpecies()).isEqualTo("fox");
  }

  @Test
  void getById_neighbor_does_not_exists() {
    var name = "Léodagan de Carmélide";

    assertThatThrownBy(
      () -> new MarkdownCompanionRepository().getById(name)
    ).isInstanceOf(CompanionRetrieveError.class);
  }
}
