package template_spring_boot.template.adapters.database.postgresql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import template_spring_boot.template.adapters.database.postgresql.entities.CepAudit;

@Repository
public interface CepAuditRepository extends JpaRepository<CepAudit, Long> {
}

