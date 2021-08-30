package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.NewPassword;
import de.neuefische.rem_213_github.backend.api.User;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.service.PasswordService;
import de.neuefische.rem_213_github.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static de.neuefische.rem_213_github.backend.controller.UserController.USER_CONTROLLER_TAG;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = USER_CONTROLLER_TAG, description = "Provides CRUD operations for an User")
@Api(
        tags = USER_CONTROLLER_TAG
)
@CrossOrigin
@RestController
@RequestMapping("/user")
@Getter
@Setter
public class UserController {

    public static final String USER_CONTROLLER_TAG = "User";

    private UserService userService;
    private PasswordService passwordService;

    @Autowired
    public UserController(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_BAD_REQUEST, message = "Unable to create User with blank name"),
            @ApiResponse(code = SC_CONFLICT, message = "Unable to create User, user already exists")
    })
    public ResponseEntity<User> create(@AuthenticationPrincipal UserEntity authUser, @RequestBody User user) {
        if(!authUser.getRole().equals("admin")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            UserEntity userEntity = map(user);

            UserEntity createdUserEntity = userService.create(userEntity);
            User createdUser = map(createdUserEntity);
            createdUser.setPassword(createdUserEntity.getPassword());
            return ok(createdUser);

        } catch (IllegalArgumentException e) {
            return badRequest().build();
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("password")
    public ResponseEntity<User> updatePassword(@AuthenticationPrincipal UserEntity authUser, @RequestBody NewPassword newPassword){
        try{
            UserEntity updatedUserEntity = userService.updatePassword(authUser.getName(), newPassword.getPassword());
            User updatedUser = map(updatedUserEntity);
            updatedUser.setPassword(newPassword.getPassword());
            return ok(updatedUser);
        }catch(IllegalArgumentException e){
            return badRequest().build();
        }
    }

    @PutMapping("{name}/reset-password")
    public ResponseEntity<User> resetPassword(@AuthenticationPrincipal UserEntity authUser, @PathVariable String name){
        if(!authUser.getRole().equals("admin")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<UserEntity> optionalUserEntity = userService.find(name);
        if(optionalUserEntity.isEmpty()){
            return badRequest().build();
        }

        String password = passwordService.getNewPassword();

        UserEntity updatedUserEntity = userService.updatePassword(name, password);

        User updatedUser = map(updatedUserEntity);
        updatedUser.setPassword(password);

        return ok(updatedUser);
    }

    @GetMapping(value = "{name}", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_NOT_FOUND, message = "User not found")
    })
    public ResponseEntity<User> find(@PathVariable String name) {
        Optional<UserEntity> userEntityOptional = userService.find(name);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            User user = map(userEntity);
            return ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_NO_CONTENT, message = "No users found")
    })
    public ResponseEntity<List<User>> findAll() {
        List<UserEntity> userEntities = userService.findAll();
        if (userEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<User> users = map(userEntities);
        return ok(users);
    }

    @DeleteMapping(value = "{name}", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_NOT_FOUND, message = "User not found")
    })
    public ResponseEntity<User> delete(@PathVariable String name) {
        Optional<UserEntity> userEntityOptional = userService.delete(name);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            User user = map(userEntity);
            return ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    private User map(UserEntity userEntity) {
        return User.builder()
                .name(userEntity.getName())
                .avatar(userEntity.getAvatarUrl())
                .role(userEntity.getRole())
                .build();
    }

    private UserEntity map(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setAvatarUrl(user.getAvatar());
        userEntity.setRole(user.getRole());
        return userEntity;
    }

    private List<User> map(List<UserEntity> userEntities) {
        List<User> users = new LinkedList<>();
        for (UserEntity userEntity : userEntities) {
            User user = map(userEntity);
            users.add(user);
        }
        return users;
    }
}
