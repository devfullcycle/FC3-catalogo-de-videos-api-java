package com.fullcycle.catalogo.domain;

import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import net.datafaker.Faker;

import java.util.UUID;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static final class Categories {

        public static Category aulas() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Aulas",
                    "Conteudo gravado",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }

        public static Category talks() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Talks",
                    "Conteudo ao vivo",
                    false,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }

        public static Category lives() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Lives",
                    "Conteudo ao vivo",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }
    }

    public static final class CastMembers {

        private static final CastMember WESLEY =
                CastMember.with(UUID.randomUUID().toString(), "Wesley FullCycle", CastMemberType.ACTOR, InstantUtils.now(), InstantUtils.now());

        private static final CastMember GABRIEL =
                CastMember.with(UUID.randomUUID().toString(), "Gabriel FullCycle", CastMemberType.ACTOR, InstantUtils.now(), InstantUtils.now());

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember gabriel() {
            return CastMember.with(GABRIEL);
        }
    }

}
