package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.ChangePasswordRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.*;
import TTCS.TTCS_ThayPhuong.Enums.Role;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.RolesRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.EmailVerificationTokenService;
import TTCS.TTCS_ThayPhuong.Service.UserService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    @Override
    public UserResponse createUser(UserCreateRequest request) {
        User existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(existingUser!=null){
            if(existingUser.getEmailVerifiedAt() != null){
                throw new BadRequestException("Email đã được đăng kí rồi");
            }else{
                try {
                    existingUser.setFullName(request.getFullName());
                    existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    //nếu user tồn tại nhwung chưa được xác thực,gwuir lại OTP
                    existingUser.setEmailVerificationToken(emailVerificationTokenService.create(existingUser));
                    mailSenderService.sendEmailUser(existingUser);
                    return UserResponse.convert(existingUser);
                } catch (Exception e){
                    throw new BadRequestException("Gửi Email thất bại vui lòng thử lại");
                }
            }
        }

        User user=User.builder()
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .isActive(false)
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .avatarUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSp3ztVtyMtzjiT_yIthons_zqTQ_TNZm4PS0LxFyFO0ozfM2S87W8QoL4&s")
                .build();
        userRepository.save(user);
        Roles role= rolesRepository.findByName(String.valueOf(Role.USER))
                .orElseThrow(()->new NotFoundException("Không tìm thấy role user"));

        Set<UserHasRole> userHasRoleList=new HashSet<>();
        UserHasRole userHasRole=UserHasRole.builder()
                .role(role)
                .user(user)
                .build();
        userHasRoleList.add(userHasRole);
        user.setUserHasRoles(userHasRoleList);

        ArrayList<CartDetail> cartDetails=new ArrayList<>();
        Cart cart=Cart.builder()
                .user(user)
                .totalMoney(0L)
                .cartDetails(cartDetails)
                .build();
        user.setCart(cart);
        userRepository.save(user);
        try {
            user.setEmailVerificationToken(emailVerificationTokenService.create(user));
            mailSenderService.sendEmailUser(user);
            log.info("User được lưu thành công");
        } catch (Exception e){
            throw new BadRequestException("Gửi Email thất bại vui lòng thử lại");
        }
        return UserResponse.convert(user);
    }

    @Override
    public PageResponse<List<UserResponse>> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        //sort
        List<Sort.Order> orders=new ArrayList<>();
        for (String sort : sorts) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        //Paging
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, Sort.by(orders));
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponseList=userPage.stream().map(UserResponse::convert).toList();

        return PageResponse.<List<UserResponse>>builder()
                .currentPage(pageNo)
                .pageSize(pageSize)
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .items(userResponseList)
                .build();
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));
        return UserResponse.convert(user);
    }

    @Override
    public UserResponse update(UserUpdateRequest request) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new BadCredentialsException("email invalid"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        //boolean isRequiredEmailVerification = false;
        if (StringUtils.hasText(request.getFullName()) && !request.getFullName().equals(user.getFullName())) {
            user.setFullName(request.getFullName());
        }

        if (request.getDob() != null && !Objects.equals(request.getDob(), user.getDob())) {
            user.setDob(request.getDob());
        }

        if (StringUtils.hasText(request.getPhoneNumber()) && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        userRepository.save(user);
        return UserResponse.convert(user);
    }

    @Override
    public void banUser(Long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User Not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void unBanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void delete(Long userId) {
        User user= userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User Not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public UserResponse changePassword(ChangePasswordRequest request) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()-> new TokenExpireException("Người dùng chưa đăng nhập"));
        if(!request.getEmail().equals(email)){
            throw new BadRequestException("Bạn không có quyền thay đổi mật khẩu");
        }
        if(!request.getNewPassword().equals(request.getNewPasswordConfirm())){
            throw new BadRequestException("Vui lòng xác nhận lại mật khẩu");
        }
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new BadRequestException("Mật khẩu bạn đưa vào không trùng với mật khẩu trong hệ thống");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return UserResponse.convert(user);
    }

    @Override
    public UserResponse getMyInfo() {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()-> new TokenExpireException("Người dùng chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        return UserResponse.convert(user);
    }

    @Override
    public Double getDistance(Long userId, Long supplierId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));

        User supplier = userRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("User not found"));
        if(user.getLatitude()==null||user.getLongitude()==null||supplier.getLatitude()==null||supplier.getLongitude()==null){
            return 0.0;
        }
        return getDistance(user.getLatitude(),user.getLongitude(),supplier.getLatitude(),supplier.getLongitude());
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        final double DEG_TO_RAD = Math.PI / 180;
        final double EARTH_RADIUS = 63710088;

        double dLat = (lat2 - lat1) * DEG_TO_RAD;
        double dLon = (lon2 - lon1) * DEG_TO_RAD;

        double lat1Rad = lat1 * DEG_TO_RAD;
        double lat2Rad = lat2 * DEG_TO_RAD;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}

// User có bảng supplier
// supplier là 1 user khác user bình thường là nó được add nâng quyền lên supplier
// supplier có chức năng thêm sản phẩm quản lý sản phẩm
