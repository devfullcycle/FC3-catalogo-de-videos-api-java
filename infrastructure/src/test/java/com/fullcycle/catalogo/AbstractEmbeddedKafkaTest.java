package com.fullcycle.catalogo;

import com.fullcycle.catalogo.infrastructure.configuration.WebServerConfig;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.Source;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@ActiveProfiles("test-integration")
@EnableAutoConfiguration(exclude = {
        ElasticsearchRepositoriesAutoConfiguration.class,
})
@SpringBootTest(
        classes = {WebServerConfig.class, IntegrationTestConfiguration.class},
        properties = {"kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"}
)
@Tag("integrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractEmbeddedKafkaTest {

    private Producer<String, String> producer;

    private AdminClient admin;

    @Autowired
    protected EmbeddedKafkaBroker kafkaBroker;

    @BeforeAll
    void init() {
        admin = AdminClient.create(Collections.singletonMap(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker.getBrokersAsString()));

        producer =
                new DefaultKafkaProducerFactory<>(KafkaTestUtils.producerProps(kafkaBroker), new StringSerializer(), new StringSerializer())
                        .createProducer();
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }

    protected AdminClient admin() {
        return admin;
    }

    protected Producer<String, String> producer() {
        return producer;
    }

    protected Source aSource() {
        return new Source("admin_mysql", "admin_catalogo", "categories");
    }
}
