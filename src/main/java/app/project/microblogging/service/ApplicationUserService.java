package app.project.microblogging.service;

import app.project.microblogging.domain.ApplicationUser;
import app.project.microblogging.domain.Authority;
import app.project.microblogging.repository.ApplicationUserRepository;
import app.project.microblogging.repository.AuthorityRepository;
import app.project.microblogging.repository.UserRepository;
import app.project.microblogging.service.dto.ApplicationUserDTO;
import app.project.microblogging.service.dto.UserDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, UserRepository userRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.userRepository = userRepository;
    }

    public ApplicationUserDTO getApplicationUserByIdService(Long id) {
        ApplicationUserDTO applicationUserDTO = new ApplicationUserDTO();

        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findApplicationUserById(id);

        applicationUserOptional.ifPresent(applicationUser -> {
            applicationUserDTO.setId(applicationUser.getId());
            applicationUserDTO.setFirstName(applicationUser.getUser().getFirstName());
            applicationUserDTO.setLastName(applicationUser.getUser().getLastName());
            applicationUserDTO.setEmail(applicationUser.getUser().getEmail());
            applicationUserDTO.setLogin(applicationUser.getUser().getLogin());
            applicationUserDTO.setFechaNacimiento(applicationUser.getFechaNacimiento());
            applicationUserDTO.setTelefono(applicationUser.getTelefono());

            List<String> authorityNames = applicationUser
                .getUser()
                .getAuthorities()
                .stream()
                .map(Authority::getName)
                .collect(Collectors.toList());

            applicationUserDTO.setAuthorities(authorityNames);
        });

        return applicationUserDTO;
    }

    public ApplicationUserDTO getApplicationUserByLoginUserService(String login) {
        ApplicationUserDTO applicationUserDTO = new ApplicationUserDTO();

        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findApplicationUserByUserLogin(login);

        applicationUserOptional.ifPresent(applicationUser -> {
            applicationUserDTO.setId(applicationUser.getId());
            applicationUserDTO.setFirstName(applicationUser.getUser().getFirstName());
            applicationUserDTO.setLastName(applicationUser.getUser().getLastName());
            applicationUserDTO.setEmail(applicationUser.getUser().getEmail());
            applicationUserDTO.setLogin(applicationUser.getUser().getLogin());
            applicationUserDTO.setFechaNacimiento(applicationUser.getFechaNacimiento());
            applicationUserDTO.setTelefono(applicationUser.getTelefono());

            List<String> authorityNames = applicationUser
                .getUser()
                .getAuthorities()
                .stream()
                .map(Authority::getName)
                .collect(Collectors.toList());

            applicationUserDTO.setAuthorities(authorityNames);
        });

        return applicationUserDTO;
    }

    //ENDPOINTS CONTACTOS
    public Set<UserDTO> getApplicationUserContactsService(Long userId) {
        Optional<ApplicationUser> applicationUser = applicationUserRepository.findWithContactosById(userId);
        Set<UserDTO> userDTOSet = new HashSet<>();
        applicationUser.ifPresent(user ->
            userDTOSet.addAll(
                user
                    .getContactos()
                    .stream()
                    .map(contacto -> new UserDTO(contacto.getId(), contacto.getUser().getLogin()))
                    .collect(Collectors.toSet())
            )
        );
        return userDTOSet;
    }

    public void addContactoService(Long userId, Long contactoId) {
        applicationUserRepository
            .findWithContactosById(userId)
            .ifPresent(applicationUser -> {
                applicationUserRepository
                    .findApplicationUserById(contactoId)
                    .ifPresent(contacto -> {
                        applicationUser.getContactos().add(contacto);
                        applicationUserRepository.save(applicationUser);
                    });
            });
    }

    public void deleteContactoService(Long userId, Long contactoId) {
        applicationUserRepository
            .findWithContactosById(userId)
            .ifPresent(applicationUser -> {
                applicationUserRepository
                    .findApplicationUserById(contactoId)
                    .ifPresent(contacto -> {
                        applicationUser.getContactos().remove(contacto);
                        applicationUserRepository.save(applicationUser);
                    });
            });
    }

    //ENDPOINTS SEGUIDOS

    public Set<UserDTO> getApplicationUserSeguidosService(Long userId) {
        Optional<ApplicationUser> applicationUser = applicationUserRepository.findWithSeguidosById(userId);
        Set<UserDTO> userDTOSet = new HashSet<>();
        applicationUser.ifPresent(user ->
            userDTOSet.addAll(
                user
                    .getSeguidos()
                    .stream()
                    .map(seguido -> new UserDTO(seguido.getId(), seguido.getUser().getLogin()))
                    .collect(Collectors.toSet())
            )
        );
        return userDTOSet;
    }

    public void addSeguidoService(Long userId, Long seguidoId) {
        applicationUserRepository
            .findWithSeguidosById(userId)
            .ifPresent(applicationUser -> {
                applicationUserRepository
                    .findApplicationUserById(seguidoId)
                    .ifPresent(contacto -> {
                        applicationUser.getSeguidos().add(contacto);
                        applicationUserRepository.save(applicationUser);
                    });
            });
    }

    public void deleteSeguidoService(Long userId, Long seguidoId) {
        applicationUserRepository
            .findWithSeguidosById(userId)
            .ifPresent(applicationUser -> {
                applicationUserRepository
                    .findApplicationUserById(seguidoId)
                    .ifPresent(contacto -> {
                        applicationUser.getSeguidos().remove(contacto);
                        applicationUserRepository.save(applicationUser);
                    });
            });
    }
}
