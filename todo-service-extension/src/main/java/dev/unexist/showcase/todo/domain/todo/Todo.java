/**
 * @package Quarkus-Kubernetes-Showcase
 *
 * @file Todo class and aggregate root
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Todos")
@NamedQueries({
        @NamedQuery(name = Todo.FIND_ALL, query = "SELECT t FROM Todo t"),
        @NamedQuery(name = Todo.FIND_BY_ID, query = "SELECT t FROM Todo t WHERE t.id = :id")
})
public class Todo extends TodoBase {
    public static final String FIND_ALL = "Todo.findAll";
    public static final String FIND_BY_ID = "Todo.findById";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    /**
     * Constructor
     **/

    public Todo() {
    }


    /**
     * Constructor
     *
     * @param base
     *          Base entry
     **/

    public Todo(final TodoBase base) {
        this.update(base);
    }

    /**
     * Update values from base
     *
     * @param base
     *          Todo base class
     **/

    public void update(final TodoBase base) {
        this.setDueDate(base.getDueDate());
        this.setTitle(base.getTitle());
        this.setDescription(base.getDescription());
        this.setDone(base.getDone());
    }

    /**
     * Get id of entry
     *
     * @return
     *          Id of the entry
     **/

    public int getId() {
        return id;
    }

    /**
     * Set id of entry
     *
     * @param id
     *          Id of the entry
     **/

    public void setId(int id) {
        this.id = id;
    }
}
