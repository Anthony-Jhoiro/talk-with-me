package fr.anthonyquere.talkwithme.core.adapters.data.jpa.conversations;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageJpaRepository extends CrudRepository<Message, UUID> {
  List<Message> getMessagesByCompanionIdOrderByCreatedAtDesc(String companionId, Pageable pageable);

  List<Message> getMessagesByCompanionIdAndUserIdOrderByCreatedAtDesc(String companionId, String userId, Pageable pageable);

  void deleteByCompanionIdAndUserId(String companionId, String userId);

  Optional<Message> findByCompanionIdAndUserIdAndType(String companionId, String userId, String type);
}
