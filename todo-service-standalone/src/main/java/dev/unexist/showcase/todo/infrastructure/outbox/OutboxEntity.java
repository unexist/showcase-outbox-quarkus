/**
 * @package Showcase-Outbox-Quarkus
 *
 * @file Outbox entity
 * @copyright 2020-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "outbox_standalone")
public class OutboxEntity {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "aggregateId")
    private Integer aggregateId;

    @Column(name = "eventType")
    private String eventType;

    @Column(name = "payload")
    private String payload;

    @Column(name = "createdOn")
    private Date createdOn;

    /**
     * Constructor
     **/

    protected OutboxEntity() {
    }

    /**
     * Constructor
     *
     * @param  uuid         UUID of the aggregate
     * @param  aggregateId  Id of the aggregate
     * @param  eventType    Event type of the aggregate
     * @param  payload      Payload of this event
     */

    public OutboxEntity(UUID uuid, Integer aggregateId, String eventType, String payload) {
        this.uuid = requireNonNull(uuid);
        this.aggregateId = requireNonNull(aggregateId);
        this.eventType = requireNonNull(eventType);
        this.payload = requireNonNull(payload);
    }
}