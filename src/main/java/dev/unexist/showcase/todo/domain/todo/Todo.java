/**
 * @package Quarkus-Kubernetes-Showcase
 *
 * @file Todo class and aggregate root
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

public class Todo extends TodoBase {
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
