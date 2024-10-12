package com.example.taskday.domain.jobVacancy;

import java.util.Optional;

public record JobVacancyFiltersRequestDTO(Optional<String> stateFilter, Optional<String> cityFilter,
                                          Optional<String> nameFilter

) {
}
