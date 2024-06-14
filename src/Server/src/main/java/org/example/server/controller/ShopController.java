package org.example.server.controller;

import org.example.server.backend.Database;
import org.springframework.ui.Model;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.example.server.models.Product;
import org.example.server.models.Purchase;
import org.example.server.models.Package;
import org.example.server.backend.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Controller
public class ShopController {
    private final Server server;

    @Autowired
    public ShopController(Server server) {
        this.server = server;
    }

    @RequestMapping("/shop")
    public String shop(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("address", "83.147.246.223");
        return "pages/shop";
    }

    @MessageMapping("/search")
    @SendTo("/topic/search_results")
    public Product Search(Product query) {
        return server.findByName(query.getName());
    }

    @MessageMapping("/findAllPurchase")
    @SendTo("/topic/findAllPurchase_results")
    public ArrayList<Purchase> findAllPurchaseSearch() {
        return server.findAllPurchase();
    }

    @MessageMapping("/buy")
    @SendTo("/topic/buy_results")
    public Package handleSearch(Package query) {
        Package res = new Package();
        try{
            server.addPurchase(query.getQuery());
            res.setStatus(200);
        } catch(Exception e) {
            res.setStatus(404);
            e.printStackTrace();
        }
        return res;
    }
}
