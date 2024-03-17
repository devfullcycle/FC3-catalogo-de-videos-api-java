package com.fullcycle.catalogo.application.category.get;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidIds_whenCallsGetAllById_shouldReturnIt() {
        // given
        final var categories = List.of(
                Fixture.Categories.lives(),
                Fixture.Categories.aulas()
        );

        final var expectedItems = categories.stream()
                .map(GetAllByIdUseCase.Output::new)
                .toList();

        final var expectedIds = categories.stream().map(Category::id).collect(Collectors.toSet());

        when(this.categoryGateway.findAllById(any()))
                .thenReturn(categories);

        // when
        final var actualOutput = this.useCase.execute(new GetAllByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.size() &&
                        expectedItems.containsAll(actualOutput)
        );

        verify(this.categoryGateway, times(1)).findAllById(expectedIds);
    }

    @Test
    public void givenNullIds_whenCallsGetAllById_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = this.useCase.execute(new GetAllByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(actualOutput.isEmpty());

        verify(this.categoryGateway, times(0)).findAllById(any());
    }
}