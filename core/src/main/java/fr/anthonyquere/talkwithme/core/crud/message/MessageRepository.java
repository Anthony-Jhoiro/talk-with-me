package fr.anthonyquere.talkwithme.core.crud.message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {
  List<Message> getMessagesByCompanion_IdOrderByCreatedAtDesc(String companionId, Pageable pageable);

  @Query("select m from Message m " +
    "where m.companion.id = :companionId " +
    "and m.type in ('USER', 'AI') " +
    "and (m.status is null or m.status != 'ARCHIVED') " +
    "order by m.createdAt asc"
  )
  List<Message> getLatestMassages(String companionId);

  @Query("select m.message from Message m " +
    "where m.companion.id = :companionId " +
    "and m.type = 'SUMMARY' " +
    "and (m.status is null or m.status != 'ARCHIVED') " +
    "order by m.createdAt desc " +
    "limit 1"
  )
  Optional<String> getLatestSummary(String companionId);

  Optional<Message> getFirstByCompanion_IdAndTypeOrderByCreatedAtDesc(String companionId, String type);

  void deleteByCompanionId(String companionId);
}
