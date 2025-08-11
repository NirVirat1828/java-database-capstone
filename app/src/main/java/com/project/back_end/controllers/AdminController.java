package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final Services services;

    @Autowired
    public AdminController(Services services) {
        this.services = services;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> adminLogin(@RequestBody Admin admin) {
        return services.validateAdmin(admin.getUsername(), admin.getPassword());
    }
}