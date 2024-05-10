package app.project.microblogging.web.rest;

import app.project.microblogging.domain.ApplicationUser;
import app.project.microblogging.repository.ApplicationUserRepository;
import app.project.microblogging.service.ApplicationUserService;
import app.project.microblogging.service.dto.ApplicationUserDTO;
import app.project.microblogging.service.dto.UserDTO;
import app.project.microblogging.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link app.project.microblogging.domain.ApplicationUser}.
 */
@RestController
@RequestMapping("/api/application-users")
@Transactional
public class ApplicationUserResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserResource.class);

    private static final String ENTITY_NAME = "applicationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserService applicationUserService;

    public ApplicationUserResource(ApplicationUserRepository applicationUserRepository, ApplicationUserService applicationUserService) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserService = applicationUserService;
    }

    /**
     * {@code POST  /application-users} : Create a new applicationUser.
     *
     * @param applicationUser the applicationUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationUser, or with status {@code 400 (Bad Request)} if the applicationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationUser> createApplicationUser(@RequestBody ApplicationUser applicationUser) throws URISyntaxException {
        log.debug("REST request to save ApplicationUser : {}", applicationUser);
        if (applicationUser.getId() != null) {
            throw new BadRequestAlertException("A new applicationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationUser result = applicationUserRepository.save(applicationUser);
        return ResponseEntity
            .created(new URI("/api/application-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-users/:id} : Updates an existing applicationUser.
     *
     * @param id the id of the applicationUser to save.
     * @param applicationUser the applicationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUser,
     * or with status {@code 400 (Bad Request)} if the applicationUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUser> updateApplicationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUser applicationUser
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationUser : {}, {}", id, applicationUser);
        if (applicationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApplicationUser result = applicationUserRepository.save(applicationUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-users/:id} : Partial updates given fields of an existing applicationUser, field will ignore if it is null
     *
     * @param id the id of the applicationUser to save.
     * @param applicationUser the applicationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUser,
     * or with status {@code 400 (Bad Request)} if the applicationUser is not valid,
     * or with status {@code 404 (Not Found)} if the applicationUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationUser> partialUpdateApplicationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUser applicationUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationUser partially : {}, {}", id, applicationUser);
        if (applicationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationUser> result = applicationUserRepository
            .findById(applicationUser.getId())
            .map(existingApplicationUser -> {
                if (applicationUser.getFechaNacimiento() != null) {
                    existingApplicationUser.setFechaNacimiento(applicationUser.getFechaNacimiento());
                }
                if (applicationUser.getTelefono() != null) {
                    existingApplicationUser.setTelefono(applicationUser.getTelefono());
                }

                return existingApplicationUser;
            })
            .map(applicationUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationUser.getId().toString())
        );
    }

    /**
     * {@code GET  /application-users} : get all the applicationUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationUsers in body.
     */
    @GetMapping("")
    public List<ApplicationUser> getAllApplicationUsers(@RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all ApplicationUsers");
        if (eagerload) {
            return applicationUserRepository.findAllWithEagerRelationships();
        } else {
            return applicationUserRepository.findAll();
        }
    }

    /**
     * {@code GET  /application-users/:id} : get the "id" applicationUser.
     *
     * @param id the id of the applicationUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUser> getApplicationUser(@PathVariable Long id) {
        log.debug("REST request to get ApplicationUser : {}", id);
        Optional<ApplicationUser> applicationUser = applicationUserRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(applicationUser);
    }

    /**
     * {@code DELETE  /application-users/:id} : delete the "id" applicationUser.
     *
     * @param id the id of the applicationUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationUser(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApplicationUserDTO> getApplicationUserByIdController(@PathVariable Long id) {
        log.debug("REST request to get ApplicationUser : {}", id);
        ApplicationUserDTO applicationUserDTO = applicationUserService.getApplicationUserByIdService(id);
        return ResponseEntity.ok().body(applicationUserDTO);
    }

    @GetMapping("/user/login/{login}")
    public ResponseEntity<ApplicationUserDTO> getApplicationUserByLoginUserController(@PathVariable String login) {
        log.debug("REST request to get ApplicationUser : {}", login);
        ApplicationUserDTO applicationUserDTO = applicationUserService.getApplicationUserByLoginUserService(login);
        return ResponseEntity.ok().body(applicationUserDTO);
    }

    //ENDPOINTS CONTACTOS

    @GetMapping("/contacts/{userId}")
    public ResponseEntity<Set<UserDTO>> getApplicationUserContactsController(@PathVariable Long userId) {
        log.debug("REST request to get ApplicationUser : {}", userId);
        Set<UserDTO> userDTOSet = applicationUserService.getApplicationUserContactsService(userId);
        return ResponseEntity.ok().body(userDTOSet);
    }

    @PostMapping("/contacts/{userId}/{contactoId}")
    public ResponseEntity<Void> addContactoController(@PathVariable Long userId, @PathVariable Long contactoId) {
        log.debug("REST request to get ApplicationUser : {}", userId);
        applicationUserService.addContactoService(userId, contactoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contacts/{userId}/{contactoId}")
    public ResponseEntity<Void> deleteContactoController(@PathVariable Long userId, @PathVariable Long contactoId) {
        log.debug("REST request to get ApplicationUser : {}", userId);
        applicationUserService.deleteContactoService(userId, contactoId);
        return ResponseEntity.ok().build();
    }

    //ENDPOINTS SEGUIDOS

    @GetMapping("/seguidos/{userId}")
    public ResponseEntity<Set<UserDTO>> getApplicationUserSeguidosController(@PathVariable Long userId) {
        log.debug("REST request to get ApplicationUser : {}", userId);
        Set<UserDTO> userDTOSet = applicationUserService.getApplicationUserSeguidosService(userId);
        return ResponseEntity.ok().body(userDTOSet);
    }

    @PostMapping("/seguidos/{userId}/{seguidoId}")
    public ResponseEntity<Void> addSeguidoController(@PathVariable Long userId, @PathVariable Long seguidoId) {
        log.debug("REST request to get ApplicationUser : {}", userId);
        applicationUserService.addSeguidoService(userId, seguidoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/seguidos/{userId}/{seguidoId}")
    public ResponseEntity<Void> deleteSeguidoController(@PathVariable Long userId, @PathVariable Long seguidoId) {
        log.debug("REST request to get ApplicationUser : {}", userId);
        applicationUserService.deleteSeguidoService(userId, seguidoId);
        return ResponseEntity.ok().build();
    }
}
