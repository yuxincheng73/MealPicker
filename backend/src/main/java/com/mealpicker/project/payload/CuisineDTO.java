package com.mealpicker.project.payload;

import java.util.List;

import com.mealpicker.project.model.Recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuisineDTO {

    private Long cuisineId;
    private String cuisineName;

}
