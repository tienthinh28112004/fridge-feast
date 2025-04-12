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
            if(roleAdmin.isEmpty()) {
                Roles rolesAdmin=Roles.builder()
                        .name(String.valueOf(Role.ADMIN))
                        .description("Admin role")
                        .build();
                rolesRepository.save(rolesAdmin);
                //tạo tài khoản admin
                User user= User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .build();
                Set<UserHasRole> userHasRoles=new HashSet<>();
                userHasRoles.add(UserHasRole.builder()
                        .user(user)
                        .role(rolesAdmin)
                        .build());
                user.setUserHasRoles(userHasRoles);
                log.info("admin đã được tạo");
                userRepository.save(user);
            }
            log.info("Application initialization completed .....");
        };
    }
}
