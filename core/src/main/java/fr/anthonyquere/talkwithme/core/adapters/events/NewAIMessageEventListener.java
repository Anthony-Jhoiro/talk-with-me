package fr.anthonyquere.talkwithme.core.adapters.events;

import fr.anthonyquere.talkwithme.core.adapters.ai.langchain.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewAIMessageEventListener implements ApplicationListener<NewAIMessageEvent> {
  private final SummaryService summaryService;

  @Override
  public void onApplicationEvent(NewAIMessageEvent event) {
    //summaryService.summarizeChat(event.getCompanion());
  }
}
