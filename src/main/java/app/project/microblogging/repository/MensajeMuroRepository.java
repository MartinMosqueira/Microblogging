package app.project.microblogging.repository;

import app.project.microblogging.domain.MensajeMuro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MensajeMuro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MensajeMuroRepository extends JpaRepository<MensajeMuro, Long> {}
