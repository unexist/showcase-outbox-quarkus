FROM strimzi/kafka:0.20.1-kafka-2.6.0

ENV KAFKA_CONNECT_PLUGIN_PATH=/opt/kafka/connect-plugins

ENV DEBEZIUM_DIR=$KAFKA_CONNECT_PLUGIN_PATH/debezium-connector-postgres

USER root

# create plugin directory that is accessible by kafka user
RUN mkdir $KAFKA_CONNECT_PLUGIN_PATH &&\
    chmod u+w $KAFKA_CONNECT_PLUGIN_PATH &&\
    chown kafka:root $KAFKA_CONNECT_PLUGIN_PATH

USER 1001

RUN cd $KAFKA_CONNECT_PLUGIN_PATH &&\
    curl -sfSL https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.6.1.Final/debezium-connector-postgres-1.6.1.Final-plugin.tar.gz | tar xz