package fr.anthonyquere.talkwithme.core.ai.langchain;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import fr.anthonyquere.talkwithme.core.ai.langchain.services.Summary;
import fr.anthonyquere.talkwithme.core.ai.langchain.services.TalkWithCompanion;
import fr.anthonyquere.talkwithme.core.domains.Companion;
import fr.anthonyquere.talkwithme.core.crud.message.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanionAiService {

  @Bean
  public TalkWithCompanion buildAiCompanionService(
    ChatLanguageModel model,
    MessageRepository messageRepository

  ) {
    return AiServices.builder(TalkWithCompanion.class)
      .chatLanguageModel(model)
      .chatMemoryProvider(companion -> new CompanionChatMemory((Companion) companion, messageRepository))
      .build();
  }

  @Bean
  public Summary buildAiSummaryService(
    ChatLanguageModel model
  ) {
    return AiServices.builder(Summary.class)
      .chatLanguageModel(model)
      .build();
  }
}
