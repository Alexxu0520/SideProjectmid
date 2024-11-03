package org.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService = new UserService();

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO user) {
        return userService.registerUser(user.getEmail(), user.getNickname(), user.getPassword());
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO user) {
        return userService.loginUser(user.getEmail(), user.getPassword());
    }

    @PostMapping("/password/change")
    public String changePassword(@RequestBody UserPasswordChangeDTO passwordChangeDTO) {
        return userService.changePassword(
                passwordChangeDTO.getEmail(),
                passwordChangeDTO.getCurrentPassword(),
                passwordChangeDTO.getNewPassword()
        );
    }
}
