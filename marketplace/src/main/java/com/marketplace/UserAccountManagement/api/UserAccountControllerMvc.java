package com.marketplace.UserAccountManagement.api;

import com.marketplace.UserAccountManagement.domain.Role;
import com.marketplace.UserAccountManagement.domain.RoleService;
import com.marketplace.UserAccountManagement.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserAccountControllerMvc {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper mapper;

    @GetMapping("/register")
    public String register(Model model) {
        UserAccountCreationDTO userDto = new UserAccountCreationDTO("", "", "", "");
        model.addAttribute("accountDto", userDto);
        return "userTemplate/register";
    }

//    @PostMapping("/register")
//    public RedirectView createAccountAndRedirectToLogin(RedirectAttributes redirectAttributes) {
//
//    }

    @GetMapping("/login")
    public String login() {
        return "userTemplate/login";
    }
}
