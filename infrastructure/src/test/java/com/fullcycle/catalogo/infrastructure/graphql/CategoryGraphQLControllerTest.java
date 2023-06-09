package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import com.fullcycle.catalogo.application.category.list.ListCategoryUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.infrastructure.GraphQLControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQLControllerTest
public class CategoryGraphQLControllerTest {

    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void testListCategories() {
        // given
        final var expectedCategories = List.of(
                ListCategoryOutput.from(Fixture.Categories.lives()),
                ListCategoryOutput.from(Fixture.Categories.aulas())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        when(this.listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedCategories.size(), expectedCategories));

        final var query = """
                {
                  categories {
                    id
                    name
                  }
                }
                """;

        // when
        final var res = this.graphql.document(query).execute();

        final var actualCategories = res.path("categories")
                .entityList(ListCategoryOutput.class)
                .get();

        // then
        Assertions.assertTrue(
                actualCategories.size() == expectedCategories.size()
                        && actualCategories.containsAll(expectedCategories)
        );
    }
}
