package com.carService.controller;

import com.carService.model.User;
import com.carService.model.dto.UserDTO;
import com.carService.service.InvoiceServiceImpl;
import com.carService.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {


    UserServiceImpl userService;
    InvoiceServiceImpl invoiceService;

    @Autowired
    public HomeController(UserServiceImpl userService, InvoiceServiceImpl invoiceService) {

        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/")
    public String showHome() {

        return "redirect:/services";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam Optional<String> id) {
        if (id.isPresent() && id.get().equals("error")) model.addAttribute("error",true);
        model.addAttribute("loginRequest");
        return "user/login";
    }

    @GetMapping("/register")
    public String showRegister() {

        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        User userToRegister = userService.registerUser(user,false);
        if (userToRegister == null)
        {
            model.addAttribute("error",true);
            return "/register";
        }
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {

        List<UserDTO> userDtoList = new ArrayList<UserDTO>();
        for(User user: userService.getAllUsers()){
            userDtoList.add(new UserDTO(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(), user.getUsername(), user.getRoles().get(0), user.getQualification()));
        }

        model.addAttribute("users",userDtoList);
        return "user/users";
    }

    @GetMapping("/editUser")
    public String showEditUser(Model model, @RequestParam String id) {

        Optional<User> optionalUser = userService.findUserById(Integer.parseInt(id));

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            model.addAttribute("user",new UserDTO(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),
                    user.getUsername(), user.getRoles().get(0), user.getQualification()));
        }

        return "user/editUser";
    }

    @PostMapping("/editUser")
    public String editUser(User user, @RequestParam String id) {

        userService.editUser(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail(),Integer.parseInt(id));

        return "redirect:/users";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(Model model, @RequestParam String id) {

        User user = userService.findUserById(Integer.parseInt(id)).orElse(null);

        if(user!=null){
            userService.delete(user);

        }
        return "redirect:/users";
    }

    @GetMapping("/myInvoices")
    public String showInvoices(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByUsername(principal.getUsername());

        model.addAttribute("invoices", invoiceService.findInvoicesByClient(user));
        return "invoice/myInvoices";
    }
}
