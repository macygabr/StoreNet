package org.example.server.controller;

import org.example.server.backend.Database;
import org.springframework.ui.Model;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.example.server.models.Product;
import org.example.server.backend.Server;

@Controller
public class ShopController {
    private final Server server = new Server();

    @RequestMapping("/shop")
    public String shop(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("address", request.getRemoteAddr());
        return "pages/shop";
    }

    @MessageMapping("/shop")
    @SendTo("/topic/results")
    public Product[] handleSearch(Product query) {
        System.out.println(query);
        return server.findByName(query.getName());
    }
}
