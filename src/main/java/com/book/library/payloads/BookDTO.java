package com.book.library.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    @NotBlank
    @Size(min = 5, max = 100, message = "Book Title should be between 5 and 100 characters.")
    private String title;
    @NotBlank
    @Size(min = 3, message = "Book Author should be at least 3 chars.")
    private String author;

    @NotNull
    @DecimalMin(value = "0.01", message = "Book Price should be greater than 0.")
    private Double price;
    @NotNull
    @Min(value = 1, message = "Book Quantity should be equal or greater than 1.")
    private Integer quantity;
}
