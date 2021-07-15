/**
 * @package Quarkus-Kubernetes-Showcase
 *
 * @file Todo base class
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
public class TodoBase {

    @NotBlank
    @Column
    private String title;

    @NotBlank
    @Column
    private String description;

    @Column
    private Boolean done;

    @Embedded
    private DueDate dueDate;

    /**
     * Get title of the entry
     *
     * @return
     *      Title of the entry
     **/

    public String getTitle() {
        return title;
    }

    /**
     * Set title of the entry
     *
     * @param title
     *          Title of the entry
     **/

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get description of entry
     *
     * @return
     *      Description of the entry
     **/

    public String getDescription() {
        return description;
    }

    /**
     * Set description of the entry
     *
     * @param description
     *          Description of the entry
     **/

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get done state of entry
     *
     * @return
     *      Done state of the entry
     **/

    public Boolean getDone() {
        return done;
    }

    /**
     * Set done state of entry
     *
     * @param done
     *          Done state of the entry
     **/

    public void setDone(Boolean done) {
        this.done = done;
    }

    /**
     * Get due state of the entry
     *
     * @return
     *          Due state of the entry
     **/

    public DueDate getDueDate() {
        return dueDate;
    }

    /**
     * Set due date of the entry
     *
     * @param dueDate
     *          Due date of the entry
     **/

    public void setDueDate(DueDate dueDate) {
        this.dueDate = dueDate;

        if (null != dueDate.getStart() && null != dueDate.getDue()) {
            this.done = dueDate.getStart().isBefore(dueDate.getDue());
        }
    }
}
