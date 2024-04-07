package fr.anthonyquere.talkwithme.core.adapters.ai.langchain;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import fr.anthonyquere.talkwithme.core.hexa.CompanionConversation;
import fr.anthonyquere.talkwithme.core.hexa.CompanionConversationStorage;
import fr.anthonyquere.talkwithme.core.adapters.ai.langchain.services.SummaryLangchainService;
import fr.anthonyquere.talkwithme.core.adapters.ai.langchain.services.TalkWithCompanionLangchainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanionAiLangchainServicesConfiguration {

  @Bean
  public TalkWithCompanionLangchainService buildAiCompanionService(
    ChatLanguageModel model,
    CompanionConversationStorage companionConversationStorage

  ) {
    return AiServices.builder(TalkWithCompanionLangchainService.class)
      .chatLanguageModel(model)
      .chatMemoryProvider(conversationId -> new CompanionChatMemory((CompanionConversation.Id) conversationId, companionConversationStorage))
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
