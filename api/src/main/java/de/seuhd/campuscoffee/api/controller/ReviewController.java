package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.dtos.PosDto;
import de.seuhd.campuscoffee.api.dtos.ReviewDto;
import de.seuhd.campuscoffee.api.mapper.DtoMapper;
import de.seuhd.campuscoffee.api.mapper.ReviewDtoMapper;
import de.seuhd.campuscoffee.api.openapi.CrudOperation;
import de.seuhd.campuscoffee.domain.model.objects.Review;
import de.seuhd.campuscoffee.domain.ports.api.CrudService;
import de.seuhd.campuscoffee.domain.ports.api.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static de.seuhd.campuscoffee.api.openapi.Operation.*;
import static de.seuhd.campuscoffee.api.openapi.Resource.REVIEW;

/**
 * Controller for handling reviews for POS, authored by users.
 */
@Tag(name="Reviews", description="Operations for managing reviews for points of sale.")
@Controller
@RequestMapping("/api/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController extends CrudController<Review, ReviewDto, Long> {
    private final ReviewDtoMapper reviewDtoMapper;
    private final ReviewService reviewService;

    // DONE: Correctly implement the service() and mapper() methods. Note the IntelliJ warning resulting from the @NonNull annotation.

    @Override
    protected @NonNull CrudService<Review, Long> service() {
        return reviewService;
    }

    @Override
    protected @NonNull DtoMapper<Review, ReviewDto> mapper() {
        return reviewDtoMapper;
    }

    @Operation
    @CrudOperation(operation=GET_ALL, resource=REVIEW)
    @GetMapping("")
    public @NonNull ResponseEntity<List<ReviewDto>> getAll() {
        return super.getAll();
    }

    // DONE: Implement the missing methods/endpoints.
    @Operation
    @CrudOperation(operation =GET_BY_ID, resource =REVIEW)
    @GetMapping("/{id}")
    public @NonNull ResponseEntity<ReviewDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Operation
    @CrudOperation(operation=CREATE, resource =REVIEW)
    @PostMapping("")
    public @NonNull ResponseEntity<ReviewDto> create(@Parameter(description="Data to create Review.", required=true)
    @RequestBody @Valid ReviewDto reviewDto) {
        return super.create(reviewDto);
    }

    @Operation
    @CrudOperation(operation=UPDATE, resource =REVIEW)
    @PutMapping("/{id}")
    public @NonNull ResponseEntity<ReviewDto> update(@Parameter(description = "Update review",  required=true)
                                                     @PathVariable Long id,
                                                     @Parameter(description ="review description" , required= true)
                                                     @RequestBody @Valid ReviewDto reviewDto) {
        return super.update(id, reviewDto);
    }

    @Operation
    @CrudOperation(operation=DELETE, resource=REVIEW)
    @DeleteMapping("/{id}")
    public @NonNull ResponseEntity<Void> delete(@Parameter (description = "delete review", required = true)@PathVariable Long id){
        return super.delete(id);
    }
//fix this
    @Operation
    @CrudOperation(operation =FILTER, resource = REVIEW)
    @GetMapping("/filter")
    public @NonNull ResponseEntity<List<ReviewDto>> filter(@Parameter(description = "pos id to filter by", required = true)
                                                           @RequestParam("pos_id") Long posId,
                                                           @Parameter(description = "approval status to filter by", required = true)
                                                           @RequestParam("approved")Boolean approved){
        List<Review> reviews = reviewService.filter(posId, approved);
        List<ReviewDto> dtos = new ArrayList<>(reviews.size());
        for (Review review : reviews) {
            dtos.add(reviewDtoMapper.fromDomain(review));
        }
        return ResponseEntity.ok(dtos);

    }

    @Operation
    @CrudOperation(operation =UPDATE, resource = REVIEW)
    @PutMapping("/{id}/approve")
    public @NonNull ResponseEntity<ReviewDto> approve(@PathVariable Long id, @Parameter (description = "approve review", required = true)
                                                      @RequestParam("user_id") Long userId, @RequestBody @Valid ReviewDto reviewDto){
        Review review = reviewService.getById(id);
        Review approvedReview = reviewService.approve(review, userId);

        return ResponseEntity.ok(reviewDtoMapper.fromDomain(approvedReview));
    }

}
