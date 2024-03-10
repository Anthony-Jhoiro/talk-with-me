package fr.anthonyquere.talkwithme.domains;

import feign.Param;
import feign.RequestLine;

public interface CoreAPI {

  @RequestLine("POST /companions/{companionId}/talk")
  @feign.Headers("Content-Type: application/json")
  Message talkWithCompanion(TalkRequestPayload payload, @Param("companionId") String companionId);

  record TalkRequestPayload(String question) {

  }
}
