package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishResponse {
    private String name;

    private String description;

    private String recipe;

    private String timeCook;

    private Float price;

    private String dishImage;

    private List<String> dishCategory;

    private List<String> dishIngredient;

    private List<Comment> comment;

    public static String getTimeCook(Long timeCook){
        if(timeCook == null){
            return "không có thông tin";
        }
        if(timeCook < 60){
            return timeCook+" phút";
        }else if(timeCook < 24*60){
            return timeCook/60+" giờ "+timeCook%60;
        }
        return null;
    }
    public static DishResponse convert(Dish dish){
        List<String> dishCategory=new ArrayList<>();
        dish.getDishHasCategoryList().stream()
                .map(dishHasCategory -> dishHasCategory.getCategory().getName()).forEach(dishCategory::add);
        List<String> dishIngredient = new ArrayList<>();
        dish.getDishHasIngredients().stream()
                .map(dishHasIngredient -> dishHasIngredient.getIngredient().getName()).forEach(dishIngredient::add);
        return DishResponse.builder()
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .recipe(dish.getRecipe())
                .timeCook(getTimeCook(dish.getTimeCook()))
                .dishCategory(dishCategory)
                .dishIngredient(dishIngredient)
                //.comment(dish.getComments().stream().map(comment1 -> CommentResponse.convert(comment1)).collect(Collectors.toList()))
                .build();
    }
}
