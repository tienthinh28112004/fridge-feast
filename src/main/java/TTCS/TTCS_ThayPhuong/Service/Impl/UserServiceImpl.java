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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final CloudinaryService cloudinaryService;
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
    public UserResponse update(Long userId, UserUpdateRequest request, MultipartFile avatarPdf) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        if(StringUtils.hasLength(request.getPhoneNumber())&&!request.getPhoneNumber().equals(user.getPhoneNumber())){
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if(StringUtils.hasLength(String.valueOf(request.getDob()))&&!request.getDob().equals(user.getDob())){
            user.setDob(request.getDob());
        }
        String avatarUrl=null;
        if(avatarPdf!=null){
            avatarUrl = cloudinaryService.uploadImage(avatarPdf);
        }
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        return UserResponse.convert(user);
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
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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

}
