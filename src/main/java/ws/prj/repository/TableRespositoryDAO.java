package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ws.prj.entity.Tables;

public interface TableRespositoryDAO extends JpaRepository<Tables, Long> {

}
