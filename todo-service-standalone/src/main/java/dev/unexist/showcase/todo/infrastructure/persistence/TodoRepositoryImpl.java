/**
 * @package Quarkus-Kubernetes-Showcase
 *
 * @file Todo repository
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.persistence;

import dev.unexist.showcase.todo.domain.todo.Todo;
import dev.unexist.showcase.todo.domain.todo.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TodoRepositoryImpl implements TodoRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoRepositoryImpl.class);

    @Inject
    EntityManager entityManager;

    @Override
    public boolean add(Todo todo) {
        this.entityManager.persist(todo);

        return true;
    }

    @Override
    public boolean update(Todo todo) {
        this.entityManager.persist(todo);

        return true;
    }

    @Override
    public boolean deleteById(int id) {
        findById(id).ifPresent(todo -> this.entityManager.remove(todo));

        return true;
    }

    @Override
    public List<Todo> getAll() {
        return this.entityManager.createNamedQuery(Todo.FIND_ALL, Todo.class)
                .getResultList();
    }

    @Override
    public Optional<Todo> findById(int id) {
        return this.entityManager.createNamedQuery(Todo.FIND_BY_ID, Todo.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }
}