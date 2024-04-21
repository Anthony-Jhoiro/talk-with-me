package fr.anthonyquere.talkwithme.core.adapters.ai.langchain.services;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.UserName;
import dev.langchain4j.service.V;
import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionConversation;

public interface TalkWithCompanionLangchainService {

  @SystemMessage("""
    Répond comme un personnage non-joueur dans un monde minecraft. L'utilisateur avec qui tu parles est un joueur.
    Ne mensionne aucune information concernant ta nature d'intelligence artificielle
    Répond avec un maximum de 50 mots et soit courtois.

    Ton nom: {{name}}
    Ton genre: {{gender}}
    Informations sur ton personnage:
    {{background}}
    """)
  String chat(
    @MemoryId CompanionConversation.Id conversationId,
    @V("name") String name,
    @V("gender") String gender,
    @V("background") String background,
    @UserMessage String message,
    @UserName String username
  );
}
