package stephenaamuah.prmnt_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import stephenaamuah.prmnt_application.model.Item;
import stephenaamuah.prmnt_application.model.User;
import stephenaamuah.prmnt_application.service.ItemService;
import stephenaamuah.prmnt_application.service.UserService;

import java.util.Objects;


@Controller
@RequestMapping("/procureapp")
public class AdminController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;



    @PostMapping("/add-user")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER')")
    public String addUser(@ModelAttribute("user") User user, Authentication authentication) {
        return userService.addUser(user, authentication);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER')")
    public String getAdminDashboard(Model model, Authentication authentication) {

        model.addAttribute("loggedInUserRole", Objects.requireNonNull(authentication.getAuthorities().stream().findFirst().orElse(null)).getAuthority());
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("item", new Item());
        model.addAttribute("user", new User());
        return "dashboard";
    }


    @GetMapping("/items/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER')")
    public String getAddItem(Model model, Item item) {
        model.addAttribute("item", item);
        return "dashboard";
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER')")
    @PostMapping("/items/add")
    public String addItem(@ModelAttribute("item") Item item) {
        itemService.saveItem(item);
        return "redirect:/procureapp/dashboard";
    }

    @PostMapping("/items/update")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER')")
    public String updateItem(@RequestParam("id") Long id, @ModelAttribute("item") Item item, BindingResult result) {
        if (result.hasErrors()) {
            return "dashboard";
        }
        Item existingItem = itemService.getItemById(id);
        if (existingItem == null) {
            return "redirect:/procureapp/items";
        }

        existingItem.setAsset(item.getAsset());
        existingItem.setBrand(item.getBrand());
        existingItem.setSerialNum(item.getSerialNum());
        existingItem.setMaintenanceDate(item.getMaintenanceDate());
        existingItem.setDescription(item.getDescription());
        itemService.updateItem(existingItem);

        return "redirect:/procureapp/dashboard";
    }

    @PostMapping("/items/delete")
    @PreAuthorize("hasAuthority('SUPER')")
    public String deleteItem(@RequestParam("id") Long id) {
        itemService.deleteItem(id);
        return "redirect:/procureapp/dashboard";
    }
}
