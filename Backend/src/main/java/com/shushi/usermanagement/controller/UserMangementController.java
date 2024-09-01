package com.shushi.usermanagement.controller;

import com.shushi.usermanagement.dto.ReqRes;
import com.shushi.usermanagement.entity.OurUsers;
import com.shushi.usermanagement.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserMangementController {

    @Autowired
    private UserManagementService userManagementService;

    //Register User
    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> Register(@RequestBody ReqRes req) {
        return ResponseEntity.ok(userManagementService.registerUser(req));
    }

    //Login User
    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> Login(@RequestBody ReqRes req) {
        return ResponseEntity.ok(userManagementService.loginUser(req));
    }

    //Refresh Token
    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> RefreshToken(@RequestBody ReqRes req) {
        return ResponseEntity.ok(userManagementService.refreshToken(req));
    }

    //Get All Users
    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }

    //Get User By Id
    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable int userId) {
        return ResponseEntity.ok(userManagementService.getUserById(userId));
    }

    //Delete User By Id
    @DeleteMapping("/admin/delete-user/{userId}")
    public ResponseEntity<ReqRes> deleteUserById(@PathVariable int userId) {
        return ResponseEntity.ok(userManagementService.deleteUserById(userId));
    }

    //Update the User
    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUserById(@PathVariable int userId, @RequestBody OurUsers req) {
        return ResponseEntity.ok(userManagementService.updateUser(userId, req));
    }

    //Get My Profile
    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = userManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
