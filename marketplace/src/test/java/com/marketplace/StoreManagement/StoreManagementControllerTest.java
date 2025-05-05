package com.marketplace.StoreManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.StoreManagement.api.*;
import com.marketplace.StoreManagement.domain.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StoreManagementController.class)
@TestMethodOrder(MethodOrderer.class)
@AutoConfigureMockMvc(addFilters = false)
public class StoreManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private StoreService storeService;

    @MockitoBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StoreMapper mapper;

    private Account account;

    private Role role;

    private Store store;

    @BeforeEach
    public void setup() {
        String accountId = "account_id";
        String accountNameTest = "account_name_test";
        String accountEmailTest = "account_email_test";
        String accountPassword = "account_password_test";

        account = Account.builder()
                .id(accountId)
                .name(accountNameTest)
                .email(accountEmailTest)
                .password(accountPassword)
                .build();
    }

    @RepeatedTest(3)
    @Order(1)
    public void createStoreTest_thenReturnCreatedStatus() throws Exception {
        String accountId = "account_id";

        String storeId = "store_id";

        String storeName = "store_name_test";
        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();
        StoreRequestDTO storeDto = new StoreRequestDTO(storeName);

        StoreDto expected = new StoreDto(storeName);

        Role createdRole = new Role(Role.RoleEnum.SELLER);

        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));
        given(roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER)).willReturn(createdRole);
        Role createRole = roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER);
        given(storeService.hasStoreNameSame(storeName)).willReturn(false);
        Boolean checkStoreName = storeService.hasStoreNameSame(storeName);
        given(accountService.createStore(accountId, createdRole, false, storeDto)).willReturn(createdStore);

        Store createdStore1 = accountService.createStore(accountId, createRole, checkStoreName, storeDto);

        given(mapper.toStoreDto(createdStore1)).willReturn(expected);
        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore1);
        System.out.println("parseToStoreDto = " + parseToStoreDto);
        
        ResultActions response = mockMvc.perform(post("http://localhost:8080/api/sellers/{seller_id}/stores", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDto))
        );

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Store created"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.data.store_name").value(storeName));

    }

    @RepeatedTest(3)
    @Order(2)
    public void createStore_whenAccountIsNotFound_thenThrowResourceNotFoundException() throws Exception {
        String accountId = "invalid_account_id";

        String storeName = "store_name_test";

        StoreRequestDTO storeDto = new StoreRequestDTO(storeName);
        Role createdRole = new Role(Role.RoleEnum.SELLER);

        given(accountService.getAccountById(accountId)).willReturn(Optional.empty());
        given(roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER)).willReturn(createdRole);
        Role createRole = roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER);
        given(storeService.hasStoreNameSame(storeName)).willReturn(false);
        Boolean checkStoreName = storeService.hasStoreNameSame(storeName);
        given(accountService.createStore(accountId, createRole, checkStoreName, storeDto)).willThrow(new ResourceNotFoundException("account with this id is not found " + accountId));

        ResultActions response = mockMvc.perform(post("http://localhost:8080/api/sellers/{seller_id}/stores", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDto))
        );

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("account with this id is not found " + accountId));
    }

    @RepeatedTest(3)
    @Order(3)
    public void createStoreTest_whenStoreRequestDtoNull_thenThrowNotNull() throws Exception {
        String accountId = "account_id";

        String storeId = "store_id";

        String storeName = "store_name_test";
        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();
        StoreRequestDTO storeDto = new StoreRequestDTO(null);

        StoreDto expected = new StoreDto(storeName);

        Role createdRole = new Role(Role.RoleEnum.SELLER);

        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));
        given(roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER)).willReturn(createdRole);
        Role createRole = roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER);
        given(storeService.hasStoreNameSame(storeName)).willReturn(false);
        Boolean checkStoreName = storeService.hasStoreNameSame(storeName);
        given(accountService.createStore(accountId, createdRole, false, storeDto)).willReturn(createdStore);

        Store createdStore1 = accountService.createStore(accountId, createRole, checkStoreName, storeDto);

        given(mapper.toStoreDto(createdStore1)).willReturn(expected);
        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore1);
        System.out.println("parseToStoreDto = " + parseToStoreDto);

        ResultActions response = mockMvc.perform(post("http://localhost:8080/api/sellers/{seller_id}/stores", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDto))
        );

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Invalid store name: Empty/Null")));

    }

    @RepeatedTest(3)
    @Order(4)
    public void createStoreTest_whenStoreRequestNotInRange_thenThrowErrorMessageWithCharacter() throws Exception {
        String accountId = "account_id";

        String storeId = "store_id";

        String storeName = "store_name_test";
        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();
        StoreRequestDTO storeDto = new StoreRequestDTO("fs");

        StoreDto expected = new StoreDto(storeName);

        Role createdRole = new Role(Role.RoleEnum.SELLER);

        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));
        given(roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER)).willReturn(createdRole);
        Role createRole = roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER);
        given(storeService.hasStoreNameSame(storeName)).willReturn(false);
        Boolean checkStoreName = storeService.hasStoreNameSame(storeName);
        given(accountService.createStore(accountId, createdRole, false, storeDto)).willReturn(createdStore);

        Store createdStore1 = accountService.createStore(accountId, createRole, checkStoreName, storeDto);

        given(mapper.toStoreDto(createdStore1)).willReturn(expected);
        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore1);
        System.out.println("parseToStoreDto = " + parseToStoreDto);

        ResultActions response = mockMvc.perform(post("http://localhost:8080/api/sellers/{seller_id}/stores", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDto))
        );

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Store name must be 3-20 characters")));

    }

    @RepeatedTest(3)
    @Order(5)
    public void createStoreTest_whenStoreRequestNotOverRange_thenThrowErrorMessageWithCharacter() throws Exception {
        String accountId = "account_id";

        String storeId = "store_id";

        String storeName = "store_name_test";
        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();
        StoreRequestDTO storeDto = new StoreRequestDTO("testssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

        StoreDto expected = new StoreDto(storeName);

        Role createdRole = new Role(Role.RoleEnum.SELLER);

        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));
        given(roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER)).willReturn(createdRole);
        Role createRole = roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER);
        given(storeService.hasStoreNameSame(storeName)).willReturn(false);
        Boolean checkStoreName = storeService.hasStoreNameSame(storeName);
        given(accountService.createStore(accountId, createdRole, false, storeDto)).willReturn(createdStore);

        Store createdStore1 = accountService.createStore(accountId, createRole, checkStoreName, storeDto);

        given(mapper.toStoreDto(createdStore1)).willReturn(expected);
        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore1);
        System.out.println("parseToStoreDto = " + parseToStoreDto);

        ResultActions response = mockMvc.perform(post("http://localhost:8080/api/sellers/{seller_id}/stores", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDto))
        );

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Store name must be 3-20 characters")));

    }

    @RepeatedTest(3)
    @Order(6)
    public void getStoreWithAnAccountTest_whenSuccess_thenReturnResponseOk() throws Exception {
        String accountId = "account_id";
        String storeId = "store_id";
        String storeName = "store_name_test";

        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));

        StoreDto expected = new StoreDto(storeName);
        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();

        createdStore.setAccount(account);

        Account existingAccount = accountService.getAccountById(accountId).get();
        existingAccount.setStore(createdStore);
        Store existingStore = existingAccount.getStore();

        given(mapper.toStoreDto(existingStore)).willReturn(expected);

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/sellers/{seller_id}/stores/store", accountId)
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Get the stores from the sellers"))
                .andExpect(jsonPath("$.data.store_name").value(storeName));

    }

    @RepeatedTest(3)
    @Order(7)
    public void getStoreWithAnAccountTest_whenAccountIdNotFound_thenThrowResourceNotFoundException() throws Exception {
        String invalidId = "invalid_id";
        String storeId = "store_id";
        String storeName = "store_name_test";



        StoreDto expected = new StoreDto(storeName);
        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();

        createdStore.setAccount(account);

        given(accountService.getAccountById(invalidId)).willThrow(new ResourceNotFoundException("resource not found"));

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/sellers/{seller_id}/stores/store", invalidId)
        );

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("resource not found"));

    }

    @RepeatedTest(3)
    @Order(8)
    public void getStoreWithAnAccountTest_whenStoreIsNull_thenThrowIllegalArgumentException() throws Exception {
        String accountId = "account_id";
        String storeId = "store_id";
        String storeName = "store_name_test";


        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));

        StoreDto expected = new StoreDto(storeName);
        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();

        createdStore.setAccount(account);

        Account existingAccount = accountService.getAccountById(accountId).get();
        Store existingStore = existingAccount.getStore();

        given(mapper.toStoreDto(existingStore)).willReturn(expected);

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/sellers/{seller_id}/stores/store", accountId)
        );

        response.andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("You need to create store first"));

    }

    @RepeatedTest(3)
    @Order(9)
    public void doesAccountHaveStoreTest_whenSuccess_thenReturnTrueAndResponseEntityOk() throws Exception {
        String accountId = "account_id";
        String storeId = "store_id";
        String storeName = "store_name_test";

        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));

        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();

        createdStore.setAccount(account);

        Account existingAccount = accountService.getAccountById(accountId).get();
        existingAccount.setStore(createdStore);

        given(accountService.saveAccount(existingAccount)).willReturn(existingAccount);
        given(accountService.doesAccountHaveStore(accountId)).willReturn(true);

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/sellers/{account_id}/check", accountId)
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @RepeatedTest(3)
    @Order(10)
    public void doesAccountHaveStoreTest_whenSuccess_thenReturnFalseAndResponseEntityOk() throws Exception {
        String accountId = "account_id";
        String storeId = "store_id";
        String storeName = "store_name_test";

        given(accountService.getAccountById(accountId)).willReturn(Optional.of(account));

        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .build();

        createdStore.setAccount(account);

        Account existingAccount = accountService.getAccountById(accountId).get();

        given(accountService.saveAccount(existingAccount)).willReturn(existingAccount);
        given(accountService.doesAccountHaveStore(accountId)).willReturn(false);

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/sellers/{account_id}/check", accountId)
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @RepeatedTest(3)
    @Order(11)
    public void doesAccountHaveStoreTest_whenAccountNotFound_thenThrowResourceNotFoundException() throws Exception {
        String sellerId = "invalid_id";

        given(accountService.getAccountById(sellerId)).willReturn(Optional.empty());
        given(accountService.doesAccountHaveStore(sellerId)).willThrow(new ResourceNotFoundException("User account with this id " + sellerId + " not found"));

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/sellers/{account_id}/check", sellerId)
        );

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User account with this id " + sellerId + " not found"));
    }

    @RepeatedTest(3)
    @Order(12)
    public void getAllStores_thenReturnStatusOK() throws Exception {
        String storeId = "store_id";
        String storeName = "store_name_test";

        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .account(account)
                .build();
        account.setStore(createdStore);

        Account account1 = Account.builder()
                        .id("account1_id")
                        .name("account1_name")
                        .email("account1_email")
                        .password("account1_password")
                        .build();
        Store createdStore1 = Store.builder()
                .id("store1_id")
                .name("store2_id")
                .account(account1)
                .build();
        account1.setStore(createdStore1);

        StoreProjection storeProjection = new StoreProjection(storeId, storeName);
        StoreProjection storeProjection1 = new StoreProjection("account1_id", "account1_name");

//        given(accountService.saveAccount(account)).willReturn(account);
//        given(accountService.saveAccount(account1)).willReturn(account1);
        given(storeService.getAllIdAndNameStores()).willReturn(List.of(storeProjection, storeProjection1));


        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/stores")
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully to retrieve the stores"))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].storeId").value("store_id"))
                .andExpect(jsonPath("$.data[0].storeName").value("store_name_test"))
                .andExpect(jsonPath("$.data[1].storeId").value("account1_id"))
                .andExpect(jsonPath("$.data[1].storeName").value("account1_name"));
    }

    @RepeatedTest(3)
    @Order(12)
    public void getAllStores_whenStoreHasNotCreated_thenReturnStatusOK() throws Exception {
        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/stores")
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully to retrieve the stores"))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @RepeatedTest(3)
    @Order(13)
    public void updateStoreLogoTest_thenReturnCreatedStatus() throws Exception {

        String storeId = "store_id";
        String storeName = "store_name_test";

        Store createdStore = Store.builder()
                .id(storeId)
                .name(storeName)
                .account(account)
                .build();
        account.setStore(createdStore);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "image_test.png",
                "image/png",
                "image_test".getBytes()
        );

        String uploadDir = "test/upload/image/";
        Profile profile = new Profile(uploadDir, createdStore);
        createdStore.setStoreProfile(profile);
        Path mockPath = Paths.get(uploadDir)
                .resolve(storeId)
                .resolve("profile/profile_" + storeId + ".png");

        given(storeService.getStoreById(storeId)).willReturn(Optional.of(createdStore));
        Store existingStore = storeService.getStoreById(storeId).get();
        System.out.println("existingStore = " + existingStore);
        given(storeService.uploadStoreProfile(storeId, file, uploadDir)).willReturn(mockPath.toString());
        String uploadPath = storeService.uploadStoreProfile(storeId, file, uploadDir);
        System.out.println("uploadPath = " + uploadPath);
        
        ResultActions response = mockMvc.perform(multipart("http://localhost:8080/api/stores/{store_id}/upload_image/image", storeId)
                .file(file)
                .accept(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Image successfully to uploaded"));
    }

    @RepeatedTest(3)
    @Order(14)
    public void updateStoreLogoTest_whenStoreNotFound_thenThrowNotFound() throws Exception {

        String invalidId = "invalidId";
        String storeName = "store_name_test";

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "image_test.png",
                "image/png",
                "image_test".getBytes()
        );

        String uploadDir = "test/upload/image/";

        given(storeService.getStoreById(invalidId)).willReturn(Optional.empty());
        given(storeService.uploadStoreProfile(invalidId, file, uploadDir)).willThrow(new ResourceNotFoundException("The store with this id not found"));
        ResultActions response = mockMvc.perform(multipart("http://localhost:8080/api/stores/{store_id}/upload_image/image", invalidId)
                .file(file)
                .accept(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @RepeatedTest(3)
    @Order(14)
    public void updateStoreLogoTest_whenFileIsGif_thenThrowIllegalArgumentException() throws Exception {

        String storeId = "store_id";
        String storeName = "store_name_test";

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "image_test.gif",
                "image/gif",
                "image_test".getBytes()
        );

        String uploadDir = "test/upload/image/";
//        Profile profile = new Profile(uploadDir, createdStore);
//        createdStore.setStoreProfile(profile);

        given(storeService.getStoreById(storeId)).willReturn(Optional.of(store));
        given(storeService.uploadStoreProfile(storeId, file, uploadDir)).willThrow(new IllegalArgumentException("Invalid file type. Only PNG or JPEG or JPG files are allowed"));
        ResultActions response = mockMvc.perform(multipart("http://localhost:8080/api/stores/{store_id}/upload_image/image", storeId)
                .file(file)
                .accept(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }


}
