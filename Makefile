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
		"schema.whitelist" : "todos,todo,todo_created",
		"table.whitelist": "public.outbox_extension",
		"transforms": "outbox",
		"transforms.outbox.type" : "io.debezium.transforms.outbox.EventRouter",
		"transforms.outbox.route.by.field": "type",
		"transforms.outbox.route.topic.replacement": "${routedByValue}",
		"transforms.outbox.table.field.event.timestamp": "timestamp",
		"transforms.outbox.table.fields.additional.placement": "type:header:eventType"
	}
}'
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
}'
endef
export JSON_TODO

# Build
debezium:
	cd debezium-transformer
	mvn clean install

	docker build -t showcase-debezium-connect .

# Connector
connector-create-standalone:
	@echo $$JSON_CONNECTOR_STANDALONE | bash

connector-status-standalone:
	curl -X "GET" http://localhost:8083/connectors/todo-outbox-standalone-connector/status \
		-H 'content-type: application/json' | jq .

connector-status-extension:
	curl -X "GET" http://localhost:8083/connectors/todo-outbox-extension-connector/status \
		-H 'content-type: application/json' | jq .

connector-create-extension:
	@echo $$JSON_CONNECTOR_EXTENSION | bash

connector-create: connector-create-standalone connector-create-extension

connector-list:
	@curl -s "http://localhost:8083/connectors"| \
		jq '.[]'| \
		xargs -I{connector_name} curl -s "http://localhost:8083/connectors/"{connector_name}"/status"| \
		jq -c -M '[.name,.connector.state,.tasks[].state]|join(":|:")'| \
		column -s : -t| \
		sed 's/\"//g'| \
		sort

connector-delete:
	curl -X "DELETE" "http://localhost:8083/connectors/todo-outbox-standalone-connector"
	curl -X "DELETE" "http://localhost:8083/connectors/todo-outbox-extension-connector"

# Tools
todo:
	@echo $$JSON_TODO | bash


docker:
	docker-compose up

listen-kt:
	kt consume -topic todo_created

listen-cat:
	kafkacat -t todo_created -b localhost:9092 -C

test-cat:
	kafkacat -t todo_created -b localhost:9092 -P

psql:
	PGPASSWORD=password psql -U unexist -h localhost -d showcase