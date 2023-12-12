package app.project.microblogging.service.dto;

import java.time.LocalDate;
import java.util.List;

public class ApplicationUserDTO {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate fechaNacimiento;
    private Long telefono;
    private List<String> authorities;

    public ApplicationUserDTO() {}

    public ApplicationUserDTO(
        Long id,
        String login,
        String firstName,
        String lastName,
        String email,
        LocalDate fechaNacimiento,
        Long telefono,
        List<String> authorities
    ) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Long getTelefono() {
        return telefono;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
