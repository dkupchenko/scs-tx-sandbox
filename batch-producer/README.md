# Spring Cloud Stream sandbox for Kafka batch producer

This is sandbox for [Producer Initiated Transactions in Spring Cloud Stream Kafka Applications](https://spring.io/blog/2023/09/28/producer-initiated-transactions-in-spring-cloud-stream-kafka-applications)

Kafka producer is transactional (see `transaction-id-prefix`is sent in `application.yml`)
```
spring.cloud:
  stream:
    kafka:
      binder:
        replication-factor: 1
        min-partition-count: 1
        transaction:
          transaction-id-prefix: sandbox-tx-
          producer.configuration:
            retries: 5
            acks: all
```

With `KafkaTransactionManager` and transactional call `PersonService.createAndSend()` produces these log entries:

```
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [PersonService.createAndSend]
[   scheduling-1] o.s.k.t.KafkaTransactionManager          : Initiating transaction commit
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@53
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@54
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@55
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@56
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] close(PT5S)
```

Behind the scenes, these are the sequence of events (see https://spring.io/blog/2023/09/28/producer-initiated-transactions-in-spring-cloud-stream-kafka-applications):

* As soon as the method annotated with `Transactional` is called, the transaction interceptor kicks in through the 
AOP proxying mechanism, and it starts a new transaction by using the custom `KafkaTransactionManager`.
* When the transaction manager begins the transaction, the resource used by the transaction manager - the transactional 
resource holder (AKA, producer obtained from the producer factory) - is bound to the transaction.
* When the method calls the `StreamBridge#send` method, the underlying `KafkaTemplate` will use the same transactional 
resource created by the custom `KafkaTransactionManager`. Since a transaction is already in progress, it does not start 
another transaction, but the publishing occurs on the same transactional producer.
* As it calls more send methods, it will not start new transactions. Instead, it publishes via the same producer resource 
used in the original transaction.
* When the method exits, the interceptor asks the transaction manager to commit the transaction if there is no error. 
If any of the send operations or anything else in the method throws an exception, the interceptor asks the transaction 
manager to roll back the transaction. These calls eventually hit the `KafkaResourceHolder` commit or rollback methods, 
which calls the Kafka producer to commit or rollback the transaction.

Without `KafkaTransactionManager` transaction manager configured:

```
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@74
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] close(PT5S)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@74
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] close(PT5S)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@74
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] close(PT5S)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@74
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] close(PT5S)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
```
