package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationNewApp;
import com.mycompany.myapp.domain.Food;
import com.mycompany.myapp.repository.FoodRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FoodResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationNewApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FoodResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_CALORIES = 1F;
    private static final Float UPDATED_CALORIES = 2F;

    private static final Float DEFAULT_PROTEINS = 1F;
    private static final Float UPDATED_PROTEINS = 2F;

    private static final Float DEFAULT_CARBS = 1F;
    private static final Float UPDATED_CARBS = 2F;

    private static final Float DEFAULT_FATS = 1F;
    private static final Float UPDATED_FATS = 2F;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodMockMvc;

    private Food food;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Food createEntity(EntityManager em) {
        Food food = new Food()
            .name(DEFAULT_NAME)
            .calories(DEFAULT_CALORIES)
            .proteins(DEFAULT_PROTEINS)
            .carbs(DEFAULT_CARBS)
            .fats(DEFAULT_FATS);
        return food;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Food createUpdatedEntity(EntityManager em) {
        Food food = new Food()
            .name(UPDATED_NAME)
            .calories(UPDATED_CALORIES)
            .proteins(UPDATED_PROTEINS)
            .carbs(UPDATED_CARBS)
            .fats(UPDATED_FATS);
        return food;
    }

    @BeforeEach
    public void initTest() {
        food = createEntity(em);
    }

    @Test
    @Transactional
    public void createFood() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();
        // Create the Food
        restFoodMockMvc.perform(post("/api/foods").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(food)))
            .andExpect(status().isCreated());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate + 1);
        Food testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFood.getCalories()).isEqualTo(DEFAULT_CALORIES);
        assertThat(testFood.getProteins()).isEqualTo(DEFAULT_PROTEINS);
        assertThat(testFood.getCarbs()).isEqualTo(DEFAULT_CARBS);
        assertThat(testFood.getFats()).isEqualTo(DEFAULT_FATS);
    }

    @Test
    @Transactional
    public void createFoodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();

        // Create the Food with an existing ID
        food.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodMockMvc.perform(post("/api/foods").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(food)))
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFoods() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList
        restFoodMockMvc.perform(get("/api/foods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(food.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].calories").value(hasItem(DEFAULT_CALORIES.doubleValue())))
            .andExpect(jsonPath("$.[*].proteins").value(hasItem(DEFAULT_PROTEINS.doubleValue())))
            .andExpect(jsonPath("$.[*].carbs").value(hasItem(DEFAULT_CARBS.doubleValue())))
            .andExpect(jsonPath("$.[*].fats").value(hasItem(DEFAULT_FATS.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", food.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(food.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.calories").value(DEFAULT_CALORIES.doubleValue()))
            .andExpect(jsonPath("$.proteins").value(DEFAULT_PROTEINS.doubleValue()))
            .andExpect(jsonPath("$.carbs").value(DEFAULT_CARBS.doubleValue()))
            .andExpect(jsonPath("$.fats").value(DEFAULT_FATS.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFood() throws Exception {
        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Update the food
        Food updatedFood = foodRepository.findById(food.getId()).get();
        // Disconnect from session so that the updates on updatedFood are not directly saved in db
        em.detach(updatedFood);
        updatedFood
            .name(UPDATED_NAME)
            .calories(UPDATED_CALORIES)
            .proteins(UPDATED_PROTEINS)
            .carbs(UPDATED_CARBS)
            .fats(UPDATED_FATS);

        restFoodMockMvc.perform(put("/api/foods").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFood)))
            .andExpect(status().isOk());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
        Food testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFood.getCalories()).isEqualTo(UPDATED_CALORIES);
        assertThat(testFood.getProteins()).isEqualTo(UPDATED_PROTEINS);
        assertThat(testFood.getCarbs()).isEqualTo(UPDATED_CARBS);
        assertThat(testFood.getFats()).isEqualTo(UPDATED_FATS);
    }

    @Test
    @Transactional
    public void updateNonExistingFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodMockMvc.perform(put("/api/foods").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(food)))
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        int databaseSizeBeforeDelete = foodRepository.findAll().size();

        // Delete the food
        restFoodMockMvc.perform(delete("/api/foods/{id}", food.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
