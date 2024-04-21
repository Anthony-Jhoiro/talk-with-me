package fr.anthonyquere.talkwithme.core.adapters.ai.langchain;

import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionConversation;
import fr.anthonyquere.talkwithme.core.hexa.CompletionOutput;
import fr.anthonyquere.talkwithme.core.hexa.CompletionProvider;
import fr.anthonyquere.talkwithme.core.adapters.ai.langchain.services.TalkWithCompanionLangchainService;
import fr.anthonyquere.talkwithme.core.hexa.domains.Companion;
import fr.anthonyquere.talkwithme.core.adapters.events.NewAIMessageEvent;
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

    var response = talkWithCompanion.chat(
      new CompanionConversation.Id(
        companion.id(),
        userId
      ),
      companion.name(),
      companion.gender(),
      companion.background(),
      question,
      userId
    );

    // Publish event to build summary
    applicationEventPublisher.publishEvent(new NewAIMessageEvent(this, response, companion));

    return new CompletionOutput(response);
  }
}
