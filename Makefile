define JSON_CONNECTOR_STANDALONE
curl -X POST \
  http://localhost:8083/connectors/ \
  -H 'content-type: application/json' \
  -d '{
	"name": "todo-outbox-standalone-connector",
	"config": {
		"connector.class": "io.debezium.connector.postgresql.PostgresConnector",
		"tasks.max": "1",
		"database.hostname": "postgres",
		"database.port": "5432",
		"database.user": "unexist",
		"database.password": "password",
		"database.dbname": "showcase",
		"database.server.name": "pg-outbox-standalone-server",
		"tombstones.on.delete": "false",
		"table.whitelist": "public.outbox_standalone",
		"transforms": "outbox",
		"transforms.outbox.type": "dev.unexist.showcase.transformer.TodoTransformer"
	}
}'
endef
export JSON_CONNECTOR_STANDALONE

define JSON_CONNECTOR_EXTENSION
curl -X POST \
  http://localhost:8083/connectors/ \
  -H 'content-type: application/json' \
  -d '{
	"name": "todo-outbox-extension-connector",
	"config": {
		"connector.class": "io.debezium.connector.postgresql.PostgresConnector",
		"tasks.max": "1",
		"database.hostname": "postgres",
		"database.port": "5432",
		"database.user": "unexist",
		"database.password": "password",
		"database.dbname": "showcase",
		"database.server.name": "pg-outbox-extension-server",
		"tombstones.on.delete": "false",
		"table.whitelist": "public.outbox_extension",
		"table.field.event.schema.version": "1",
		"key.converter": "org.apache.kafka.connect.json.JsonConverter",
		"key.converter.schemas.enable": "false",
		"value.converter": "io.debezium.converters.CloudEventsConverter",
		"value.converter.data.serializer.type": "json",
		"value.converter.json.schemas.enable": "false",
		"tracing.with.context.field.only": "true",
		"transforms": "outbox",
		"transforms.outbox.type" : "io.debezium.transforms.outbox.EventRouter",
		"transforms.outbox.route.by.field": "type",
		"transforms.outbox.route.topic.replacement": "todo_created",
		"transforms.outbox.table.field.event.timestamp": "timestamp",
		"transforms.outbox.table.fields.additional.placement": "type:header:eventType"
	}
}' | jq .
endef
export JSON_CONNECTOR_EXTENSION

define JSON_TODO
curl -X 'POST' \
  'http://localhost:8080/todo' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "description": "string",
  "done": true,
  "dueDate": {
    "due": "2021-05-07",
    "start": "2021-05-07"
  },
  "title": "string"
}' | jq .
endef
export JSON_TODO


# Connector
connector-standalone-create:
	@echo $$JSON_CONNECTOR_STANDALONE | bash

connector-extension-create:
	@echo $$JSON_CONNECTOR_EXTENSION | bash

connector-create: connector-standalone-create connector-extension-create

connector-standalone-status:
	curl -X "GET" "http://localhost:8083/connectors/todo-outbox-standalone-connector/status" \
		-H 'content-type: application/json' | jq .

connector-extension-status:
	curl -X "GET" "http://localhost:8083/connectors/todo-outbox-extension-connector/status" \
		-H 'content-type: application/json' | jq .

connector-status: connector-standalone-status connector-extension-status

connector-standalone-restart:
	curl -X "POST" "http://localhost:8083/connectors/todo-outbox-standalone-connector/restart"

connector-extension-restart:
	curl -X "POST" "http://localhost:8083/connectors/todo-outbox-extension-connector/restart"

connector-restart: connector-standalone-restart connector-extension-restart

connector-standalone-delete:
	curl -X "DELETE" "http://localhost:8083/connectors/todo-outbox-standalone-connector"

connector-extension-delete:
	curl -X "DELETE" "http://localhost:8083/connectors/todo-outbox-extension-connector"

connector-delete: connector-standalone-delete connector-standalone-delete

connector-list:
	@curl -s "http://localhost:8083/connectors"| \
		jq '.[]'| \
		xargs -I{connector_name} curl -s "http://localhost:8083/connectors/"{connector_name}"/status"| \
		jq -c -M '[.name,.connector.state,.tasks[].state]|join(":|:")'| \
		column -s : -t| \
		sed 's/\"//g'| \
		sort

# Docker
docker-build-debezium:
	cd transformer-debezium

	docker build -t custom-connect -f docker/Dockerfile .

docker-build-standalone:
	cd transformer-standalone
	mvn clean package -f pom.xml

	docker build -t connect -f docker/Dockerfile .

docker-standalone:
	@docker-compose -f docker/docker-compose-standalone.yaml \
		-p debezium-standalone up

docker-extension:
	@docker-compose -f docker/docker-compose-extension.yaml \
		-p debezium-extension up

# Quarkus
quarkus-standalone:
	mvn -f todo-service-standalone/pom.xml quarkus:dev

quarkus-extension:
	mvn -f todo-service-extension/pom.xml quarkus:dev

# Tools
todo:
	@echo $$JSON_TODO | bash

kat-listen:
	kafkacat -t todo_created -b localhost:9092 -C

kat-test:
	kafkacat -t todo_created -b localhost:9092 -P

psql:
	PGPASSWORD=password psql -U unexist -h localhost -d showcase