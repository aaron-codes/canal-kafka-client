/*
 * *
 *
 *     Created by OuYangX.
 *     Copyright (c) 2018, ouyangxian@gmail.com All Rights Reserved.
 *
 * /
 */

package com.zyxr.bi.business.process;

import com.zyxr.bi.business.model.event.EventChannel;
import com.zyxr.bi.business.model.event.EventEntry;
import com.zyxr.bi.business.process.base.AbstractCanalClient;
import com.zyxr.bi.business.util.SystemConfig;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Objects;
import java.util.Properties;


/**
*
* 
* @author: OuYangX
* @date: 2018/1/27 上午11:33
**/

public class KafkaCanalClient extends AbstractCanalClient {

    protected String kafkaAddress;
    protected KafkaProducer<Integer, String> producer;

    public KafkaCanalClient(String destination) {
        super(destination);
        this.kafkaAddress = SystemConfig.getProperty(destination.concat(".").concat("kafka.address"));
    }

    @Override
    protected void prepare() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, destination);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    @Override
    protected void destroy() {
        producer.flush();
        producer.close();
    }

    @Override
    protected void after(EventEntry data) {

        logger.debug("canal source data:{}", data.toString());

        if (!Objects.isNull(data)) {
            producer.send(new ProducerRecord(EventChannel.DB2HDFS.name(), data.toString()));
            logger.debug("sent message-->topic:{}, text:{}", EventChannel.DB2HDFS.name(), data);
        }
    }

    public static void main(String[] args) {

        final KafkaCanalClient kafkaCanalClient = new KafkaCanalClient("InvestDataSource");
        kafkaCanalClient.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    logger.info("## stop the canal kafka client");
                    kafkaCanalClient.stop();
                } catch (Throwable e) {
                    logger.warn("##something goes wrong when stopping canal:", e);
                } finally {
                    logger.info("## canal kafka client is down.");
                }
            }

        });
    }

}
