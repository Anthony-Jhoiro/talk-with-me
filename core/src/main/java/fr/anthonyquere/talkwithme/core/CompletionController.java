package fr.anthonyquere.talkwithme.core;

import fr.anthonyquere.talkwithme.core.crud.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companions")
public class CompletionController {
  private final CompletionService completionService;

  @PostMapping("/{companionId}/talk")
  public Message talkToCompanion(
    @PathVariable("companionId") String companionId,
    @RequestBody() TalkRequestPayload payload
  ) throws Exception {
    var question = Optional.ofNullable(payload.question()).orElse("Hello! What's up today?");
    return completionService.complete(companionId, question);
  }

  public record TalkRequestPayload(String question) {

  }

}
