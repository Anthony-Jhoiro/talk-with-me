package fr.anthonyquere.talkwithme.core.ai.langchain.services;

import dev.langchain4j.service.*;
import fr.anthonyquere.talkwithme.core.crud.companions.Companion;

public interface TalkWithCompanion {

  @SystemMessage("From this context: {{context}}\nAnswer at best but keep it short and simple")
  String chat(@MemoryId Companion companion, @V("context") String context, @UserMessage String message, @UserName String username);
}
