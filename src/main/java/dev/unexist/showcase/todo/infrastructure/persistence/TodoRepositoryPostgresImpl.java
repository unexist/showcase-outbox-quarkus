package dev.unexist.showcase.todo.infrastructure.persistence;

import dev.unexist.showcase.todo.domain.todo.Todo;
import dev.unexist.showcase.todo.domain.todo.TodoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Named("todo_pg")
public class TodoRepositoryPostgresImpl implements TodoRepository {

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
