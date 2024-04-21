package fr.anthonyquere.talkwithme.core.adapters.api;

import fr.anthonyquere.talkwithme.core.hexa.CompanionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/companions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET})
@Slf4j
public class CompanionResource {

  private final CompanionService companionService;

  @GetMapping("/{companionId}")
  public CompanionDto getCompanionByID(@PathVariable String companionId) {
    log.info("GET getCompanionByID companion={}", companionId);
    return CompanionDto.fromDomain(companionService.getCompanionById(companionId));
  }

  @GetMapping("")
  public Collection<CompanionDto> listCompanions() {
    log.info("GET listCompanions");
    return companionService.listAllCompanions().stream().map(CompanionDto::fromDomain).toList();
  }

  @PostMapping("/{companionId}/conversation/{userId}")
  public CompanionConversationDto sendMessage(@PathVariable String companionId, @PathVariable String userId, @RequestBody TalkRequestPayload talkRequestPayload) {
    log.info("POST sendMessage companion={}", companionId);
    companionService.sendMessage(companionId, userId, talkRequestPayload.question);
    return CompanionConversationDto.fromDomain(companionService.getConversationById(companionId, userId));
  }

  @GetMapping("/{companionId}/conversation/{userId}")
  public CompanionConversationDto getConversation(@PathVariable String companionId, @PathVariable String userId) {
    log.info("GET getConversation companion={}", companionId);
    return CompanionConversationDto.fromDomain(companionService.getConversationById(companionId, userId));
  }

  @PostMapping("/{companionId}/conversation")
  public CompanionConversationDto sendMessageToCurrentUser(@PathVariable String companionId, @RequestBody TalkRequestPayload talkRequestPayload, Principal principal) {
    var userId = Optional.ofNullable(principal).map(Principal::getName).orElse("unknown");
    log.info("POST sendMessage user={} companion={}", userId, companionId);
    companionService.sendMessage(companionId, userId, talkRequestPayload.question);
    return CompanionConversationDto.fromDomain(companionService.getConversationById(companionId, userId));
  }

  @GetMapping("/{companionId}/conversation")
  public CompanionConversationDto getConversationWithCurrentUser(@PathVariable String companionId, Principal principal) {
    var userId = Optional.ofNullable(principal).map(Principal::getName).orElse("unknown");
    log.info("GET getConversation user={} companion={}", userId, companionId);

    return CompanionConversationDto.fromDomain(companionService.getConversationById(companionId, userId));
  }

  public record TalkRequestPayload(String question) {
  }
}
