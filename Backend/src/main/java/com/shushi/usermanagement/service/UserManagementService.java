package com.shushi.usermanagement.service;

import com.shushi.usermanagement.dto.ReqRes;
import com.shushi.usermanagement.entity.OurUsers;
import com.shushi.usermanagement.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Register Users
    public ReqRes registerUser(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setName(registrationRequest.getName());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers ourUserResult = usersRepo.save(ourUser);
            if (ourUserResult.getId() > 0) {
                resp.setOurUser(ourUserResult);
                resp.setMessage("USER SAVED SUCCESSFULLY!!");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    //login User
    public ReqRes loginUser(ReqRes loginRequest) {
        ReqRes resp = new ReqRes();

        try {
            authenticationManager
                    .authenticate(new
                            UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));

            OurUsers user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRefreshToken(refreshToken);
            resp.setRole(user.getRole());
            resp.setExpirationTime("24 HOURS");
            resp.setMessage("USER LOGGED IN SUCCESSFULLY!!");

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }

    //Refresh Token
    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes resp = new ReqRes();

        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                String jwt = jwtUtils.generateToken(users);
                resp.setStatusCode(200);
                resp.setToken(jwt);
                resp.setRefreshToken(refreshTokenRequest.getRefreshToken());
                resp.setExpirationTime("24 HOURS");
                resp.setMessage("SUCCESSFULLY REFRESHED TOKEN!!");
            }
            resp.setStatusCode(200);
            return resp;

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
            return resp;
        }
    }

    //Get All Users
    public ReqRes getAllUsers() {

        ReqRes resp = new ReqRes();

        try {
            List<OurUsers> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                resp.setOurUsersList(result);
                resp.setStatusCode(200);
                resp.setMessage("SUCCESSFULLY LISTED USERS!!");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("USERS NOT FOUND!");
            }
            return resp;
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
            return resp;
        }
    }

    //Get User By Id
    public ReqRes getUserById(Integer id) {
        ReqRes resp = new ReqRes();
        try {
            OurUsers userById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("USER NOT FOUND WITH ID:" + id));
            resp.setOurUser(userById);
            resp.setStatusCode(200);
            resp.setMessage("USER FOUND WITH ID:" + id);

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    //Delete User
    public ReqRes deleteUserById(Integer id) {
        ReqRes resp = new ReqRes();
        try{
            Optional<OurUsers> userFromDB = usersRepo.findById(id);
            if(userFromDB.isPresent()){
                usersRepo.deleteById(id);
                resp.setStatusCode(200);
                resp.setMessage("USER DELETED SUCCESSFULLY!!");
            }else{
                resp.setStatusCode(404);
                resp.setMessage("USER NOT FOUND!");
            }
        }catch (Exception e) {
            resp.setStatusCode(404);
            resp.setError("USER NOT FOUND WITH ID:" + id);
        }
        return resp;
    }

    //Update User
    public ReqRes updateUser(Integer userId, OurUsers updateUserRequest) {
        ReqRes resp = new ReqRes();
        try{
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if(userOptional.isPresent()){
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updateUserRequest.getEmail());
                existingUser.setName(updateUserRequest.getName());
                existingUser.setRole(updateUserRequest.getRole());
                existingUser.setCity(updateUserRequest.getCity());

                if(updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()){
                    existingUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                resp.setOurUser(savedUser);
                resp.setStatusCode(200);
                resp.setMessage("USER UPDATED SUCCESSFULLY!!");


            }else{
                resp.setStatusCode(404);
                resp.setMessage("USER NOT FOUND!");
            }
        }catch (Exception e) {
                resp.setStatusCode(500);
                resp.setError(e.getMessage());
        }
        return resp;
    }

    //Get User Info
    public ReqRes getMyInfo(String email){
        ReqRes resp = new ReqRes();
        try{
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if(userOptional.isPresent()){
                resp.setOurUser(userOptional.get());
                resp.setStatusCode(200);
                resp.setMessage("USER FOUND SUCCESSFULLY!!");
            }else{
                resp.setStatusCode(404);
                resp.setMessage("USER NOT FOUND!");
            }
        }catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
}
