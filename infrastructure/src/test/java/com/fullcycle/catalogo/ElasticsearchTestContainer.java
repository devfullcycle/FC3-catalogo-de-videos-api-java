package com.fullcycle.catalogo;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface ElasticsearchTestContainer {

    @Container
    ElasticsearchContainer ELASTIC = new CatalogoElasticsearchContainer();

    class CatalogoElasticsearchContainer extends ElasticsearchContainer {

        private static final String IMAGE = "elasticsearch:7.17.9";
        private static final String COMPATIBLE = "docker.elastic.co/elasticsearch/elasticsearch";
        private static final String CLUSTER_NAME = "codeflix";
        private static final String CLUSTER_USER = "elastic";
        private static final String CLUSTER_PWD = "elastic";

        public CatalogoElasticsearchContainer() {
            super(DockerImageName.parse(IMAGE).asCompatibleSubstituteFor(COMPATIBLE));
            this.addFixedExposedPort(9200, 9200);
            this.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(CatalogoElasticsearchContainer.class)));
            this.withPassword(CLUSTER_PWD);
            this.setWaitStrategy(httpWaitStrategy());

            final var envMap = this.getEnvMap();
            envMap.put("ES_JAVA_OPTS", "-Xms512m -Xmx512m");
            envMap.put("cluster.name", CLUSTER_NAME);
        }

        private static HttpWaitStrategy httpWaitStrategy() {
            return new HttpWaitStrategy()
                    .forPort(9200)
                    .forPath("/")
                    .forStatusCode(200)
                    .withReadTimeout(Duration.of(5, TimeUnit.MINUTES.toChronoUnit()))
                    .withBasicCredentials(CLUSTER_USER, CLUSTER_PWD)
                    .allowInsecure();
        }
    }
}
