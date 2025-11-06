package com.ghallab.Ghallab_Bank.auth_users.services.impl;
import com.ghallab.Ghallab_Bank.auth_users.dtos.UpdatePasswordRequest;
import com.ghallab.Ghallab_Bank.auth_users.dtos.UserDTO;
import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.auth_users.services.UserService;
//import com.ghallab.Ghallab_Bank.aws.S3Service;
import com.ghallab.Ghallab_Bank.auth_users.repo.UserRepo;
import com.ghallab.Ghallab_Bank.aws.S3Service;
import com.ghallab.Ghallab_Bank.exceptions.BadRequestException;
import com.ghallab.Ghallab_Bank.exceptions.NotFoundException;
import com.ghallab.Ghallab_Bank.notification.dtos.NotificationDTO;
import com.ghallab.Ghallab_Bank.notification.services.NotificationService;
import com.ghallab.Ghallab_Bank.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {

    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    //private final String uploadDir = "uploads/profile-pictures/"  ;
    private final String uploadDir = "C:/Users/User/Desktop/Ghallab_Dev/Ghallab_Bank/frontend/bank-react/public/profile-picture/"  ;

   private final S3Service s3Service;

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            throw  new NotFoundException("User is not authenticated") ;
        }
        String email = authentication.getName() ;
        return userRepo.findByEmail(email).orElseThrow(()->new NotFoundException("User not found ")) ;
    }

    @Override
    public Response<UserDTO> getMyProfile() {
        User user = getCurrentLoggedInUser() ;
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return  Response.<UserDTO>builder().
                statusCode(HttpStatus.OK.value())
                .message("User fetched")
                .data(userDTO)
                .build() ;
    }

    @Override
    public Response<Page<UserDTO>> getAllUsers(int page, int size) {
        Page<User> users = userRepo.findAll(PageRequest.of(page , size));
        Page<UserDTO>userDTOS= users.map(user -> modelMapper.map(user , UserDTO.class)) ;
        return  Response.<Page<UserDTO>>builder().
                statusCode(HttpStatus.OK.value())
                .message("Users fetched")
                .data(userDTOS)
                .build() ;
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentLoggedInUser() ;
        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if (oldPassword == null || newPassword == null) {
            throw new BadRequestException("Old and New Password Required");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old Password not Correct");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);

        // Send password change confirmation email.
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Your Password Was Successfully Changed")
                .templateName("password-change")
                .templateVariable(templateVariables)
                .build();

        notificationService.sendEmail(notificationDTO, user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password Changed Successfully")
                .build();

    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);

            }
            if(user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()){
                Path oldFile = Paths.get(user.getProfilePictureUrl()) ;
                if (Files.exists(oldFile)){
                    Files.delete(oldFile);
                }

            }

            String originalFileName = file.getOriginalFilename() ;
            String fileExtension = "" ;
            if (originalFileName != null&& originalFileName.contains("."))
            {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID() + fileExtension ;
            Path filePath = uploadPath.resolve(newFilename) ;

            Files.copy(file.getInputStream() , filePath) ;
            //for backend
            //String fileUrl = uploadDir + newFilename ;
            //for frontend
            String fileUrl = "profile-picture/" + newFilename ;

            user.setProfilePictureUrl(fileUrl);
            userRepo.save(user) ;

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully.")
                    .data(fileUrl)
                    .build();



        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public Response<?> uploadProfilePictureToS3(MultipartFile file){

        log.info("Inside uploadProfilePictureToS3()");
        User user = getCurrentLoggedInUser();

        try {

            if(user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()){
                s3Service.deleteFile(user.getProfilePictureUrl());
            }
            String s3Url = s3Service.uploadFile(file, "profile-pictures");

            log.info("profile url is: {}", s3Url );

            user.setProfilePictureUrl(s3Url);
            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully.")
                    .data(s3Url)
                    .build();

        }catch (IOException e){

            throw new RuntimeException(e.getMessage());
        }
    }
}
