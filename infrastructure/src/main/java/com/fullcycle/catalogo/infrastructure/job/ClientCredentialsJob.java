package com.fullcycle.catalogo.infrastructure.job;

import com.fullcycle.catalogo.infrastructure.authentication.RefreshClientCredentials;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class ClientCredentialsJob {

    private final RefreshClientCredentials refreshClientCredentials;

    public ClientCredentialsJob(final RefreshClientCredentials refreshClientCredentials) {
        this.refreshClientCredentials = Objects.requireNonNull(refreshClientCredentials);
    }

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES, initialDelay = 3)
    public void refreshClientCredentials() {
        this.refreshClientCredentials.refresh();
    }
}
