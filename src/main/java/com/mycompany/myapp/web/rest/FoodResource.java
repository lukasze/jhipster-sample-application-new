package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Food;
import com.mycompany.myapp.repository.FoodRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Food}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FoodResource {

    private final Logger log = LoggerFactory.getLogger(FoodResource.class);

    private static final String ENTITY_NAME = "food";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodRepository foodRepository;

    public FoodResource(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    /**
     * {@code POST  /foods} : Create a new food.
     *
     * @param food the food to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new food, or with status {@code 400 (Bad Request)} if the food has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/foods")
    public ResponseEntity<Food> createFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to save Food : {}", food);
        if (food.getId() != null) {
            throw new BadRequestAlertException("A new food cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.created(new URI("/api/foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /foods} : Updates an existing food.
     *
     * @param food the food to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated food,
     * or with status {@code 400 (Bad Request)} if the food is not valid,
     * or with status {@code 500 (Internal Server Error)} if the food couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/foods")
    public ResponseEntity<Food> updateFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to update Food : {}", food);
        if (food.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, food.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /foods} : get all the foods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foods in body.
     */
    @GetMapping("/foods")
    public List<Food> getAllFoods() {
        log.debug("REST request to get all Foods");
        return foodRepository.findAll();
    }

    /**
     * {@code GET  /foods/:id} : get the "id" food.
     *
     * @param id the id of the food to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the food, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/foods/{id}")
    public ResponseEntity<Food> getFood(@PathVariable Long id) {
        log.debug("REST request to get Food : {}", id);
        Optional<Food> food = foodRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(food);
    }

    /**
     * {@code DELETE  /foods/:id} : delete the "id" food.
     *
     * @param id the id of the food to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/foods/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        log.debug("REST request to delete Food : {}", id);
        foodRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
