/**
 * @package Showcase-Outbox-Quarkus
 *
 * @file Outbox domain service
 * @copyright 2020-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.outbox;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@ApplicationScoped
public class OutboxService {

    @Inject
    OutboxRepository outboxRepository;

    /**
     * Handle outbox event
     *
     * @param  event  {@link OutboxEvent} to handle
     **/

    @Transactional(REQUIRES_NEW)
    public void handleOutboxEvent(@Observes(during = TransactionPhase.AFTER_SUCCESS) OutboxEvent event) {
        UUID uuid = UUID.randomUUID();

        OutboxEntity entity = new OutboxEntity(uuid,
            event.getAggregateId(),
            event.getEventType(),
            event.getPayload());

        outboxRepository.add(entity);
    }
}
