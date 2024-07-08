# Kafka Connect

Kafka Connect es una herramienta de código abierto para la integración de datos entre Apache Kafka y otros sistemas de almacenamiento de datos. 

Kafka Connect se ejecuta como un clúster y coordina la importación y exportación de datos entre Kafka y otros sistemas.

## Introducción

Kafka Connect es una herramienta de código abierto para la integración de datos entre Apache Kafka y otros sistemas de almacenamiento de datos.

Kafka Connect se ejecuta como un clúster y coordina la importación y exportación de datos entre Kafka y otros sistemas.

## Instalación

Forma parte de la distribución de Apache Kafka, por que no es necesario instalarlo por separado.

## Configuración

Kafka Connect se configura mediante archivos de propiedades. Los archivos de configuración más importantes son:

- `connect-distributed.properties`: Archivo de configuración de Kafka Connect en modo distribuido.
- `connect-standalone.properties`: Archivo de configuración de Kafka Connect en modo independiente.
- `connect-log4j.properties`: Archivo de configuración de Log4j para Kafka Connect.
- `connect-runtime.properties`: Archivo de configuración del runtime Kafka Connect.
- `connect-file-source.properties`: Archivo de configuración de un conector de origen de archivos.
- `connect-file-sink.properties`: Archivo de configuración de un conector de destino de archivos.

## Comandos

### Iniciar Kafka Connect en modo independiente (standalone)

```shell
./kafka_2.13-*/bin/connect-standalone.sh config/connect-standalone.properties
```

### Gestión de conectores



Kafka Connect proporciona una API REST para la gestión de conectores en el puerto 8083 por defecto. 

http://localhost:8083


Algunos de los comandos más comunes son: 

Instalación de conectores:
- `GET /connector-plugins`: Obtiene una lista de los plugins de conectores disponibles.
Para instalar un conector, se puede utilizar la siguiente URL: http://localhost:8083/connector-plugins y enviar un JSON con la configuración del conector.


#### Conectores
- `GET /connectors`: Obtiene una lista de los conectores activos.
- `GET /connectors/{connector-name}`: Obtiene información sobre un conector específico.
- `POST /connectors`: Crea un nuevo conector.
  - Por ejemplo, para crear un conector, se puede utilizar la siguiente URL: http://localhost:8083/connectors 
  - Como es un POST, se debe enviar un JSON con la configuración del conector.
  - Por ejemplo, para crear un conector de archivos, se puede utilizar el siguiente JSON:
  ```json
  {
    "name": "file-source-connector",
    "config": {
      "connector.class": "FileStreamSource",
      "tasks.max": "1",
      "file": "/path/to/file.txt",
      "topic": "test-topic"
    }
  }
  ```
  - Para enviar el JSON, se puede utilizar una herramienta como `curl`:
  ```shell
    curl -X POST -H "Content-Type: application/json" --data @connector-config.json http://localhost:8083/connectors
    ```
  
- `PUT /connectors/{connector-name}/config`: Actualiza la configuración de un conector.
- `DELETE /connectors/{connector-name}`: Elimina un conector.
- `POST /connectors/{connector-name}/restart`: Reinicia un conector.
- `POST /connectors/{connector-name}/pause`: Pausa un conector.
- `POST /connectors/{connector-name}/resume`: Reanuda un conector.
- `GET /connectors/{connector-name}/status`: Obtiene el estado de un conector.
#### Tareas de conectores
- `GET /connectors/{connector-name}/tasks`: Obtiene información sobre las tareas de un conector.
- `GET /connectors/{connector-name}/tasks/{task-id}/status`: Obtiene el estado de una tarea de un conector.
- `PUT /connectors/{connector-name}/tasks/{task-id}/restart`: Reinicia una tarea de un conector.
- `POST /connectors/{connector-name}/tasks/{task-id}/pause`: Pausa una tarea de un conector.
- `POST /connectors/{connector-name}/tasks/{task-id}/resume`: Reanuda una tarea de un conector.

#### Configuración de conectores
- `GET /connectors/{connector-name}/config`: Obtiene la configuración de un conector.
- `PUT /connectors/{connector-name}/config`: Actualiza la configuración de un conector.
- `DELETE /connectors/{connector-name}/config`: Elimina la configuración de un conector.

#### Plugins
- `GET /connector-plugins`: Obtiene una lista de los plugins de conectores disponibles.
  - Por ejemplo, para obtener información sobre un plugin de conector específico, se puede utilizar la siguiente URL: http://localhost:8083/connector-plugins
- `GET /connector-plugins/{connector-class}`: Obtiene información sobre un plugin de conector específico.
- `POST /connector-plugins/validate`: Valida la configuración de un conector.
- `POST /connector-plugins/{connector-class}/config/validate`: Valida la configuración de un conector específico.
- `POST /connector-plugins/{connector-class}/config/validate`: Valida la configuración de un conector específico.
- `POST /connector-plugins/{connector-class}/config/validate`: Valida la configuración de un conector específico.
- `POST /connector-plugins/{connector-class}/config/validate`: Valida la configuración de un conector específico.

Para instalar un plugin de conector, se puede utilizar la siguiente URL: http://localhost:8083/connector-plugins/{connector-class} y enviar un JSON con la configuración del plugin.

### Iniciar Kafka Connect en modo distribuido (distributed)

```shell
