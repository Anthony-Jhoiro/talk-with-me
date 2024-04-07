package fr.anthonyquere.talkwithme.core.api;

import fr.anthonyquere.talkwithme.core.CompanionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/companions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CompanionResource {

  private final CompanionService companionService;

  @GetMapping("/{companionId}")
  public CompanionDto getCompanionByID(@PathVariable String companionId) {
    return CompanionDto.fromDomain(companionService.getCompanionById(companionId));
  }

  @GetMapping("")
  public Collection<CompanionDto> listCompanions() {
    return companionService.listAllCompanions().stream().map(CompanionDto::fromDomain).toList();
  }

  @PostMapping("/{companionId}/discussion/{userId}")
  public CompanionConversationDto sendMessage(@PathVariable String companionId, @PathVariable String userId, TalkRequestPayload talkRequestPayload) {
    companionService.sendMessage(companionId, userId, talkRequestPayload.question);
    return CompanionConversationDto.fromDomain(companionService.getConversationById(companionId, userId));
  }

  @GetMapping("/{companionId}/discussion/{userId}")
  public CompanionConversationDto getDiscussion(@PathVariable String companionId, @PathVariable String userId) {
    return CompanionConversationDto.fromDomain(companionService.getConversationById(companionId, userId));
  }

  public record TalkRequestPayload(String question) {
  }
}
