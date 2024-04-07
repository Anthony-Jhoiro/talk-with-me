package fr.anthonyquere.talkwithme.core.adapters.data.jpa.conversations;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageJpaRepository extends CrudRepository<MessageEntity, UUID> {
  List<MessageEntity> getMessagesByCompanionIdOrderByCreatedAtDesc(String companionId, Pageable pageable);

  List<MessageEntity> getMessagesByCompanionIdAndUserIdOrderByCreatedAtDesc(String companionId, String userId, Pageable pageable);

  void deleteByCompanionIdAndUserId(String companionId, String userId);

  Optional<MessageEntity> findByCompanionIdAndUserIdAndType(String companionId, String userId, String type);
}
