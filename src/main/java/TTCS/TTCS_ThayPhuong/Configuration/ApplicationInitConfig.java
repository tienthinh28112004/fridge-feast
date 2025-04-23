package TTCS.TTCS_ThayPhuong.Configuration;

import TTCS.TTCS_ThayPhuong.Entity.Roles;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Entity.UserHasRole;
import TTCS.TTCS_ThayPhuong.Enums.Role;
import TTCS.TTCS_ThayPhuong.Repository.RolesRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ApplicationInitConfig {

    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Bean
    @Transactional
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner initApplication() {
        log.info("Initializing application.....");
        return args -> {
            Optional<Roles> roleUser = rolesRepository.findByName(String.valueOf(Role.USER));
            if(roleUser.isEmpty()) {
                rolesRepository.save(Roles.builder()
                        .name(String.valueOf(Role.USER))
                        .description("User role")
                        .build());
            }

            Optional<Roles> roleSupplier = rolesRepository.findByName(String.valueOf(Role.SUPPLIER));
            if(roleSupplier.isEmpty()) {
                rolesRepository.save(Roles.builder()
                        .name(String.valueOf(Role.SUPPLIER))
                        .description("Supplier role")
                        .build());
            }
            Optional<Roles> roleAdmin = rolesRepository.findByName(String.valueOf(Role.ADMIN));
            if (roleAdmin.isEmpty()) {
                // Tạo role ADMIN nếu chưa có
                Roles rolesAdmin = Roles.builder()
                        .name(String.valueOf(Role.ADMIN))
                        .description("Admin role")
                        .build();
                rolesRepository.save(rolesAdmin);

                // Kiểm tra nếu user admin chưa tồn tại
                Optional<User> existingAdmin = userRepository.findByEmail("admin@gmail.com");
                if (existingAdmin.isEmpty()) {
                    // Tạo user admin
                    User user = User.builder()
                            .email("admin@gmail.com")
                            .password(passwordEncoder.encode("Admin@123"))
                            .isActive(true)
                            .emailVerifiedAt(LocalDateTime.now()) // có thể set luôn nếu muốn
                            .build();

                    // Tạo quan hệ role
                    UserHasRole userHasRole = UserHasRole.builder()
                            .user(user)           // set chiều user → role
                            .role(rolesAdmin)
                            .build();

                    // Gắn set role vào user
                    Set<UserHasRole> userHasRoles = new HashSet<>();
                    userHasRoles.add(userHasRole);
                    user.setUserHasRoles(userHasRoles); // set chiều role → user

                    // Lưu user (sẽ cascade lưu luôn UserHasRole)
                    userRepository.save(user);
                    log.info("Admin account has been created.");
                } else {
                    log.info("Admin user already exists.");
                }
            }

            log.info("Application initialization completed .....");
        };
    }
}
