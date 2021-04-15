/**
 * @package Quarkus-Kubernetes-Showcase
 *
 * @file Todo service and domain service
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TodoService {

    @Inject
    @Named("todo_list")
    TodoRepository todoRepository;

    /**
     * Create new {@link Todo} entry and store it in repository
     *
     * @param base
     *          A {@link TodoBase} entry
     * @return
     *          Either {@code true} on success; otherwise {@code false}
     **/

    public boolean create(TodoBase base) {
        Todo todo = new Todo(base);

        return this.todoRepository.add(todo);
    }

    /**
     * Update {@link Todo} at with given id
     *
     * @param id
     *          Id to update
     * @param base
     *          Values for the entry
     * @return
     *          Either {@code true} on success; otherwise {@code false}
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
     * @param id
     *          Id to delete
     * @return
     *          Either {@code true} on success; otherwise {@code false}
     **/

    public boolean delete(int id) {
        return this.todoRepository.deleteById(id);
    }

    /**
     * Get all {@link Todo} entries
     *
     * @return
     *          List of all {@link Todo}; might be empty
     **/

    public List<Todo> getAll() {
        return this.todoRepository.getAll();
    }

    /**
     * Find {@link Todo} by given id
     *
     * @param id
     *          Id to look for
     * @return
     *          A {@link Optional} of the entry
     **/

    public Optional<Todo> findById(int id) {
        return this.todoRepository.findById(id);
    }
}
