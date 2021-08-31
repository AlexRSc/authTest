package de.neuefische.rem_213_github.backend.service;

import de.neuefische.rem_213_github.backend.config.UserServiceConfigProperties;
import de.neuefische.rem_213_github.backend.model.RepoEntity;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.repo.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.StringUtils.hasText;

@Service
@Getter
@Setter
public class UserService {

    private final PasswordService passwordService;
    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserServiceConfigProperties userServiceConfigProperties;

    @Autowired
    public UserService(UserRepository userRepository, UserServiceConfigProperties userServiceConfigProperties, PasswordService passwordService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userServiceConfigProperties = userServiceConfigProperties;
        this.passwordService = passwordService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity create(UserEntity userEntity) {
        String name = userEntity.getName();
        if (!hasText(name)) {
            throw new IllegalArgumentException("Name must not be blank to create user");
        }
        checkUserNameExists(name);

        String password = passwordService.getNewPassword();
        String hashedPassword = passwordEncoder.encode(password);

        UserEntity savedUser = userRepository.save(userEntity.toBuilder()
                .password(hashedPassword)
                .build());
        return savedUser.toBuilder().password(password).build();
    }

    private void checkUserNameExists(String name) {
        Optional<UserEntity> existingUser = find(name);
        if (existingUser.isPresent()) {
            throw new EntityExistsException(String.format(
                    "User with name=%s already exists", name));
        }
    }

    public UserEntity update(Long id, UserEntity updateEntity) {
        Optional<UserEntity> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new EntityNotFoundException(String.format(
                    "User with id=%d not found, unable to update", id));
        }
        UserEntity existingUserEntity = existingUserOpt.get();

        String name = updateEntity.getName();
        if (name != null && !name.equals(existingUserEntity.getName())) {
            checkUserNameExists(name);
        }

        String avatarUrl = updateEntity.getAvatarUrl();
        if (avatarUrl != null) {
            existingUserEntity.setAvatarUrl(avatarUrl);
        }

        Set<RepoEntity> repos = updateEntity.getRepos();
        if (repos != null) {
            existingUserEntity.setRepos(repos);
        }
        return userRepository.save(existingUserEntity);
    }

    public Optional<UserEntity> find(String name) {
        return userRepository.findByName(name);
    }

    public Optional<UserEntity> delete(String name) {
        Optional<UserEntity> userEntityOptional = find(name);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userRepository.delete(userEntity);
        }
        return userEntityOptional;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity updatePassword(String name, String password) {
        UserEntity userEntity = find(name).orElseThrow(()
                -> new IllegalArgumentException(String.format("User name=%s not found", name)));

        String hashedPassword = passwordEncoder.encode(password);
        userEntity.setPassword(hashedPassword);
        return userRepository.save(userEntity);
    }
}
