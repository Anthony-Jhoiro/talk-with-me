package fr.anthonyquere.talkwithme.core.ai.langchain;

import fr.anthonyquere.talkwithme.core.Completion;
import fr.anthonyquere.talkwithme.core.CompletionOutput;
import fr.anthonyquere.talkwithme.core.ai.langchain.services.TalkWithCompanion;
import fr.anthonyquere.talkwithme.core.crud.companions.Companion;
import fr.anthonyquere.talkwithme.core.events.NewAIMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service("Langchain")
@RequiredArgsConstructor
public class LangchainIACompletion implements Completion {

  private final TalkWithCompanion talkWithCompanion;

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public CompletionOutput answerMessage(Companion companion, String question) throws Exception {

    var response = talkWithCompanion.chat(companion, companion.getBackground(), question, "Jhoiro");

    // Publish event to build summary
    applicationEventPublisher.publishEvent(new NewAIMessageEvent(this, response, companion));

    return new CompletionOutput(response);
  }
}
