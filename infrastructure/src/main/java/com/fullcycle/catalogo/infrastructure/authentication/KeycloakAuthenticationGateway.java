package com.fullcycle.catalogo.infrastructure.authentication;

public class KeycloakAuthenticationGateway implements AuthenticationGateway {

    @Override
    public AuthenticationResult login(final ClientCredentialsInput input) {
        return null;
    }

    @Override
    public AuthenticationResult refresh(final RefreshTokenInput input) {
        return null;
    }
}
