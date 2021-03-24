package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationNewApp;
import com.mycompany.myapp.domain.Meal;
import com.mycompany.myapp.repository.MealRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MealResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationNewApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MealResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMealMockMvc;

    private Meal meal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meal createEntity(EntityManager em) {
        Meal meal = new Meal()
            .date(DEFAULT_DATE)
            .quantity(DEFAULT_QUANTITY);
        return meal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meal createUpdatedEntity(EntityManager em) {
        Meal meal = new Meal()
            .date(UPDATED_DATE)
            .quantity(UPDATED_QUANTITY);
        return meal;
    }

    @BeforeEach
    public void initTest() {
        meal = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeal() throws Exception {
        int databaseSizeBeforeCreate = mealRepository.findAll().size();
        // Create the Meal
        restMealMockMvc.perform(post("/api/meals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(meal)))
            .andExpect(status().isCreated());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeCreate + 1);
        Meal testMeal = mealList.get(mealList.size() - 1);
        assertThat(testMeal.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMeal.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createMealWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mealRepository.findAll().size();

        // Create the Meal with an existing ID
        meal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMealMockMvc.perform(post("/api/meals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(meal)))
            .andExpect(status().isBadRequest());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMeals() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        // Get all the mealList
        restMealMockMvc.perform(get("/api/meals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meal.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        // Get the meal
        restMealMockMvc.perform(get("/api/meals/{id}", meal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meal.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingMeal() throws Exception {
        // Get the meal
        restMealMockMvc.perform(get("/api/meals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        int databaseSizeBeforeUpdate = mealRepository.findAll().size();

        // Update the meal
        Meal updatedMeal = mealRepository.findById(meal.getId()).get();
        // Disconnect from session so that the updates on updatedMeal are not directly saved in db
        em.detach(updatedMeal);
        updatedMeal
            .date(UPDATED_DATE)
            .quantity(UPDATED_QUANTITY);

        restMealMockMvc.perform(put("/api/meals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeal)))
            .andExpect(status().isOk());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
        Meal testMeal = mealList.get(mealList.size() - 1);
        assertThat(testMeal.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMeal.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingMeal() throws Exception {
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealMockMvc.perform(put("/api/meals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(meal)))
            .andExpect(status().isBadRequest());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        int databaseSizeBeforeDelete = mealRepository.findAll().size();

        // Delete the meal
        restMealMockMvc.perform(delete("/api/meals/{id}", meal.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
