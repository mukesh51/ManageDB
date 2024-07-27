package com.bgcg.queryexecute.services;

import com.bgcg.queryexecute.entity.AdminProfile;
import com.bgcg.queryexecute.entity.StaffProfile;
import com.bgcg.queryexecute.entity.Users;
import com.bgcg.queryexecute.entity.UsersType;
import com.bgcg.queryexecute.exception.UserAlreadyExistsException;
import com.bgcg.queryexecute.exception.UserNotFoundException;
import com.bgcg.queryexecute.repository.AdminProfileRepository;
import com.bgcg.queryexecute.repository.StaffProfileRepository;
import com.bgcg.queryexecute.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final AdminProfileRepository adminProfileRepository;

    private final StaffProfileRepository staffProfileRepository;
    private  final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository,
                        AdminProfileRepository adminProfileRepository,
                        StaffProfileRepository staffProfileRepository,
                        PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.adminProfileRepository = adminProfileRepository;
        this.staffProfileRepository = staffProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users addNew(Users users){
        System.out.println("user is "+ users);
        if (usersRepository.findByEmail(users.getEmail()) != null) throw new UserAlreadyExistsException("User already exists");
//        UsersType userType = users.getUserTypeId();
//        userType.setUserTypeId(2);  // Set the userTypeId to 2 manually, which is staff ID.
//        users.setUserTypeId(userType);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser = usersRepository.save(users);
        int userTypeId = users.getUserTypeId().getUserTypeId();
        if (userTypeId == 1) {
            AdminProfile adminProfile = adminProfileRepository.save(new AdminProfile(savedUser));
        } else {
            StaffProfile staffProfile = staffProfileRepository.save(new StaffProfile(savedUser));
        }
        return savedUser;
    }

    public Users updateUserPassword(Users users){

        Optional<Users> optionalUsers = usersRepository.findById(users.getUserId());

        if (optionalUsers.isPresent()) {
            Users existingUser = optionalUsers.get();
            String encodedPassword = passwordEncoder.encode(users.getPassword());
            existingUser.setPassword(encodedPassword);
            usersRepository.save(existingUser);
            return existingUser;
        } else {
            throw new UserNotFoundException("User not found with ID: " + users.getUserId());
        }
    }


    public List<Users> getAllUsers() {  return usersRepository.findAll(); }

    public Object getCurrentUserProfile() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = usersRepository.findByEmail(username);
            int userId = users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                AdminProfile adminProfile = adminProfileRepository.findById(userId).orElse(new AdminProfile());
                return adminProfile;
            } else {
                StaffProfile staffProfile = staffProfileRepository.findById(userId).orElse(new StaffProfile());
                return staffProfile;
            }
        }
        return null;
    }
}
