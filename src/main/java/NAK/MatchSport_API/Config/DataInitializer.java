//package NAK.MatchSport_API.Config;
//
//import NAK.MatchSport_API.Entity.Admin;
//import NAK.MatchSport_API.Entity.SuperAdmin;
//import NAK.MatchSport_API.Repository.AdminRepository;
//import NAK.MatchSport_API.Repository.ParticipantRepository;
//import NAK.MatchSport_API.Repository.SuperAdminRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DataInitializer implements CommandLineRunner {
//
//    private final ParticipantRepository participantRepository;
//    private final AdminRepository adminRepository;
//    private final SuperAdminRepository superAdminRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) {
//        if (!participantRepository.existsByEmail("superadmin@sportmatch.com")) {
//            log.info("Creating default super admin user");
//
//            SuperAdmin superAdmin = new SuperAdmin();
//            superAdmin.setEmail("superadmin@sportmatch.com");
//            superAdmin.setPassword(passwordEncoder.encode("SuperAdmin123!"));
//            superAdmin.setFullName("Super Admin");
//            superAdmin.setDateOfBirth(LocalDate.of(2003, 1, 16));
//            superAdmin.setLocation("Safi ,Morocco");
//
//            superAdminRepository.save(superAdmin);
//            log.info("Super admin created successfully");
//        }
//
//        if (!participantRepository.existsByEmail("admin@sportmatch.com")) {
//            log.info("Creating default admin user");
//
//            Admin admin = new Admin();
//            admin.setEmail("admin@sportmatch.com");
//            admin.setPassword(passwordEncoder.encode("Admin123!"));
//            admin.setFullName("Regular Admin");
//            admin.setDateOfBirth(LocalDate.of(2003, 1, 16));
//            admin.setLocation("Safi ,Morocco");
//
//            adminRepository.save(admin);
//            log.info("Admin created successfully");
//        }
//    }
//}
//
