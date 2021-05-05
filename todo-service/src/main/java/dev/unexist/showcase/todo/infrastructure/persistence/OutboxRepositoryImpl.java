/**
 * @package Quarkus-Kubernetes-Showcase
 *
 * @file Outbox repository
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.persistence;

import dev.unexist.showcase.todo.infrastructure.outbox.OutboxEntity;
import dev.unexist.showcase.todo.infrastructure.outbox.OutboxRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class OutboxRepositoryImpl implements OutboxRepository {

    @Inject
    EntityManager entityManager;

    @Override
    public boolean add(OutboxEntity outbox) {
        this.entityManager.persist(outbox);

        return true;
    }
}