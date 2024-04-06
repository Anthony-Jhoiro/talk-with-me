package fr.anthonyquere.talkwithme.core.api;

import fr.anthonyquere.talkwithme.core.data.md.companions.MarkdownCompanionRepository;
import fr.anthonyquere.talkwithme.core.domains.Companion;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/companions")
@RequiredArgsConstructor
public class CompanionResource {

  private final MarkdownCompanionRepository markdownCompanionRepository;

  @GetMapping("/{companionId}")
  public Companion getCompanionByID(@PathVariable String companionId) {
    return markdownCompanionRepository.getById(companionId);
  }

  @GetMapping("")
  public Collection<Companion> listCompanions() {
    return markdownCompanionRepository.findAll();
  }
}
