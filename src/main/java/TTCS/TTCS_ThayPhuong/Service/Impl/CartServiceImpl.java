package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.CartAddItemRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.CartTotalResponse;
import TTCS.TTCS_ThayPhuong.Entity.CartDetail;
import TTCS.TTCS_ThayPhuong.Entity.SupplierHasIngredient;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.SupplierHasIngredientRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.CartService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final SupplierHasIngredientRepository supplierHasIngredientRepository;
    @Override
    public CartTotalResponse addOrUpdateItemCart(CartAddItemRequest request) {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new TokenExpireException("Bạn chưa đăng nhập"));
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User noy found"));
        SupplierHasIngredient hasIngredient=supplierHasIngredientRepository.findById(request.getSupplierHasIngredientId())
                .orElseThrow(()->new NotFoundException("Không tìm thấy nguyên liệu thuộc nhà hàng này"));
        if(request.getQuantity()==0){
            deleteItemCart(request.getSupplierHasIngredientId());
            return CartTotalResponse.convert(user.getCart());
        }
        boolean hasIngredientExists = false;

        //kiểm tra xem sản phẩm có trong giỏ hàng chưa
        for(CartDetail detail : user.getCart().getCartDetails()){
            if(detail.getSupplierHasIngredient().getId().equals(request.getSupplierHasIngredientId())){
                //Cập nhật lại giá
                user.getCart().setTotalMoney((user.getCart().getTotalMoney() - detail.getTotalMoneyIngedient() + hasIngredient.getPrice()* request.getQuantity()));

                //cập nhật số lượng
                detail.setQuantity(request.getQuantity());
                detail.setTotalMoneyIngedient((hasIngredient.getPrice() * request.getQuantity()));
                hasIngredientExists=true;
                break;
            }
        }

        //nếu sản phẩm chưa tồn tại thì thêm mới
        if(!hasIngredientExists){
            long totalMoney = (hasIngredient.getPrice()* request.getQuantity());
            CartDetail cartDetail = CartDetail.builder()
                    .cart(user.getCart())
                    .supplierHasIngredient(hasIngredient)
                    .quantity(request.getQuantity())
                    .totalMoneyIngedient(request.getQuantity() * hasIngredient.getPrice())
                    .build();
            user.getCart().getCartDetails().add(cartDetail);
            user.getCart().setTotalMoney(user.getCart().getTotalMoney() + totalMoney);
        }

        userRepository.save(user);
        return CartTotalResponse.convert(user.getCart());
    }

    @Override
    public void deleteItemCart(Long supplierHasIngredientId) {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new TokenExpireException("Bạn chưa đăng nhập"));
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User noy found"));
        List<CartDetail> cartDetailList = user.getCart().getCartDetails();

        CartDetail cartDetail=null;

        //Tìm sản phẩm cần xóa
        for(CartDetail detail : cartDetailList){
            if(detail.getSupplierHasIngredient().getId().equals(supplierHasIngredientId)){
                cartDetail = detail;
                user.getCart().setTotalMoney(user.getCart().getTotalMoney() - detail.getTotalMoneyIngedient());
                break;
            }
        }
        //xóa sản phẩm nếu tìm thấy(chỉ lưu nếu tìm thấy)
        if(cartDetail != null){
            cartDetailList.remove(cartDetail);
            userRepository.save(user);
        }
    }

    @Override
    public void clearAllItemCart() {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new TokenExpireException("Bạn chưa đăng nhập"));
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User noy found"));
        user.getCart().getCartDetails().clear();
        user.getCart().setTotalMoney(0L);
        userRepository.save(user);
    }
}
