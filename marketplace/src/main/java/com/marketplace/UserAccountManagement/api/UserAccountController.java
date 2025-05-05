package com.marketplace.UserAccountManagement.api;

import java.util.List;

import com.marketplace.UserAccountManagement.domain.Role;
import com.marketplace.UserAccountManagement.domain.RoleService;
import com.marketplace.UserAccountManagement.domain.User;
import com.marketplace.UserAccountManagement.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

// add idempotent post operation
// consider to use projection in each controller to reduce unretrieve value and boost performance
// eliminate all "" or `` which is no need in database

@Slf4j
@RestController
@RequestMapping("/api")
public class UserAccountController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper mapper;

    @Autowired
    public UserAccountController(UserService userService, RoleService roleService, UserMapper mapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @Operation(summary = "Admin Can get User here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieve the Users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class)) }
            )
    })
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUserAccount() {
        List<UserAccountDTO> sellers = userService.getAllUserIsDeletedFalse();
        return ResponseHandler.generateResponse("Successfully retrieve the Users", HttpStatus.OK, sellers);
        
    }

    @Operation(summary = "User can be register in here")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully to create users",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class)) }
        ),
        @ApiResponse(responseCode = "302", description = "the email has already been taken",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{\"message\": \"Email already registered\", \"cause\": \"Duplicate resource\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid inserted value",
            content = { @Content(mediaType = "application/json", schema = @Schema(
                example = "Invalid inserted value"
            )) }
        )
    })
    @PostMapping("/users") // the plan this url redirect into /{id}
    public ResponseEntity<Object> createAccount(@RequestBody @Valid UserAccountCreationDTO accountDTO) {
        Role role = roleService.getOrCreateRoleAccount(Role.RoleEnum.BUYER);
        UserAccountDTO dto = userService.createUserAccount(role, mapper, accountDTO);
        return ResponseHandler.generateResponse("Successfully to create users", HttpStatus.CREATED, dto);
    }

    @Operation(summary = "Get User by id here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "User not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "Invalid inserted value"
                    )) }
            )
    })
    @GetMapping("/users/{user_id}")
    public ResponseEntity<Object> getUserById(@PathVariable("user_id") String id) {
        UserAccountDTO dto = userService.getUserDataById(id, mapper);
        return new ResponseEntity<Object>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Get User by email here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully create the store"),
            @ApiResponse(responseCode = "404", description = "User not found with email {user_email}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "Invalid inserted value"
                    )) }
            )
    })
    @GetMapping("/users/email")
    public ResponseEntity<UserAccountDTO> getUserByEmail(@RequestParam String email) {
        UserAccountDTO dto = userService.getUserDataByEmail(email, mapper);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "User can be register in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully to Edit Account",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountUpdateDTO.class)) }
            ),
            @ApiResponse(responseCode = "302", description = "the email has already been taken",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Email already registered\", \"cause\": \"Duplicate resource\"}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "User is not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "Invalid inserted value"
                    )) }
            )
    })
    @PutMapping("/users/{user_id}") // the plan this url redirect into /{id}
    public ResponseEntity<Object> updateAccount(@PathVariable("user_id") String id, @RequestBody @Valid UserAccountUpdateDTO accountDto) {
        User foundUser = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        User newUserData = userService.updateUser(foundUser, accountDto);
        UserAccountDTO data = mapper.toAccountDto(newUserData);
        return ResponseHandler.generateResponse("Successfully to Edit Account", HttpStatus.OK, data);
    }

    @Operation(summary = "User can change password in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully to change password",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountUpdatePasswordDTO.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "User is not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "Invalid inserted value"
                    )) }
            ),
            @ApiResponse(responseCode = "409", description = "repeat password should match the password",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "repeat password should match the password"
                    )) }
            )
    })
    @PutMapping(path = "/users/change-password/{user_id}") // the plan this url redirect into /{id}
    public ResponseEntity<Object> updatePassword(@PathVariable("user_id") String id, @RequestBody @Valid UserAccountUpdatePasswordDTO accountDto) {
//        for this operation, you can make the email verification first
        User foundUser = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+id));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        if (!accountDto.repeatPassword().equals(accountDto.password())) {
            throw new IllegalArgumentException("repeat password should match the password");
        }
        userService.updateUserPassword(foundUser, accountDto);
        return ResponseHandler.generateResponse("Password successfully changed", HttpStatus.ACCEPTED, "");
    }

    @Operation(summary = "Address can be added in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address successfully added",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAddressCreationDto.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "User is not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User has already been deleted"
                    )) }
            ),
            @ApiResponse(responseCode = "409", description = "User has already been deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User has already been deleted"
                    )) }
            )
    })
    @PostMapping("/users/{user_id}/addresses")
    public ResponseEntity<Object> addUserAddresses(@PathVariable("user_id") String id, @RequestBody @Valid UserAddressCreationDto addressDto) {
       userService.addUserAddress(id, addressDto);
       return ResponseHandler.generateResponse("Address successfully added", HttpStatus.CREATED, "");
    }

    @Operation(summary = "Retrieve all user address based on user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address successfully added",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAddressCreationDto.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "User is not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User has already been deleted"
                    )) }
            ),
            @ApiResponse(responseCode = "409", description = "User has already been deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User has already been deleted"
                    )) }
            )
    })
    @GetMapping("/users/{user_id}/addresses")
    public ResponseEntity<List<UserAddressDto>> getUserAddress(@PathVariable("user_id") String userId) {
        List<UserAddressDto> dtos = userService.getAllUserAddresses(userId);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Address can be edited in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address successfully updated",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAddressCreationDto.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "User is not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User not found with id"
                    )) }
            ),
            @ApiResponse(responseCode = "409", description = "User has already been deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User has already been deleted"
                    )) }
            )
    })
    @PutMapping("/users/{user_id}/addresses/address_id")
    public ResponseEntity<Object> updateUserAddress(@PathVariable("user_id") String id, @RequestParam("address_id") String addressId, @RequestBody @Valid UserAddressCreationDto addressDto) {
        userService.updateUserAddress(id, addressId, addressDto);
        return ResponseEntity.ok().body("Address successfully updated");
    }

    @Operation(summary = "Delete Address in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address successfully deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAddressCreationDto.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "User is not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User not found with id"
                    )) }
            )
    })
    @DeleteMapping("/users/{user_id}/addresses/address_id")
    public ResponseEntity<String> deleteAddress() {
        return ResponseEntity.ok("Address successfully deleted");
    }

    @Operation(summary = "Delete Account in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAddressCreationDto.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "User is not found with id {user_id}",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "User not found with id"
                    )) }
            )
    })
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable("user_id") String id) {
        userService.deleteAccountById(id);
        return ResponseHandler.generateResponse("delete successfully", HttpStatus.OK, "");

    }

    // @GetMapping("/sellers/test/{user_id}")
    // public ResponseEntity<Object> testQuery(@PathVariable("user_id") String id) {
    //     AccountProjection outputs = userService.getEmailAndName(id);
    //     return ResponseHandler.generateResponse("Test successfully", HttpStatus.OK, outputs);
    // }

}
