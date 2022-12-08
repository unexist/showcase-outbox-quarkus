/**
 * @package Showcase-Outbox-Quarkus
 *
 * @file Outbox event
 * @copyright 2020-2022 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.unexist.showcase.todo.domain.todo.Todo;
import io.debezium.outbox.quarkus.ExportedEvent;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonSchema;

import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class TodoCreatedEvent implements ExportedEvent<String, JsonNode> {
    private static final String TYPE = "Todo";
    private static final String EVENT_TYPE = "todo_created";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final long todoId;
    private final JsonNode jsonNode;
    private final Instant timestamp;

    /**
     * Constructor
     *
     * @param  createdAt  Timestamp of creation
     * @param  todo       A {@link Todo}
     **/

    public TodoCreatedEvent(Instant createdAt, Todo todo) {
        this.todoId = todo.getId();
        this.timestamp = createdAt;

        JsonNode payload = MAPPER.valueToTree(todo);
        Schema jsonSchema = inferSchema(payload);

        JsonConverter jsonConverter = new JsonConverter();
        jsonConverter.configure(Collections.singletonMap("schemas.enable", true), false);

        ObjectNode schema = jsonConverter.asJsonSchema(jsonSchema);

        schema.put("name", "test");

        this.jsonNode = JsonSchema.envelope(schema, payload);
    }

    /**
     * Infer schema
     *
     * @param  jsonValue  Value to infer from
     *
     * @return
     **/

    private Schema inferSchema(JsonNode jsonValue) {
        switch (jsonValue.getNodeType()) {
            case NULL:
                return Schema.OPTIONAL_STRING_SCHEMA;
            case BOOLEAN:
                return Schema.BOOLEAN_SCHEMA;
            case NUMBER:
                if (jsonValue.isIntegralNumber()) {
                    return Schema.INT64_SCHEMA;
                } else {
                    return Schema.FLOAT64_SCHEMA;
                }
            case ARRAY:
                SchemaBuilder arrayBuilder = SchemaBuilder.array(
                        jsonValue.elements().hasNext()
                                ? inferSchema(jsonValue.elements().next())
                                : Schema.OPTIONAL_STRING_SCHEMA);

                return arrayBuilder.build();
            case OBJECT:
                SchemaBuilder structBuilder = SchemaBuilder.struct();
                Iterator<Map.Entry<String, JsonNode>> it = jsonValue.fields();

                while (it.hasNext()) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    structBuilder.field(entry.getKey(), inferSchema(entry.getValue()));
                }

                return structBuilder.build();
            case STRING:
                return Schema.STRING_SCHEMA;
            case BINARY:
            case MISSING:
            case POJO:
            default:
                return null;
        }
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(todoId);
    }

    @Override
    public String getAggregateType() {
        return TYPE;
    }

    @Override
    public JsonNode getPayload() {
        return jsonNode;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
