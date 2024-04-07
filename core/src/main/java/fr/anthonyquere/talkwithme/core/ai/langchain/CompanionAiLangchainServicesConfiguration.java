package fr.anthonyquere.talkwithme.core.ai.langchain;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import fr.anthonyquere.talkwithme.core.ai.langchain.services.SummaryLangchainService;
import fr.anthonyquere.talkwithme.core.ai.langchain.services.TalkWithCompanionLangchainService;
import fr.anthonyquere.talkwithme.core.domains.Companion;
import fr.anthonyquere.talkwithme.core.data.jpa.conversations.MessageJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanionAiLangchainServicesConfiguration {

  @Bean
  public TalkWithCompanionLangchainService buildAiCompanionService(
    ChatLanguageModel model,
    MessageJpaRepository messageRepository

  ) {
    return AiServices.builder(TalkWithCompanionLangchainService.class)
      .chatLanguageModel(model)
      .chatMemoryProvider(companion -> new CompanionChatMemory((Companion) companion, messageRepository))
      .build();
  }

  @Bean
  public SummaryLangchainService buildAiSummaryService(
    ChatLanguageModel model
  ) {
    return AiServices.builder(SummaryLangchainService.class)
      .chatLanguageModel(model)
      .build();
  }
}
