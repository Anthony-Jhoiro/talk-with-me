package fr.anthonyquere.talkwithme.core.events;

import fr.anthonyquere.talkwithme.core.domains.Companion;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class NewAIMessageEvent extends ApplicationEvent {
  private final String message;
  private final Companion companion;

  public NewAIMessageEvent(Object source, String message, Companion companion) {
    super(source);
    this.message = message;
    this.companion = companion;
  }

}
