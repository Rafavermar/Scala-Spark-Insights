#!/bin/bash
echo Creando topic1 con 10 particiones

. ./env.sh

# Creamos un consumer para el topic1: El comando kafka-console-consumer.sh nos permite consumir mensajes de un topic
posicion=--from-beginning
# posicion=--offset 0
# posicion=--partition 0
# posicion=--partition 1
# posicion="--offset earliest"
#$dir_kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic1 --from-beginning
$dir_kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic-2 $posicion --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer


# Opciones
# --bootstrap-server: Servidor de kafka
# --topic: Nombre del topic
# --partition: Numero de particion

# --from-beginning: Lee los mensajes desde el principio
# --max-messages: Lee un numero maximo de mensajes
# --earliest: Lee los mensajes desde el principio
# --offset: Lee los mensajes desde un offset
# --property print.key=true: Imprime la clave del mensaje
# --max-messages: Lee un numero maximo de mensajes
# --timeout-ms: Tiempo de espera para leer mensajes
# Serializador
# --key-deserializer: Serializador de la clave
# --value-deserializer: Serializador del valor
# Los posibles valores son:
# org.apache.kafka.common.serialization.StringDeserializer
# org.apache.kafka.common.serialization.ByteArrayDeserializer
# org.apache.kafka.common.serialization.IntegerDeserializer
# org.apache.kafka.common.serialization.LongDeserializer
# org.apache.kafka.common.serialization.DoubleDeserializer
# org.apache.kafka.common.serialization.FloatDeserializer
# org.apache.kafka.common.serialization.ShortDeserializer
# org.apache.kafka.common.serialization.ByteDeserializer
# org.apache.kafka.common.serialization.BytesDeserializer
# org.apache.kafka.common.serialization.BooleanDeserializer
# org.apache.kafka.common.serialization.ByteBufferDeserializer
# org.apache.kafka.common.serialization.UUIDDeserializer
# org.apache.kafka.common.serialization.DateDeserializer
# org.apache.kafka.common.serialization.TimeDeserializer
# org.apache.kafka.common.serialization.TimestampDeserializer
# org.apache.kafka.common.serialization.InstantDeserializer
# org.apache.kafka.common.serialization.LocalDateDeserializer
# org.apache.kafka.common.serialization.LocalDateTimeDeserializer
# org.apache.kafka.common.serialization.LocalTimeDeserializer
# org.apache.kafka.common.serialization.ZonedDateTimeDeserializer
# org.apache.kafka.common.serialization.DurationDeserializer
# org.apache.kafka.common.serialization.MonthDayDeserializer
# org.apache.kafka.common.serialization.YearDeserializer

## Formateo de mensajes
#--formatter : El nombre de la clase de formateo de mensajes para mostrar. (predeterminado: kafka.tools.DefaultMessageFormatter)
# --formatter-config : Propiedades para el formateador de mensajes. (predeterminado: ninguno)
# <String: config       Config properties file to initialize
# --property print.timestamp=true: Imprime la marca de tiempo del mensaje

# Ejemplos de formateo:
#$dir_kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic1 --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer

