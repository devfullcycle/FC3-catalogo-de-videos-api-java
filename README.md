<center>
  <p align="center">
    <img src="https://user-images.githubusercontent.com/20674439/158480514-a529b310-bc19-46a5-ac95-fddcfa4776ee.png" width="150"/>&nbsp;
    <img src="https://icon-library.com/images/java-icon-png/java-icon-png-15.jpg"  width="150" />
  </p>  
  <h1 align="center">🚀 Microserviço: Catálogo de Vídeos com Java</h1>
  <p align="center">
    Microserviço referente ao backend do Catálogo de Vídeos<br />
    Utilizando Clean Architecture, TDD e as boas práticas atuais de mercado
  </p>
</center>
<br />

## Ferramentas necessárias

- JDK 17
- IDE de sua preferência
- Docker

## Kafka Connect

### Debezium

Creating the debezium mysql user:
```sql
CREATE USER IF NOT EXISTS 'debezium'@'%' IDENTIFIED WITH mysql_native_password BY 'debezium';
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium'@'%';
FLUSH PRIVILEGES;
```

Creating the kafka connect connector for the Debezium:
```shell
curl --location --request PUT 'http://localhost:8083/connectors/admin-mysql-cdc/config' \
--header 'Content-Type: application/json' \
--data '{
    "topic.creation.default.replication.factor": 1,
    "topic.creation.default.partitions": 1,
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "key.converter": "org.apache.kafka.connect.json.JsonConverter",
    "key.converter.schemas.enable": "true",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter.schemas.enable": "true",
    "database.hostname": "mysql",
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "debezium",
    "database.server.id": "10000",
    "database.server.name": "adm_videos_mysql",
    "database.allowPublicKeyRetrieval": "true",
    "database.include.list": "adm_videos",
    "table.include.list": "adm_videos.categories,adm_videos.cast_members,adm_videos.genres,adm_videos.videos",
    "database.history.kafka.bootstrap.servers": "kafka:9092",
    "database.history.kafka.topic": "adm_videos.dbhistory",
    "include.schema.changes": "false",
    "schema.enable": "false",
    "topic.creation.adm_videos.include": "adm_videos_mysql\\.adm_videos\\.*",
    "topic.creation.adm_videos.partitions": 1
}'
```