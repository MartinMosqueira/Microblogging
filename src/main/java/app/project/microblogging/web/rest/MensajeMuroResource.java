package app.project.microblogging.web.rest;

import app.project.microblogging.domain.MensajeMuro;
import app.project.microblogging.repository.MensajeMuroRepository;
import app.project.microblogging.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link app.project.microblogging.domain.MensajeMuro}.
 */
@RestController
@RequestMapping("/api/mensaje-muros")
@Transactional
public class MensajeMuroResource {

    private final Logger log = LoggerFactory.getLogger(MensajeMuroResource.class);

    private static final String ENTITY_NAME = "mensajeMuro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MensajeMuroRepository mensajeMuroRepository;

    public MensajeMuroResource(MensajeMuroRepository mensajeMuroRepository) {
        this.mensajeMuroRepository = mensajeMuroRepository;
    }

    /**
     * {@code POST  /mensaje-muros} : Create a new mensajeMuro.
     *
     * @param mensajeMuro the mensajeMuro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mensajeMuro, or with status {@code 400 (Bad Request)} if the mensajeMuro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MensajeMuro> createMensajeMuro(@RequestBody MensajeMuro mensajeMuro) throws URISyntaxException {
        log.debug("REST request to save MensajeMuro : {}", mensajeMuro);
        if (mensajeMuro.getId() != null) {
            throw new BadRequestAlertException("A new mensajeMuro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MensajeMuro result = mensajeMuroRepository.save(mensajeMuro);
        return ResponseEntity
            .created(new URI("/api/mensaje-muros/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    (result.getId() != null) ? result.getId().toString() : "Id is null"
                )
            )
            .body(result);
    }

    /**
     * {@code PUT  /mensaje-muros/:id} : Updates an existing mensajeMuro.
     *
     * @param id the id of the mensajeMuro to save.
     * @param mensajeMuro the mensajeMuro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mensajeMuro,
     * or with status {@code 400 (Bad Request)} if the mensajeMuro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mensajeMuro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MensajeMuro> updateMensajeMuro(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MensajeMuro mensajeMuro
    ) throws URISyntaxException {
        log.debug("REST request to update MensajeMuro : {}, {}", id, mensajeMuro);
        if (mensajeMuro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mensajeMuro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mensajeMuroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MensajeMuro result = mensajeMuroRepository.save(mensajeMuro);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mensajeMuro.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mensaje-muros/:id} : Partial updates given fields of an existing mensajeMuro, field will ignore if it is null
     *
     * @param id the id of the mensajeMuro to save.
     * @param mensajeMuro the mensajeMuro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mensajeMuro,
     * or with status {@code 400 (Bad Request)} if the mensajeMuro is not valid,
     * or with status {@code 404 (Not Found)} if the mensajeMuro is not found,
     * or with status {@code 500 (Internal Server Error)} if the mensajeMuro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MensajeMuro> partialUpdateMensajeMuro(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MensajeMuro mensajeMuro
    ) throws URISyntaxException {
        log.debug("REST request to partial update MensajeMuro partially : {}, {}", id, mensajeMuro);
        if (mensajeMuro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mensajeMuro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mensajeMuroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MensajeMuro> result = mensajeMuroRepository
            .findById(mensajeMuro.getId())
            .map(existingMensajeMuro -> {
                if (mensajeMuro.getMensaje() != null) {
                    existingMensajeMuro.setMensaje(mensajeMuro.getMensaje());
                }
                if (mensajeMuro.getFecha() != null) {
                    existingMensajeMuro.setFecha(mensajeMuro.getFecha());
                }
                if (mensajeMuro.getTags() != null) {
                    existingMensajeMuro.setTags(mensajeMuro.getTags());
                }

                return existingMensajeMuro;
            })
            .map(mensajeMuroRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mensajeMuro.getId().toString())
        );
    }

    /**
     * {@code GET  /mensaje-muros} : get all the mensajeMuros.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mensajeMuros in body.
     */
    @GetMapping("")
    public List<MensajeMuro> getAllMensajeMuros() {
        log.debug("REST request to get all MensajeMuros");
        return mensajeMuroRepository.findAll();
    }

    /**
     * {@code GET  /mensaje-muros/:id} : get the "id" mensajeMuro.
     *
     * @param id the id of the mensajeMuro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mensajeMuro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MensajeMuro> getMensajeMuro(@PathVariable Long id) {
        log.debug("REST request to get MensajeMuro : {}", id);
        Optional<MensajeMuro> mensajeMuro = mensajeMuroRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mensajeMuro);
    }

    /**
     * {@code DELETE  /mensaje-muros/:id} : delete the "id" mensajeMuro.
     *
     * @param id the id of the mensajeMuro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMensajeMuro(@PathVariable Long id) {
        log.debug("REST request to delete MensajeMuro : {}", id);
        mensajeMuroRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
