


# Zookeeper Shell

La consola de Zookeeper es una interfaz de línea de comandos que se utiliza para interactuar con un servidor de Zookeeper. Permite a los usuarios realizar operaciones como crear, leer, actualizar y eliminar nodos, así como establecer permisos de acceso y observar cambios en los nodos. En este tutorial, se describen los comandos más comunes que se utilizan en la consola de Zookeeper.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181/brokers/topics
```

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181/brokers/topics/topics1
```

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181/brokers/topics/topics1/partitions
```

## Comandos básicos

- El comando `ls` se utiliza para listar los nodos hijos de un nodo dado.

```shell    
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
ls /brokers/topics
ls /brokers/topics/topics1
ls /brokers/topics/topics1/partitions
```

- El comando `get` se utiliza para obtener el valor de un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
get /brokers/topics/topics1
get /brokers/topics/topics1/partitions
```

- El comando `create` se utiliza para crear un nodo.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
create /brokers/topics/topics2 "{'version':1,'partitions':1,'replication-factor':1}"
```

- El comando `delete` se utiliza para eliminar un nodo.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
delete /brokers/topics/topics2
```

- El comando `set` se utiliza para actualizar el valor de un nodo.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
set /brokers/topics/topics1 "{'version':1,'partitions':1,'replication-factor':1}"
```

- El comando `quit` se utiliza para salir de la consola de Zookeeper.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
quit
```

- El comando `help` se utiliza para obtener ayuda sobre los comandos disponibles.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
help
```
### Comandos para gestionar la consola de Zookeeper

- El comando `connect` se utiliza para conectarse a un servidor de Zookeeper diferente.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
connect localhost:2182
```

- El comando `reconnect` se utiliza para reconectarse al servidor de Zookeeper original.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
reconnect
```

- El comando `addauth` se utiliza para agregar una credencial de autenticación.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
addauth digest username:password
```

### Gestión de cuotas y permisos
- El comando `listquota` se utiliza para listar las cuotas de disco configuradas.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
listquota /brokers/topics
```

- El comando `setquota` se utiliza para establecer una cuota de disco para un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
setquota 100 /brokers/topics
```

- El comando `delquota` se utiliza para eliminar una cuota de disco para un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
delquota /brokers/topics
```

- El comando `close` se utiliza para cerrar la conexión con el servidor de Zookeeper.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
close
```

### Trabajo con nodos y permisos

- El comando `createall` se utiliza para crear un nodo y todos sus padres si no existen.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
createall /brokers/topics/topics3 "{'version':1,'partitions':1,'replication-factor':1}"
```

- El comando `deleteall` se utiliza para eliminar un nodo y todos sus hijos.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
deleteall /brokers/topics/topics3
```

- El comando `setAcl` se utiliza para establecer los permisos de acceso a un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
setAcl /brokers/topics/topics1 world:anyone:r
```

- El comando `getAcl` se utiliza para obtener los permisos de acceso a un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
getAcl /brokers/topics/topics1
```

- El comando `sync` se utiliza para sincronizar los datos en un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
sync /brokers/topics/topics1
```

### Observación de cambios en los nodos (Watches)

- El comando `addWatch` se utiliza para agregar un observador a un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
addWatch /brokers/topics/topics1
```

- El comando `removeWatch` se utiliza para eliminar un observador de un nodo dado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
removeWatch /brokers/topics/topics1
```

- El comando `listWatches` se utiliza para listar todos los observadores activos.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
listWatches
```

- El comando `envi` se utiliza para mostrar información sobre el entorno de la consola de Zookeeper.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
envi
```

- El comando `redo` se utiliza para reenviar el último comando ejecutado.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
redo
```

- El comando `history` se utiliza para mostrar el historial de comandos ejecutados.

```shell
./kafka_2.13-*/bin/zookeeper-shell.sh localhost:2181
history
```




