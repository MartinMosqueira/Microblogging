package app.project.microblogging.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "telefono")
    private Long telefono;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<MensajeMuro> mensajes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_application_user__contacto",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "contacto_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "mensajes", "contactos", "seguidos", "userCS", "userS" }, allowSetters = true)
    private Set<ApplicationUser> contactos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_application_user__seguido",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "seguido_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "mensajes", "contactos", "seguidos", "userCS", "userS" }, allowSetters = true)
    private Set<ApplicationUser> seguidos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "contactos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "mensajes", "contactos", "seguidos", "userCS", "userS" }, allowSetters = true)
    private Set<ApplicationUser> userCS = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "seguidos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "mensajes", "contactos", "seguidos", "userCS", "userS" }, allowSetters = true)
    private Set<ApplicationUser> userS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public ApplicationUser fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getTelefono() {
        return this.telefono;
    }

    public ApplicationUser telefono(Long telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ApplicationUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<MensajeMuro> getMensajes() {
        return this.mensajes;
    }

    public void setMensajes(Set<MensajeMuro> mensajeMuros) {
        if (this.mensajes != null) {
            this.mensajes.forEach(i -> i.setUser(null));
        }
        if (mensajeMuros != null) {
            mensajeMuros.forEach(i -> i.setUser(this));
        }
        this.mensajes = mensajeMuros;
    }

    public ApplicationUser mensajes(Set<MensajeMuro> mensajeMuros) {
        this.setMensajes(mensajeMuros);
        return this;
    }

    public ApplicationUser addMensaje(MensajeMuro mensajeMuro) {
        this.mensajes.add(mensajeMuro);
        mensajeMuro.setUser(this);
        return this;
    }

    public ApplicationUser removeMensaje(MensajeMuro mensajeMuro) {
        this.mensajes.remove(mensajeMuro);
        mensajeMuro.setUser(null);
        return this;
    }

    public Set<ApplicationUser> getContactos() {
        return this.contactos;
    }

    public void setContactos(Set<ApplicationUser> applicationUsers) {
        this.contactos = applicationUsers;
    }

    public ApplicationUser contactos(Set<ApplicationUser> applicationUsers) {
        this.setContactos(applicationUsers);
        return this;
    }

    public ApplicationUser addContacto(ApplicationUser applicationUser) {
        this.contactos.add(applicationUser);
        return this;
    }

    public ApplicationUser removeContacto(ApplicationUser applicationUser) {
        this.contactos.remove(applicationUser);
        return this;
    }

    public Set<ApplicationUser> getSeguidos() {
        return this.seguidos;
    }

    public void setSeguidos(Set<ApplicationUser> applicationUsers) {
        this.seguidos = applicationUsers;
    }

    public ApplicationUser seguidos(Set<ApplicationUser> applicationUsers) {
        this.setSeguidos(applicationUsers);
        return this;
    }

    public ApplicationUser addSeguido(ApplicationUser applicationUser) {
        this.seguidos.add(applicationUser);
        return this;
    }

    public ApplicationUser removeSeguido(ApplicationUser applicationUser) {
        this.seguidos.remove(applicationUser);
        return this;
    }

    public Set<ApplicationUser> getUserCS() {
        return this.userCS;
    }

    public void setUserCS(Set<ApplicationUser> applicationUsers) {
        if (this.userCS != null) {
            this.userCS.forEach(i -> i.removeContacto(this));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.addContacto(this));
        }
        this.userCS = applicationUsers;
    }

    public ApplicationUser userCS(Set<ApplicationUser> applicationUsers) {
        this.setUserCS(applicationUsers);
        return this;
    }

    public ApplicationUser addUserC(ApplicationUser applicationUser) {
        this.userCS.add(applicationUser);
        applicationUser.getContactos().add(this);
        return this;
    }

    public ApplicationUser removeUserC(ApplicationUser applicationUser) {
        this.userCS.remove(applicationUser);
        applicationUser.getContactos().remove(this);
        return this;
    }

    public Set<ApplicationUser> getUserS() {
        return this.userS;
    }

    public void setUserS(Set<ApplicationUser> applicationUsers) {
        if (this.userS != null) {
            this.userS.forEach(i -> i.removeSeguido(this));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.addSeguido(this));
        }
        this.userS = applicationUsers;
    }

    public ApplicationUser userS(Set<ApplicationUser> applicationUsers) {
        this.setUserS(applicationUsers);
        return this;
    }

    public ApplicationUser addUserS(ApplicationUser applicationUser) {
        this.userS.add(applicationUser);
        applicationUser.getSeguidos().add(this);
        return this;
    }

    public ApplicationUser removeUserS(ApplicationUser applicationUser) {
        this.userS.remove(applicationUser);
        applicationUser.getSeguidos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", telefono=" + getTelefono() +
            "}";
    }
}
