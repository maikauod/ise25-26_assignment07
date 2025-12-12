package de.seuhd.campuscoffee.api.dtos;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * DTO record for POS metadata.
 */
@Builder(toBuilder = true)
public record ReviewDto (
        // DONE: Implement ReviewDto
        @Nullable Long id,
        @NonNull Long authorId,
        @NotBlank String review, //not empty
        @Nullable LocalDateTime createdAt,
        @Nullable LocalDateTime updatedAt,
        @NonNull Long posId,
        @Nullable Boolean approved

) implements Dto<Long> {
    @Override
    public @Nullable Long getId() {
        return id;
    }
}
