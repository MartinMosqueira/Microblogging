package app.project.microblogging.repository;

import app.project.microblogging.domain.ApplicationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ApplicationUserRepositoryWithBagRelationships {
    Optional<ApplicationUser> fetchBagRelationships(Optional<ApplicationUser> applicationUser);

    List<ApplicationUser> fetchBagRelationships(List<ApplicationUser> applicationUsers);

    Page<ApplicationUser> fetchBagRelationships(Page<ApplicationUser> applicationUsers);
}
