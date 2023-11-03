# Spring Cloud Stream sandbox for consume-process-produce pattern

This is sandbox for [Synchronizing with External Transaction Managers in Spring Cloud Stream Kafka Applications](https://spring.io/blog/2023/10/04/synchronizing-with-external-transaction-managers-in-spring-cloud-stream)

## Scenario 1: Producer-initiated transactions

Kafka producer is transactional (see `transaction-id-prefix`is sent in `application.yml`)
```
spring.cloud:
  stream:
    kafka:
      binder:
        transaction:
          transaction-id-prefix: sandbox-tx-
```

Without `KafkaTransactionManager` bean defined transactional call `PersonService.process()` produces these log entries:

```
[container-0-C-1] o.s.k.t.KafkaTransactionManager          : Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[container-0-C-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@3ef03058] beginTransaction()
[container-0-C-1] o.s.k.t.KafkaTransactionManager          : Created Kafka transaction on producer [CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@3ef03058]]
[container-0-C-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [info.kupchenko.sandbox.stream.txdemo.function.service.PersonService.process]
[container-0-C-1] i.k.s.s.t.f.service.PersonService        : [BEGIN] Person(id=null, name=John Smith - 12)
[container-0-C-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [org.springframework.data.jpa.repository.support.SimpleJpaRepository.save]
[container-0-C-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [org.springframework.data.jpa.repository.support.SimpleJpaRepository.save]
[container-0-C-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(topic=demo.tx.out.send-event, partition=null, headers=RecordHeaders(headers = [RecordHeader(key = contentType, value = [97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 106, 115, 111, 110]), RecordHeader(key = target-protocol, value = [107, 97, 102, 107, 97]), RecordHeader(key = spring_json_header_types, value = [123, 34, 99, 111, 110, 116, 101, 110, 116, 84, 121, 112, 101, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 44, 34, 116, 97, 114, 103, 101, 116, 45, 112, 114, 111, 116, 111, 99, 111, 108, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 125])], isReadOnly = false), key=null, value=[B@3540d677, timestamp=null)
[container-0-C-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@3ef03058] send(ProducerRecord(topic=demo.tx.out.send-event, partition=null, headers=RecordHeaders(headers = [RecordHeader(key = contentType, value = [97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 106, 115, 111, 110]), RecordHeader(key = target-protocol, value = [107, 97, 102, 107, 97]), RecordHeader(key = spring_json_header_types, value = [123, 34, 99, 111, 110, 116, 101, 110, 116, 84, 121, 112, 101, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 44, 34, 116, 97, 114, 103, 101, 116, 45, 112, 114, 111, 116, 111, 99, 111, 108, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 125])], isReadOnly = false), key=null, value=[B@3540d677, timestamp=null))
[container-0-C-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(topic=demo.tx.out.send-event, partition=null, headers=RecordHeaders(headers = [RecordHeader(key = contentType, value = [97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 106, 115, 111, 110]), RecordHeader(key = target-protocol, value = [107, 97, 102, 107, 97]), RecordHeader(key = spring_json_header_types, value = [123, 34, 99, 111, 110, 116, 101, 110, 116, 84, 121, 112, 101, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 44, 34, 116, 97, 114, 103, 101, 116, 45, 112, 114, 111, 116, 111, 99, 111, 108, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 125])], isReadOnly = true), key=null, value=[B@3540d677, timestamp=null)
[container-0-C-1] i.k.s.s.t.f.service.PersonService        : [END] Person(id=7, name=John Smith - 13)
[container-0-C-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [info.kupchenko.sandbox.stream.txdemo.function.service.PersonService.process]
2023-11-03T10:22:16.458+03:00 TRACE 133821 --- [container-0-C-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@3ef03058] sendOffsetsToTransaction({demo.tx.out.send-person-0=OffsetAndMetadata{offset=136, leaderEpoch=null, metadata=''}}, GroupMetadata(groupId = ackPersonGroup, generationId = 41, memberId = consumer-ackPersonGroup-2-83345ab4-12bc-46d0-a804-a2e944f814fc, groupInstanceId = ))
2023-11-03T10:22:16.459+03:00 TRACE 133821 --- [er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(topic=demo.tx.out.send-event, partition=null, headers=RecordHeaders(headers = [RecordHeader(key = contentType, value = [97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 106, 115, 111, 110]), RecordHeader(key = target-protocol, value = [107, 97, 102, 107, 97]), RecordHeader(key = spring_json_header_types, value = [123, 34, 99, 111, 110, 116, 101, 110, 116, 84, 121, 112, 101, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 44, 34, 116, 97, 114, 103, 101, 116, 45, 112, 114, 111, 116, 111, 99, 111, 108, 34, 58, 34, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114, 105, 110, 103, 34, 125])], isReadOnly = true), key=null, value=[B@3540d677, timestamp=null), metadata: demo.tx.out.send-event-0@44
2023-11-03T10:22:16.461+03:00 DEBUG 133821 --- [container-0-C-1] o.s.k.t.KafkaTransactionManager          : Initiating transaction commit
2023-11-03T10:22:16.461+03:00 DEBUG 133821 --- [container-0-C-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@3ef03058] commitTransaction()
2023-11-03T10:22:16.462+03:00 TRACE 133821 --- [container-0-C-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@3ef03058] close(PT5S)
```

If exception happen:

```
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [PersonService.createAndSend]
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [SimpleJpaRepository.findAll]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [SimpleJpaRepository.findAll]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [SimpleJpaRepository.save]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [SimpleJpaRepository.save]
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [EXCEPTION] Some error is happen
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [PersonService.createAndSend] after exception: java.lang.RuntimeException
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] abortTransaction()
[   scheduling-1] o.a.k.clients.producer.KafkaProducer     : [Producer clientId=producer-sandbox-tx-0, transactionalId=sandbox-tx-0] Aborting incomplete transaction
[er-sandbox-tx-0] o.s.k.support.LoggingProducerListener    : Exception thrown when sending a message with key='null' and payload='byte[34]' to topic demo.tx.out.send-person:

org.apache.kafka.common.errors.TransactionAbortedException: Failing batch since transaction was aborted

[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Failed to send: ProducerRecord(...)

org.apache.kafka.common.errors.TransactionAbortedException: Failing batch since transaction was aborted

[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@4fdeb921] close(PT5S)
```