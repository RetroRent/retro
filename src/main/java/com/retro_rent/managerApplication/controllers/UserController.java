package com.retro_rent.managerApplication.controllers;

import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.exception.ResourceNotFoundException;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.payload.ApiResponse;
import com.retro_rent.managerApplication.payload.ApiUserResponse;
import com.retro_rent.managerApplication.payload.EditRequest;
import com.retro_rent.managerApplication.security.CurrentUser;
import com.retro_rent.managerApplication.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Qualifier("UserRepository")
    @Autowired
    private UserDao userRepository;
    @Autowired
    private Environment env;

    @Qualifier("RenterRepository")
    @Autowired
    private RenterDao renterRepository;


    @Qualifier("TenantRepository")
    @Autowired
    private TenantDao tenantRepository;

    @Qualifier("roleRepository")
    @Autowired
    private RoleDao roleRepository;

    @Autowired
    private MessageBoxDao messageBoxDao;

    @GetMapping("/user/me")
    @PreAuthorize("hasAnyRole('USER', 'BOTH', 'RENTER', 'TENANT')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
            return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping("/user/getAllMessage")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT', 'RENTER')")
    public @ResponseBody List<MessagesRespons> getAllMessage(@CurrentUser UserPrincipal userPrincipal)
    {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        List<MessageBox> messageBoxes = messageBoxDao.findAllByUserAndStatus(user.get(), MessageBoxStatus.WAITING);
        List<MessagesRespons> strings = new ArrayList<>();

        for (MessageBox messageBox :
                messageBoxes) {
            strings.add(new MessagesRespons(messageBox.getId(), messageBox.getText(), messageBox.getMessageTitle(), messageBox.getDate()));
        }

        return strings;
    }

    @GetMapping("/user/getAllMessageLength")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT', 'RENTER')")
    public @ResponseBody long getAllMessageLength(@CurrentUser UserPrincipal userPrincipal)
    {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        List<MessageBox> messageBoxes = messageBoxDao.findAllByUserAndStatus(user.get(), MessageBoxStatus.WAITING);

        if (messageBoxes == null) {
            return 0;
        }

        return messageBoxes.size();
    }

    @GetMapping("/user/clearMessage/{messageID}")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT', 'RENTER')")
    public ResponseEntity<?>  clearMessage(@CurrentUser UserPrincipal userPrincipal, @PathVariable long messageID) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        MessageBox messageBox = messageBoxDao.findByUserAndId(user.get(), messageID);
        messageBox.setStatus(MessageBoxStatus.DONE);
        messageBoxDao.save(messageBox);

        return ResponseEntity.ok(new ApiResponse(true, "clear message done"));
    }

    @PostMapping("/user/edit")
    public ResponseEntity<?> editUser(@Valid @RequestBody EditRequest editRequest) {
        Optional<User> user = userRepository.findById(editRequest.getId());

        if (!user.isPresent())
        {
            return ResponseEntity.ok(new ApiResponse(false, "edit error, user id do not exist"));
        } else {
            Optional<User> by_email = userRepository.findByEmail(editRequest.getEmail());
            if (by_email.isPresent() && by_email.get().getId() != user.get().getId())
            {
                return ResponseEntity.ok(new ApiResponse(false, "edit error, user with the same email already exists"));
            } else if (editRequest.getPaymentLink().isEmpty() && editRequest.isUser_type_renter()) {
                return ResponseEntity.ok(new ApiResponse(false, "edit error, user with renter type must have PayPal Me link not empty"));
            }else {
                user.get().setPostal_code(editRequest.getPostal_code());
                user.get().setAddress_number(editRequest.getHome_number());
                user.get().setName(editRequest.getFirst_name());
                user.get().setLastName(editRequest.getLast_name());
                user.get().setUserName(editRequest.getUser_name());
                user.get().setImageUrl(editRequest.getImg_url());
                user.get().setCity(editRequest.getCity());
                user.get().setStreet(editRequest.getStreet());
                user.get().setEmail(editRequest.getEmail());
                user.get().setPhoneNumber(editRequest.getPhone_number());
                user.get().setPaymentLink(editRequest.getPaymentLink());

                switch (user.get().getRole().getRole()) {
                    case "BOTH":
                        if (editRequest.isUser_type_renter() && !editRequest.isUser_type_tenant()) {
                            tenantRepository.deleteByUser_Id(user.get().getId());
                        } else if (!editRequest.isUser_type_renter() && editRequest.isUser_type_tenant()) {
                            renterRepository.deleteByUser_Id(user.get().getId());
                        }

                        break;
                    case "TENANT" :
                        if (editRequest.isUser_type_renter() && editRequest.isUser_type_tenant()) {
                            Renter renter = new Renter();
                            renter.setUser(user.get());
                            renterRepository.save(renter);
                        } else if (editRequest.isUser_type_renter() && !editRequest.isUser_type_tenant()) {
                            Renter renter = new Renter();
                            renter.setUser(user.get());
                            renterRepository.save(renter);
                            tenantRepository.deleteByUser_Id(user.get().getId());
                        }

                        break;
                    case "RENTER" :
                        if (editRequest.isUser_type_renter() && editRequest.isUser_type_tenant()) {
                            Tenant tenant = new Tenant();
                            tenant.setUser(user.get());
                            tenantRepository.save(tenant);
                        } else if (!editRequest.isUser_type_renter() && editRequest.isUser_type_tenant()) {
                            Tenant tenant = new Tenant();
                            tenant.setUser(user.get());
                            tenantRepository.save(tenant);
                            renterRepository.deleteByUser_Id(user.get().getId());
                        }

                        break;
                }

                if (editRequest.isUser_type_renter()) {
                    if (editRequest.isUser_type_tenant()) {
                        user.get().setRole(roleRepository.findByRole("BOTH"));
                    } else {
                        user.get().setRole(roleRepository.findByRole("RENTER"));
                    }
                } else if (editRequest.isUser_type_tenant()){
                    user.get().setRole(roleRepository.findByRole("TENANT"));
                } else {
                    return ResponseEntity.ok(ResponseEntity.badRequest());
                }

                userRepository.save(user.get());

                return ResponseEntity.ok(new ApiUserResponse(true, "edit user complete", user.get()));
            }
        }
    }
}

class MessagesRespons {
    private long id;
    private String text;
    private String messageTitle;
    private Date date;

    public MessagesRespons(long id, String text, String messageTitle, Date date) {
        this.text = text;
        this.id = id;
        this.messageTitle = messageTitle;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
