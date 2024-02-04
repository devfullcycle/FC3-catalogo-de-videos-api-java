package com.fullcycle.catalogo.application.castmember.save;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.domain.validation.Error;
import com.fullcycle.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCastMemberUseCase extends UseCase<CastMember, CastMember> {

    private final CastMemberGateway castMemberGateway;

    public SaveCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMember execute(final CastMember aMember) {
        if (aMember == null) {
            throw NotificationException.with(new Error("'aMember' cannot be null"));
        }

        final var notification = Notification.create();
        aMember.validate(notification);

        if (notification.hasError()) {
            throw NotificationException.with("Invalid cast member", notification);
        }

        return this.castMemberGateway.save(aMember);
    }
}
