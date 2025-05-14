package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Dto.Response.DishResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientDetailResponse;

import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Entity.*;
import TTCS.TTCS_ThayPhuong.Repository.Criteria.SearchCriteria;
import TTCS.TTCS_ThayPhuong.Repository.Criteria.SearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.persistence.criteria.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Repository
@Slf4j
@Setter
@Getter
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<List<IngredientDetailResponse>> getIngredientWithSortMultiFieldAndSearch(int page,int size,String sortBy,String ...search){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierHasIngredient> criteriaQuery = criteriaBuilder.createQuery(SupplierHasIngredient.class);
        Root<SupplierHasIngredient> root = criteriaBuilder.createQuery().from(SupplierHasIngredient.class);

        Predicate predicate = getPredicateIngredient(criteriaBuilder,root,search);

        criteriaQuery.where(predicate);
        //Điều kiện order(Sawps xêếp)
        List<Order> orderList = new ArrayList<>();
        if(sortBy!=null){
            String[] listSort = sortBy.split(",");
            for(String sort : listSort){
                Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
                Matcher matcher = pattern.matcher(sort);

                if(matcher.find()){
                    String columnName = matcher.group(1);
                    if(matcher.group(3).equalsIgnoreCase("asc")){
                        orderList.add(criteriaBuilder.asc(root.get(columnName)));
                    }else{
                        orderList.add(criteriaBuilder.desc(root.get(columnName)));
                    }
                }
            }
        }
        if(!orderList.isEmpty()){
            criteriaQuery.orderBy(orderList);
        }
        List<SupplierHasIngredient> supplierHasIngredientList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((page-1)*size)//vị trí bản ghi đầu tiên
                .setMaxResults(size)//giới hạn số lượng
                .getResultList();

        Long totalElements = getTotalElementsIngredient(search);

        return PageResponse.<List<IngredientDetailResponse>>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .items(supplierHasIngredientList.stream().map(IngredientDetailResponse::convert).collect(Collectors.toList()))
                .build();
    }
    public PageResponse<List<DishResponse>> getDishWithSortMultiFieldAndSearch(int page,int size,String sortBy,String ...search){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dish> criteriaQuery = criteriaBuilder.createQuery(Dish.class);
        Root<Dish> root = criteriaQuery.from(Dish.class);

        Predicate predicate = getPredicateDish(criteriaBuilder,root,search);

        criteriaQuery.where(predicate);
        List<Order> orderList = new ArrayList<>();
        if(sortBy != null){
            String[] listSort = sortBy.split(",");
            for(String sort : listSort){
                Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
                Matcher matcher = pattern.matcher(sort);
                if(matcher.find()){
                    String columnName = matcher.group(1);
                    if(matcher.group(3).equalsIgnoreCase("asc")){
                        orderList.add(criteriaBuilder.asc(root.get(columnName)));
                    }else{
                        orderList.add(criteriaBuilder.desc(root.get(columnName)));
                    }
                }
            }
        }
        if(!orderList.isEmpty()){
            criteriaQuery.orderBy(orderList);
        }
        List<Dish> dishList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((page-1)*size)
                .setMaxResults(size)
                .getResultList();

        Long totalElements = getTotalElementsDish(search);

        return PageResponse.<List<DishResponse>>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .totalElements(totalElements)
                .items(dishList.stream().map(DishResponse::convert).collect(Collectors.toList()))
                .build();
    }

    private Long getTotalElementsDish(String ...search){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Dish> root = query.from(Dish.class);

        Predicate predicate=getPredicateDish(criteriaBuilder,root,search);
        query.select(criteriaBuilder.count(root)).where(predicate);
        //lấy ra số tổng số lượng phần tử lấy được
        return entityManager.createQuery(query).getSingleResult();
    }
    private Predicate getPredicateDish(CriteriaBuilder criteriaBuilder,Root root,String ...search){
        Predicate predicate = criteriaBuilder.conjunction();//khởi tạo predicate là true

        Join<Dish, DishHasCategory> hasCategoryDishJoin=root.join("dishHasCategoryList");
        Join<DishHasCategory, Category> hasCategoryJoin=hasCategoryDishJoin.join("category");

        List<SearchCriteria> criteriaList = new ArrayList<>();
        if(search != null){
            for(String s:search){
                Pattern pattern = Pattern.compile("(\\w+?)([:<>?])(.*)");
                Matcher matcher = pattern.matcher(s);

                if(matcher.find()){
                    if(matcher.group(1).equalsIgnoreCase("keyword")) {
                        String[] ingredientList=matcher.group(3).split(",");
                        for(String x:ingredientList) {
                            Predicate likeToName = criteriaBuilder.like(root.get("name"), "%" + x + "%");
                            Predicate likeToDescription = criteriaBuilder.like(root.get("description"), "%" + x + "%");
                            Predicate likeToRecipe = criteriaBuilder.like(root.get("recipe"), "%" + x + "%");

                            Predicate finalPre = criteriaBuilder.or(likeToName, likeToRecipe, likeToDescription);
                            predicate = criteriaBuilder.and(predicate, finalPre);
                        }
                    }else if(matcher.group(3).equalsIgnoreCase("category")){
                        Predicate categoryPredicate = criteriaBuilder.like(hasCategoryJoin.get("name"),"%"+matcher.group(3)+"%");//lấy tên của dnah mục category để so sánh
                        predicate = criteriaBuilder.and(predicate,categoryPredicate);
                    }else{
                        //các trường còn lại như giá stock xử lý ởdđây
                        criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2),matcher.group(3)));
                    }
                }
            }
        }
        //Xác định queryConsummer với giá trị ban đầu
        SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(criteriaBuilder,root,predicate);

        if(!criteriaList.isEmpty()){
            criteriaList.forEach(queryConsumer);
            predicate = criteriaBuilder.and(predicate, queryConsumer.getPredicate());
        }
        return predicate;
    }
    private Long getTotalElementsIngredient(String ...search){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<SupplierHasIngredient> root = query.from(SupplierHasIngredient.class);

        Predicate predicate=getPredicateIngredient(criteriaBuilder,root,search);
        query.select(criteriaBuilder.count(root)).where(predicate);
        //lấy ra số tổng số lượng phần tử lấy được
        return entityManager.createQuery(query).getSingleResult();
    }
    private Predicate getPredicateIngredient(CriteriaBuilder criteriaBuilder,Root root,String ...search){
        Predicate predicate = criteriaBuilder.conjunction();//khởi tạo predicate là true

        Join<SupplierHasIngredient, Ingredient> hasIngredientJoin=root.join("ingredient");
        Join<SupplierHasIngredient, Supplier> hasSupplierJoin=root.join("supplier");

        List<SearchCriteria> criteriaList = new ArrayList<>();
        if(search != null){
            for(String s:search){
                Pattern pattern = Pattern.compile("(\\w+?)([:<>?])(.*)");
                Matcher matcher = pattern.matcher(s);

                if(matcher.find()){
                    if(matcher.group(1).equalsIgnoreCase("keyword")) {
                        Predicate likeToName = criteriaBuilder.like(hasIngredientJoin.get("name"), "%" + matcher.group(3) + "%");
                        Predicate likeToDescription = criteriaBuilder.like(hasIngredientJoin.get("description"), "%" + matcher.group(3) + "%");

                        Predicate likeToSupplierName = criteriaBuilder.like(hasSupplierJoin.get("supplierName"), "%" + matcher.group(3) + "%");
                        Predicate likeToAddress = criteriaBuilder.like(hasSupplierJoin.get("address"), "%" + matcher.group(3) + "%");

                        Predicate finalPredicate = criteriaBuilder.or(likeToName, likeToSupplierName, likeToAddress, likeToDescription);
                        predicate = criteriaBuilder.and(predicate, finalPredicate);
                    }else{
                        //các trường còn lại như giá stock xử lý ởdđây
                        criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2),matcher.group(3)));
                    }
                }
            }
        }
        //Xác định queryConsummer với giá trị ban đầu
        SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(criteriaBuilder,root,predicate);

        if(!criteriaList.isEmpty()){
            criteriaList.forEach(queryConsumer);
            predicate = criteriaBuilder.and(predicate, queryConsumer.getPredicate());
        }
        return predicate;
    }

}
