package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ws.prj.entity.Tables;

import java.util.Optional;
import java.util.UUID;

public interface TableRespositoryDAO extends JpaRepository<Tables, Long> {
    Optional<Tables> findById(Long id);
}
