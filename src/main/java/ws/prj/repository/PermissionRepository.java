package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.prj.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
