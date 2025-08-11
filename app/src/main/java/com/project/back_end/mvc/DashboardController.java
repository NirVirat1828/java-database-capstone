package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import com.project.back_end.services.Services;

@Controller
public class DashboardController {

    // 2. Autowire the Shared Service for token validation logic
    @Autowired
    private Services service;

    // 3. Admin Dashboard: Validate token before showing dashboard
    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable String token) {
        ResponseEntity<Object> error = service.validateToken(token, "admin");
        if (error == null) {
            // Token valid, show admin dashboard view
            return new ModelAndView("admin/adminDashboard");
        } else {
            // Token invalid, redirect to root (login/home)
            return new ModelAndView("redirect:/");
        }
    }

    // 4. Doctor Dashboard: Validate token before showing dashboard
    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable String token) {
        ResponseEntity<Object> error = service.validateToken(token, "doctor");
        if (error == null) {
            // Token valid, show doctor dashboard view
            return new ModelAndView("doctor/doctorDashboard");
        } else {
            // Token invalid, redirect to root (login/home)
            return new ModelAndView("redirect:/");
        }
    }
}