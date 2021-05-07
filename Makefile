define JSON_CONNECTOR
curl -X POST \
  http://localhost:8083/connectors/ \
  -H 'content-type: application/json' \
  -d '{
	"name": "todo-outbox-connector",
	"config": {
		"connector.class": "io.debezium.connector.postgresql.PostgresConnector",
		"tasks.max": "1",
		"database.hostname": "postgres",
		"database.port": "5432",
		"database.user": "unexist",
		"database.password": "password",
		"database.dbname": "showcase",
		"database.server.name": "pg-outbox-server",
		"tombstones.on.delete": "false",
		"table.whitelist": "public.outbox",
		"transforms": "outbox",
		"transforms.outbox.type": "dev.unexist.showcase.transformer.TodoTransformer"
	}
}'
endef
export JSON_CONNECTOR

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

connector:
	@echo $$JSON_CONNECTOR | bash

todo:
	@echo $$JSON_TODO | bash

list:
	curl http://localhost:8083/connectors/ | jq .

debezium:
	cd debezium-transformer
	mvn clean install

	docker build -t showcase-debezium-connect .

docker:
	docker-compose up

listen:
	kt consume -topic todo_created

