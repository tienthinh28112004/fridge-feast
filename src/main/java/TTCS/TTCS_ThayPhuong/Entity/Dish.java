package TTCS.TTCS_ThayPhuong.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Dish")
@Table(name = "dish")
public class Dish extends AbstractEntity<Long>{
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "recipe", columnDefinition = "LONGTEXT",nullable = false)
    private String recipe;

    @Column(name = "time_cook")
    private Long timeCook;

    @Column(name = "price",nullable = false)
    private Float price;

    @Column(name = "dish_image")
    private String dishImage;

    @OneToMany(mappedBy = "dish",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DishHasCategory> dishHasCategoryList;

    @OneToMany(mappedBy = "dish",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DishHasIngredient> dishHasIngredients;//danh sách nguyên liệu món ăn

    @OneToMany(mappedBy = "dish",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments=new ArrayList<>();

}
