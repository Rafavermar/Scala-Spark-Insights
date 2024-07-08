# Kafka directories

- `bin`: contains kafka scripts.
    - `kafka-topics.sh`: Script para crear, listar y describir tópicos.
    - `kafka-console-producer.sh`: Script para crear un productor en modo consola.
    - `kafka-console-consumer.sh`: Script para crear un consumidor en modo consola.
    - `kafka-server-start.sh`: Script para iniciar el servidor de Kafka.
    - `kafka-server-stop.sh`: Script para detener el servidor de Kafka.
    - `kafka-run-class.sh`: Script para ejecutar una clase de Java.
    - `kafka-configs.sh`: Script para modificar la configuración de Kafka.
    - `kafka-acls.sh`: Script para administrar listas de control de acceso.
    - `kafka-broker-api-versions.sh`: Script para mostrar las versiones de la API del broker.
    - `kafka-consumer-groups.sh`: Script para administrar grupos de consumidores.
    - `kafka-consumer-offset-checker.sh`: Script para verificar los desplazamientos de los consumidores.
    - `kafka-consumer-perf-test.sh`: Script para realizar pruebas de rendimiento de los consumidores.
    - `kafka-log-dirs.sh`: Script para mostrar los directorios de registro de Kafka.
    - `kafka-mirror-maker.sh`: Script para crear un espejo de Kafka.
- `config`: Contiene los archivos de configuración de Kafka.
    - `zookeeper.properties`: Archivo de configuración de Zookeeper.
    - `server.properties`: Archivo de configuración del servidor de Kafka.
    - `producer.properties`: Archivo de configuración del productor de Kafka.
    - `consumer.properties`: Archivo de configuración del consumidor de Kafka.
    - `connect-distributed.properties`: Archivo de configuración de Kafka Connect.
    - `connect-standalone.properties`: Archivo de configuración de Kafka Connect en modo independiente.
    - `connect-log4j.properties`: Archivo de configuración de Log4j para Kafka Connect.
    - `connect-runtime.properties`: Archivo de configuración del runtime Kafka Connect.
- `libs`: Contiene las bibliotecas de Kafka.
- `logs`: Contiene los registros de Kafka.
- `site-docs`: Contiene la documentación de Kafka.
- `zookeeper`: Contiene los archivos de Zookeeper.
- `zookeeper-data`: Contiene los datos de Zookeeper.
- `zookeeper-logs`: Contiene los registros de Zookeeper.

# Administración de Kafka

## Introducción

Apache Kafka es una plataforma de transmisión distribuida que se utiliza para publicar y suscribir flujos de registros, similar a una cola de mensajes o un sistema de mensajería. Es rápido, escalable y duradero.

## Instalación

### Descarga

```shell
wget https://downloads.apache.org/kafka/2.8.0/kafka_2.13-3.7.0.tgz
```

### Descompresión

```shell
tar -xvzf kafka_2.13-3.7.0.tgz
```

### Inicio

```shell
cd kafka_2.13-3.7.0
```
#### Zookeeper
Vamos a iniciar el servidor de Zookeeper, ya que Kafka depende de él.
```shell
bin/zookeeper-server-start.sh config/zookeeper.properties
```
#### Kafka broker
Vamos a iniciar el servidor de Kafka.

```shell
bin/kafka-server-start.sh config/server.properties
```

## Comandos

### Crear un tópico

```shell
bin/kafka-topics.sh --create --topic topics1 --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### Listar tópicos

```shell
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### Describir un tópico

```shell
bin/kafka-topics.sh --describe --topic topics1 --bootstrap-server localhost:9092
```

### Creación de mensajes

```shell
bin/kafka-console-producer.sh --topic topics1 --bootstrap-server localhost:9092
```
Tras ejecutar el comando anterior, se abrirá un terminal en el que podremos escribir los mensajes que queramos enviar al tópico `topics1`.

Si queremos enviar un mensaje con clave, podemos hacerlo de la siguiente manera:

```shell
bin/kafka-console-producer.sh --topic topics1 --bootstrap-server localhost:9092 --property "parse.key=true" --property "key.separator=:"
```
Tras ejecutar el comando anterior, se abrirá un terminal en el que podremos escribir los mensajes que queramos enviar al tópico `topics1`. En este caso, los mensajes deben tener una clave y un valor separados por `:`.

```shell
key1:valor1
key2:valor2
```

### Consumir mensajes

```shell
bin/kafka-console-consumer.sh --topic topics1 --from-beginning --bootstrap-server localhost:9092
```

### Borrar un tópico

```shell
bin/kafka-topics.sh --delete --topic topics1 --bootstrap-server localhost:9092
```

## Grupos de consumidores

Un grupo de consumidores es un conjunto de consumidores que trabajan juntos para consumir mensajes de uno o más tópicos. Cada grupo de consumidores tiene un identificador único y cada consumidor dentro del grupo tiene un identificador único.

### Crear un grupo de consumidores

La creación de un grupo de consumidores se realiza automáticamente cuando un consumidor se suscribe a un tópico por primera vez.

```shell
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --create --group group1
```

### Listar grupos de consumidores

```shell
bin/kafka-consumer-groups.sh --list --bootstrap-server localhost:9092
```

### Describir un grupo de consumidores

```shell
bin/kafka-consumer-groups.sh --describe --group group1 --bootstrap-server localhost:9092
```

### Eliminar un grupo de consumidores

```shell
bin/kafka-consumer-groups.sh --delete --group group1 --bootstrap-server localhost:9092
```


