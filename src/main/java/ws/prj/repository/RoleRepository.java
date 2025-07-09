package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.prj.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
