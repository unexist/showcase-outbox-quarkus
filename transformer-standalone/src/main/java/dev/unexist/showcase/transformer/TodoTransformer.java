/**
 * @package Showcase-Outbox-Quarkus
 *
 * @file Todo resource
 * @copyright 2020-2022 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.transformer;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.header.Headers;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

/*The class is configured and invoked when a change occurs on any outbox */
public class TodoTransformer<R extends ConnectRecord<R>> implements Transformation<R> {

    /**
     * This method is invoked when a change is made on the outbox schema.
     *
     * @param  sourceRecord  Source record to handle
     *
     * @return
     **/

    public R apply(R sourceRecord) {

        Struct kStruct = (Struct) sourceRecord.value();
        String databaseOperation = kStruct.getString("op");

        /* Handle only the creates */
        if ("c".equalsIgnoreCase(databaseOperation)) {

            /* Get the details */
            Struct after = (Struct) kStruct.get("after");
            String UUID = after.getString("uuid");
            String eventType = after.getString("eventtype").toLowerCase();
            String topic = eventType.toLowerCase();
            String payload = after.getString("payload");

            Headers headers = sourceRecord.headers();
            headers.addString("eventId", UUID);

            /* Build the event to be published */
            sourceRecord = sourceRecord.newRecord(topic, null,
                    Schema.STRING_SCHEMA, UUID, null, payload,
                    sourceRecord.timestamp(), headers);
        }

        return sourceRecord;
    }

    /**
     * Get new config
     *
     * @return A new {@link ConfigDef}
     **/

    public ConfigDef config() {
        return new ConfigDef();
    }

    /**
     * Close transformer
     **/

    public void close() {
    }

    /**
     * Configure transformer
     *
     * @param  configs  Config to use
     **/

    public void configure(Map<String, ?> configs) {
    }
}