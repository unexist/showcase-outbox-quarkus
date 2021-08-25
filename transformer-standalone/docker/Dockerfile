FROM debezium/connect
ENV DEBEZIUM_DIR=$KAFKA_CONNECT_PLUGINS_DIR/debezium-transformer

RUN mkdir $DEBEZIUM_DIR
COPY ../target/transformer-standalone-0.1.jar $DEBEZIUM_DIR