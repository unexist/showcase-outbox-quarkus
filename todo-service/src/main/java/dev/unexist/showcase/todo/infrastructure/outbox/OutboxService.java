/**
 * @package Quarkus-Outbox-Showcase
 *
 * @file Outbox domain service
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.outbox;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class OutboxService {

    @Inject
    private OutboxRepository outboxRepository;

    public void handleOutboxEvent(@Observes(during=TransactionPhase.AFTER_SUCCESS) OutboxEvent event) {
        UUID uuid = UUID.randomUUID();

        OutboxEntity entity = new OutboxEntity(uuid,
            event.getAggregateId(),
            event.getEventType(),
            event.getPayload());

        outboxRepository.add(entity);
    }
}
