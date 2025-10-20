package com.restaurant.spring.service.impl.security;

import com.restaurant.spring.controller.vm.ChangePassReqVM;
import com.restaurant.spring.dto.security.AccountDetailsDto;
import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.helper.BundleMessage;
import com.restaurant.spring.mapper.security.AccountDetailsMapper;
import com.restaurant.spring.mapper.security.AccountMapper;
import com.restaurant.spring.model.security.Account;
import com.restaurant.spring.model.security.AccountDetails;
import com.restaurant.spring.model.security.Role;
import com.restaurant.spring.repo.security.AccountRepo;
import com.restaurant.spring.repo.security.RoleRepo;
import com.restaurant.spring.service.bundlemessage.BundleMessageService;
import com.restaurant.spring.service.security.AccountService;
import com.restaurant.spring.utils.RoleEnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepo accountRepo;
    private AccountMapper accountMapper;
    private AccountDetailsMapper detailsMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepo roleRepo;


    public AccountServiceImpl(AccountMapper accountMapper,
                              AccountDetailsMapper detailsMapper,
                              AccountRepo accountRepo,
                              RoleRepo roleRepo,
                              @Lazy PasswordEncoder passwordEncoder) {
        this.accountMapper = accountMapper;
        this.accountRepo = accountRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.detailsMapper = detailsMapper;
    }

    @Override
    public AccountDto getByUserName(String username) {
        Optional<Account>  account = accountRepo.findByUsername(username);
        if(!account.isPresent()){
            throw new RuntimeException("account.username.notExists");
        }
        return accountMapper.toAccountDto(account.get());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

      if(Objects.nonNull(accountDto.getId())){
        throw new RuntimeException("id.must_be.null");
      }

      if(accountRepo.findByUsername(accountDto.getUsername()).isPresent()){
          throw new RuntimeException("account.username.exists");
      }

        Account account = accountMapper.toAccount(accountDto);

        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        account.setEnabled(true);

        initRoleToAccount(account);

        if (account.getAccountDetails() != null) {
            account.getAccountDetails().setAccount(account);
        }
        Account savedAccount = accountRepo.save(account);
        return accountMapper.toAccountDto(savedAccount);
    }

    @Override
    public String changePassword(ChangePassReqVM request) {

        AccountDto accountDto = (AccountDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountMapper.toAccount(accountDto);

        if (account == null || account.getUsername() == null) {
            throw new RuntimeException("account.user.unauthorized");
        }

        validateChangePasswordRequest(request, false, account);

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepo.save(account);

        SecurityContextHolder.clearContext();

        return "password.changed.success";
    }

    @Override
    public String resetPassword(ChangePassReqVM request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new RuntimeException("not_empty.username");
        }

        Account account = accountRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));

        validateChangePasswordRequest(request, true, account);

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepo.save(account);

        return "password.changed.success";
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        List<AccountDto> accountDtos = accountMapper.toAccountDtoList(accounts);
        return accountDtos;
    }

    @Override
    public String toggleAccountStatus(Long id) {
        Account account = accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));
        account.setEnabled(!account.isEnabled());
        accountRepo.save(account);
        return account.isEnabled() ? "account.enabled.success" : "account.disabled.success";
    }

    @Override
    public AccountDto updateAccountDetails(String username, AccountDetailsDto accountDetailsDto) {

        System.out.println("Username: " + username);
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));

        AccountDetails accountDetails = account.getAccountDetails();

        if (accountDetails != null) {
            accountDetails.setAddress(accountDetailsDto.getAddress());
            accountDetails.setAge(accountDetailsDto.getAge());
            accountDetails.setPhoneNumber(accountDetailsDto.getPhoneNumber());
        }else{
            accountDetails = detailsMapper.toAccountDetails(accountDetailsDto);
            accountDetails.setAccount(account);
            account.setAccountDetails(accountDetails);
        }

        accountRepo.save(account);
        return accountMapper.toAccountDto(account);
    }

    private void validateChangePasswordRequest(ChangePassReqVM request, boolean isForgotMode, Account account) {

        if (!isForgotMode) {
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                throw new RuntimeException("old.pass.required");
            }

            if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
                throw new RuntimeException("old.password.notCorrect");
            }
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new RuntimeException("new.pass.required");
        }

        if (!request.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{7,}$")) {
            throw new RuntimeException("new.pass.regexp");
        }

        if (!isForgotMode && passwordEncoder.matches(request.getNewPassword(), account.getPassword())) {
            throw new RuntimeException("password.sameAsOld");
        }

        if (request.getConfirmPassword() == null || request.getConfirmPassword().isEmpty()) {
            throw new RuntimeException("confirm.pass.required");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("passwords.notMatch");
        }
    }

        private void initRoleToAccount(Account account){
      Role role = roleRepo.findByRoleName(RoleEnum.USER.toString()).
              orElseThrow(() -> new RuntimeException("role.notFound"));
      List<Role> roles = account.getRoles();
      if (Objects.isNull(roles)) {
            roles = new ArrayList<>();
        }
        roles.add(role);
        account.setRoles(roles);
    }
}
