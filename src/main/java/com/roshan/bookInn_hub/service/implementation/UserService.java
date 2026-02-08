package com.roshan.bookInn_hub.service.implementation;

import com.roshan.bookInn_hub.dto.LoginRequest;
import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.dto.UserDTO;
import com.roshan.bookInn_hub.exception.OurException;
import com.roshan.bookInn_hub.repository.UserRepository;
import com.roshan.bookInn_hub.security.JWTUtils;
import com.roshan.bookInn_hub.security.Utils;
import com.roshan.bookInn_hub.service.interfac.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.roshan.bookInn_hub.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public Response register(User user){
        Response response = new Response();
        try{
            if(user.getRole() == null || user.getRole().isBlank()){
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail()+"already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            UserDTO  userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest){
        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("user Not found"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Login " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers(){
        Response response = new Response();
        try{
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDTOList);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error fetching all users: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId){
        Response response = new Response();
        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(
                    () -> new OurException("User not found"));
            UserDTO dto = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(dto);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error fetching user booking history: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();
        try {
            User user =userRepository.findById(Long.valueOf(userId)).orElseThrow(
                    () -> new OurException("User not Found"));
            userRepository.deleteById(Long.valueOf(userId));

            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred While Getting Deleting User "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId){
        Response response = new Response();
        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(
                    () -> new OurException("User not found")
            );
            UserDTO dto = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(dto);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error fetching user by id: " + e.getMessage());
        }
        return response;
    }
}
