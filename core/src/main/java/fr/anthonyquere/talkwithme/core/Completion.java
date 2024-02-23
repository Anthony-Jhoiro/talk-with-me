package fr.anthonyquere.talkwithme.core;


import fr.anthonyquere.talkwithme.core.crud.companions.Companion;

public interface Completion {
  CompletionOutput answerMessage(Companion companion, String question) throws Exception;
}
