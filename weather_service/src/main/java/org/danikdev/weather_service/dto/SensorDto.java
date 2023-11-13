package org.danikdev.weather_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDto {
    @JsonIgnore
    private Long id;
    @Schema(example = "Sensor 2")
    @NotNull(message = "'name' must not be null or you didn't include it in json")
    @NotEmpty(message = "'name' must not be empty")
    @Size(min = 3, max = 30, message = "Length must be between 3 and 30 characters")
    private String name;
    @JsonIgnore
    private boolean enabled;
}
