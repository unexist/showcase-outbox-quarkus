.ONESHELL:
connect:
	curl -X POST \
	  http://localhost:8083/connectors/ \
	  -H 'content-type: application/json' \
	  -d '{ \
	   "name": "todo-outbox-connector", \
	   "config": { \
		  "connector.class": "io.debezium.connector.postgresql.PostgresConnector", \
		  "tasks.max": "1", \
		  "database.hostname": "postgres", \
		  "database.port": "5432", \
		  "database.user": "unexist", \
		  "database.password": "password", \
		  "database.dbname": "showcase", \
		  "database.server.name": "pg-outbox-server", \
		  "tombstones.on.delete": "false", \
		  "table.whitelist": "public.outbox", \
		  "transforms": "outbox", \
		  "transforms.outbox.type": "dev.unexist.showcase.transformer.TodoTransformer" \
	   } \
	}'

debezium:
	cd debezium-transformder
	mvn clean install

	docker build -t showcase-debezium-connect .