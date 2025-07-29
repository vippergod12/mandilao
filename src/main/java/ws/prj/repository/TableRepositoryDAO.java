package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.prj.entity.Tables;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TableRepositoryDAO extends JpaRepository<Tables,String> {
    Optional<Tables> findById(UUID id);
}
