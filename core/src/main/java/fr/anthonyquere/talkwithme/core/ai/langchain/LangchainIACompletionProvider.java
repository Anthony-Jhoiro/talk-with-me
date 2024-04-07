package fr.anthonyquere.talkwithme.core.ai.langchain;

import fr.anthonyquere.talkwithme.core.CompletionOutput;
import fr.anthonyquere.talkwithme.core.CompletionProvider;
import fr.anthonyquere.talkwithme.core.ai.langchain.services.TalkWithCompanionLangchainService;
import fr.anthonyquere.talkwithme.core.domains.Companion;
import fr.anthonyquere.talkwithme.core.events.NewAIMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("langchain")
@RequiredArgsConstructor
public class LangchainIACompletionProvider implements CompletionProvider {

  private final TalkWithCompanionLangchainService talkWithCompanion;

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public CompletionOutput answerMessage(Companion companion, String userId, String question) {

    var response = talkWithCompanion.chat(companion, companion.getBackground(), question, userId);

    // Publish event to build summary
    applicationEventPublisher.publishEvent(new NewAIMessageEvent(this, response, companion));

    return new CompletionOutput(response);
  }
}
