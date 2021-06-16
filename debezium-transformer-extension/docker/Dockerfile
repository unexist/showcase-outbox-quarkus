FROM strimzi/kafka:0.20.0-kafka-2.6.0
ENV KAFKA_CONNECT_PLUGIN_PATH=/tmp/connect-plugins

RUN mkdir $KAFKA_CONNECT_PLUGIN_PATH &&\
    cd $KAFKA_CONNECT_PLUGIN_PATH &&\
    curl -sfSL https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.6.0.Beta2/debezium-connector-postgres-1.6.0.Beta2-plugin.tar.gz | tar xz