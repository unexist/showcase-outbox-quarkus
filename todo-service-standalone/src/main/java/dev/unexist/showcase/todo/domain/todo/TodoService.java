/**
 * @package Showcase-Outbox-Quarkus
 *
 * @file Todo domain service
 * @copyright 2020-2022 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.unexist.showcase.todo.infrastructure.outbox.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TodoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoService.class);

    @Inject
    TodoRepository todoRepository;

    @Inject
    Event<OutboxEvent> eventHandler;

    /**
     * Create new {@link Todo} entry and store it in repository
     *
     * @param  base  A {@link TodoBase} entry
     *
     * @return Either id of the entry on success; otherwise {@code -1}
     **/

    @Transactional
    public int create(TodoBase base) {
        Todo todo = new Todo(base);

        int retval = this.todoRepository.add(todo) ? todo.getId() : -1;

        OutboxEvent event = new OutboxEvent();

        event.setAggregateId(todo.getId());
        event.setEventType("todo_created");

        try {
            ObjectMapper mapper = new ObjectMapper();

            event.setPayload(mapper.writeValueAsString(todo));
        } catch (JsonProcessingException jpe) {
            LOGGER.warn("Json error: {}", jpe.getMessage(), jpe);

            retval = -1;
        }

        this.eventHandler.fire(event);

        return retval;
    }

    /**
     * Update {@link Todo} at with given id
     *
     * @param  id    Id to update
     * @param  base  Values for the entry
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    public boolean update(int id, TodoBase base) {
        Optional<Todo> todo = this.findById(id);
        boolean ret = false;

        if (todo.isPresent()) {
            todo.get().update(base);

            ret = this.todoRepository.update(todo.get());
        }

        return ret;
    }

    /**
     * Delete {@link Todo} with given id
     *
     * @param  id  Id to delete
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    public boolean delete(int id) {
        return this.todoRepository.deleteById(id);
    }

    /**
     * Get all {@link Todo} entries
     *
     * @return List of all {@link Todo}; might be empty
     **/

    public List<Todo> getAll() {
        return this.todoRepository.getAll();
    }

    /**
     * Find {@link Todo} by given id
     *
     * @param  id  Id to look for
     *
     * @return A {@link Optional} of the entry
     **/

    public Optional<Todo> findById(int id) {
        return this.todoRepository.findById(id);
    }
}
