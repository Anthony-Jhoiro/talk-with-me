package fr.anthonyquere.talkwithme.core.crud.companions;

import fr.anthonyquere.talkwithme.core.adapters.data.md.companions.MarkdownCompanionRepository;
import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionRetrieveError;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MarkdownCompanionRepositoryTest {

  @Test
  void getById_neighbor_exists() {
    var name = "vulpis";

    var companion = new MarkdownCompanionRepository().getById(name);

    assertThat(companion.background()).isEqualTo("Vulpis est très curieux mais très timide. De nature casanière, il a appris tout ce qu'il sait grâce aux livres.");
    assertThat(companion.id()).isEqualTo("vulpis");
    assertThat(companion.name()).isEqualTo("Vulpis");
    assertThat(companion.gender()).isEqualTo("male");
    assertThat(companion.species()).isEqualTo("fox");
  }

  @Test
  void getById_neighbor_does_not_exists() {
    var name = "Léodagan de Carmélide";

    assertThatThrownBy(
      () -> new MarkdownCompanionRepository().getById(name)
    ).isInstanceOf(CompanionRetrieveError.class);
  }
}
