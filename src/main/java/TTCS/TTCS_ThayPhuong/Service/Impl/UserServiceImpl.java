package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.Roles;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Entity.UserHasRole;
import TTCS.TTCS_ThayPhuong.Enums.Role;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Repository.RolesRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.EmailVerificationTokenService;
import TTCS.TTCS_ThayPhuong.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        User existingUser = userRepository.findByEmail(request.getEmail()).orElseGet(null);
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
                .orElseThrow(()->new NotFoundException("Không tìm thấy roleU user"));

        List<UserHasRole> userHasRoleList=new ArrayList<>();
        UserHasRole userHasRole=UserHasRole.builder()
                .role(role)
                .user(user)
                .build();
        userHasRoleList.add(userHasRole);

        //xử lý phần cart
        try {
            user.setEmailVerificationToken(emailVerificationTokenService.create(user));
            mailSenderService.sendEmailUser(user);
            log.info("User được lưu thành công");
        } catch (Exception e){
            throw new BadRequestException("Gửi Email thất bại vui lòng thử lại");
        }
    }

    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        //sort
        List<Sort.Order> orders=new ArrayList<>();
        if(sorts.length>0){
            for(String sort : sorts){
                Pattern pattern=Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher=pattern.matcher(sort);
                if(matcher.find()){
                    if(matcher.group(3).equalsIgnoreCase("asc")){
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    }else{
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }
        //Paging
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, Sort.by(orders));
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponseList=userPage.stream().map(UserResponse::convert).toList();

        return PageResponse.builder()
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
    public UserResponse getMyInfo() {
        return null;
    }

}
