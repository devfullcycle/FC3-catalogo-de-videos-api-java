package com.fullcycle.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.catalogo.application.castmember.get.GetAllCastMembersByIdUseCase;
import com.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import com.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class CastMemberUseCasesConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCasesConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    ListCastMemberUseCase listCastMemberUseCase() {
        return new ListCastMemberUseCase(castMemberGateway);
    }

    @Bean
    SaveCastMemberUseCase saveCastMemberUseCase() {
        return new SaveCastMemberUseCase(castMemberGateway);
    }

    @Bean
    GetAllCastMembersByIdUseCase getAllCastMembersByIdUseCase() {
        return new GetAllCastMembersByIdUseCase(castMemberGateway);
    }
}
