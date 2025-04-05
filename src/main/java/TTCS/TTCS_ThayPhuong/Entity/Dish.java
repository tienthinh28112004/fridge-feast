package TTCS.TTCS_ThayPhuong.Entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "description",length = 255)
    private String description;

    @Column(name = "recipe", columnDefinition = "LONGTEXT",nullable = false)
    private String recipe;

    @Column(name = "price",nullable = false)
    private Float price;

    @Column(name = "dish_image")
    private String dishImage;

    @OneToMany(mappedBy = "dish",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DishHasCategory> dishHasCategoryList;
}
