//package com.example.ai_recruiter.serviceImple;
//
//import com.example.ai_recruiter.convertoDTO.LoginandSignup.CompanySignupRequest;
//import com.example.ai_recruiter.convertoDTO.LoginandSignup.LoginRequest;
//import com.example.ai_recruiter.convertoDTO.LoginandSignup.UserSignupRequest;
//import com.example.ai_recruiter.entity.Company;
//import com.example.ai_recruiter.entity.Role;
//import com.example.ai_recruiter.entity.User;
//import com.example.ai_recruiter.repository.CompanyRepository;
//import com.example.ai_recruiter.repository.UserRepository;
//import com.example.ai_recruiter.security.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//    @Autowired
//    private UserRepository userRepo;
//    @Autowired private CompanyRepository companyRepo;
//    @Autowired private JwtUtil jwtUtil;
//    @Autowired private PasswordEncoder encoder;
//
//    public String registerUser(UserSignupRequest request) {
//        User user = User.builder()
//                .fullName(request.getFullName())
//                .email(request.getEmail())
//                .number(request.getNumber())
//                .password(encoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
//        userRepo.save(user);
//        return "User registered successfully!";
//    }
//
//    public String registerCompany(CompanySignupRequest request) {
//        Company company = Company.builder()
//                .companyName(request.getCompanyName())
//                .companyId(request.getCompanyId())
//                .email(request.getEmail())
//                .number(request.getNumber())
//                .password(encoder.encode(request.getPassword()))
//                .role(Role.COMPANY)
//                .build();
//        companyRepo.save(company);
//        return "Company registered successfully!";
//    }
//
//    public String loginUser(LoginRequest request) {
//        User user = userRepo.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        if (encoder.matches(request.getPassword(), user.getPassword())) {
//            return jwtUtil.generateToken(user.getEmail(), user.getRole());
//        }
//        throw new RuntimeException("Invalid credentials");
//    }
//
//    public String loginCompany(LoginRequest request) {
//        Company company = companyRepo.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Company not found"));
//        if (encoder.matches(request.getPassword(), company.getPassword())) {
//            return jwtUtil.generateToken(company.getEmail(), company.getRole());
//        }
//        throw new RuntimeException("Invalid credentials");
//    }
//}

package com.example.ai_recruiter.serviceImple;

import com.example.ai_recruiter.convertoDTO.LoginandSignup.CompanySignupRequest;
import com.example.ai_recruiter.convertoDTO.LoginandSignup.LoginRequest;
import com.example.ai_recruiter.convertoDTO.LoginandSignup.UserSignupRequest;
import com.example.ai_recruiter.convertoDTO.LoginandSignup.LoginResponse;
import com.example.ai_recruiter.entity.Company;
import com.example.ai_recruiter.entity.Role;
import com.example.ai_recruiter.entity.User;
import com.example.ai_recruiter.repository.CompanyRepository;
import com.example.ai_recruiter.repository.UserRepository;
import com.example.ai_recruiter.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder encoder;

    public String registerUser(UserSignupRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .number(request.getNumber())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepo.save(user);
        return "User registered successfully!";
    }

    public String registerCompany(CompanySignupRequest request) {
        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .companyId(request.getCompanyId())
                .email(request.getEmail())
                .number(request.getNumber())
                .password(encoder.encode(request.getPassword()))
                .role(Role.COMPANY)
                .build();
        companyRepo.save(company);
        return "Company registered successfully!";
    }

    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (encoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            return new LoginResponse(user.getEmail(), user.getRole().name(), token);
        }

        throw new RuntimeException("Invalid credentials");
    }

    public LoginResponse loginCompany(LoginRequest request) {
        Company company = companyRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (encoder.matches(request.getPassword(), company.getPassword())) {
            String token = jwtUtil.generateToken(company.getEmail(), company.getRole());
            return new LoginResponse(company.getEmail(), company.getRole().name(), token);
        }

        throw new RuntimeException("Invalid credentials");
    }
}
