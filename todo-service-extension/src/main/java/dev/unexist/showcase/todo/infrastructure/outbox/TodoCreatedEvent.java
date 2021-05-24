/**
 * @package Quarkus-Outbox-Showcase
 *
 * @file Outbox event
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.unexist.showcase.todo.domain.todo.Todo;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.time.Instant;

public class TodoCreatedEvent implements ExportedEvent<String, JsonNode> {
    private static final String TYPE = "Todo";
    private static final String EVENT_TYPE = "todo_created";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final long todoId;
    private final JsonNode jsonNode;
    private final Instant timestamp;

    public TodoCreatedEvent(Instant createdAt, Todo todo) {
        this.todoId = todo.getId();
        this.timestamp = createdAt;
        this.jsonNode = MAPPER.valueToTree(todo);
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