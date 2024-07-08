# Kafka SQl (KSQL)

Kafka SQL es un motor de procesamiento de eventos en tiempo real que permite a los usuarios escribir consultas SQL sobre
los flujos de datos en Kafka.

KSQL proporciona una interfaz de línea de comandos (CLI) y una API REST para interactuar con el motor de procesamiento
de eventos.

KSQL se puede utilizar para realizar consultas, transformaciones y análisis de datos en tiempo real sobre los flujos de
datos en Kafka.

## Instalación


```shell
curl -sq http://ksqldb-packages.s3.amazonaws.com/archive/0.29/archive.key | gpg --import
curl http://ksqldb-packages.s3.amazonaws.com/archive/0.29/confluent-ksqldb-0.29.0.tar.gz --output confluent-ksqldb-0.29.0.tar.gz
curl http://ksqldb-packages.s3.amazonaws.com/archive/0.29/confluent-ksqldb-0.29.0.tar.gz.asc --output confluent-ksqldb-0.29.0.tar.gz.asc
gpg --verify confluent-ksqldb-0.29.0.tar.gz.asc confluent-ksqldb-0.29.0.tar.gz
tar -xf confluent-ksqldb-0.29.0.tar.gz -C ../confluent
``` 


https://ksqldb.io/quickstart-standalone-tarball.html#quickstart-content

KSQL se puede instalar como una aplicación independiente o como parte de la distribución de Confluent Platform.

Como una aplicación independiente, KSQL se puede instalar mediante el archivo ZIP que se puede descargar desde el sitio
web de Confluent:


Para instalar KSQL como parte de la distribución de Confluent Platform, se puede utilizar el siguiente comando:

```shell
confluent-hub install confluentinc/ksql
```


## Iniciar KSQL

En nuestro caso vamos a utilizar la versión standalone de KSQL, instalada en `oss/confluent-ksqldb-0.29.0` que se puede iniciar con el siguiente comando:

```shell
../confluent/confluent-ksqldb-0.29.0/bin/ksql-server-start ../confluent/confluent-ksqldb-0.29.0/etc/ksqldb/ksql-server.properties
```

## Interfaz de línea de comandos (CLI)

KSQL proporciona una interfaz de línea de comandos (CLI) para interactuar con el motor de procesamiento de eventos.

La CLI de KSQL se puede iniciar con el siguiente comando:

```shell
../confluent/confluent-ksqldb-0.29.0/bin/ksql
```


#### ksql-datagen

Para generar datos de prueba, se puede utilizar el comando `ksql-datagen`:

```
quickstart=pageviews -> Crea un flujo de datos de prueba llamado `pageviews` 
format=json  -> Formato de los datos json, por defecto es avro pero necesita el Schema Registry
topic=pageviews  -> Nombre del topic de Kafka
maxInterval=100 -> Intervalo máximo de tiempo entre eventos
```

```shell
../confluent/confluent-ksqldb-0.29.0/bin/ksql-datagen quickstart=pageviews format=json topic=pageviews maxInterval=1000
```


Una vez iniciada la CLI de KSQL, se puede interactuar con el motor de procesamiento de eventos mediante comandos SQL.

Por ejemplo, se puede crear un flujo de datos en KSQL con el siguiente comando:

```sql
CREATE STREAM pageviews (viewtime BIGINT, userid VARCHAR, pageid VARCHAR) WITH (KAFKA_TOPIC='pageviews', VALUE_FORMAT='JSON');
```

## Consultas SQL

KSQL permite a los usuarios escribir consultas SQL sobre los flujos de datos en Kafka.

Por ejemplo, se puede realizar una consulta SQL para contar el número de eventos en un flujo de datos:

```sql
SELECT COUNT(*) FROM pageviews;
```

## Transformaciones

KSQL permite a los usuarios realizar transformaciones en los flujos de datos en tiempo real.

Por ejemplo, se puede realizar una transformación para filtrar los eventos en un flujo de datos:

```sql
CREATE STREAM
    pageviews
WITH (KAFKA_TOPIC='pageviews', VALUE_FORMAT='JSON') AS
    SELECT
        viewtime,
        userid,
        pageid
    FROM
        pageviews
    WHERE
        userid = 'alice';
```



## API REST

KSQL proporciona una API REST para interactuar con el motor de procesamiento de eventos.

La API REST de KSQL se puede utilizar para enviar consultas SQL al motor de procesamiento de eventos y obtener los resultados.



