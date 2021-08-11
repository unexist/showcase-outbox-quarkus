/**
 * @package Quarkus-Outbox-Showcase
 *
 * @file Outbox event
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.outbox;

public class OutboxEvent {
    private Integer aggregateId;
    private String eventType;
    private String payload;

    /**
     * Get aggregate id
     *
     * @return Id of the aggregate
     **/

    public Integer getAggregateId() {
        return aggregateId;
    }

    /**
     * Set aggregate id
     *
     * @param  aggregateId  Aggregate id
     **/

    public void setAggregateId(Integer aggregateId) {
        this.aggregateId = aggregateId;
    }

    /**
     * Get event type
     *
     * @return Type of event
     **/

    public String getEventType() {
        return eventType;
    }

    /**
     * Set event type
     *
     * @param  eventType  Event type
     **/

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Get payload
     *
     * @return Payload of the event
     **/

    public String getPayload() {
        return payload;
    }

    /**
     * Set payload
     *
     * @param  payload  Payload to set
     **/

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
