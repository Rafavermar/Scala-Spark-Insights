#!/usr/bin/bash

# Buscamos el proceso de kafka y lo matamos
# El comando jps -l nos lista los procesos de java
# El comando grep kafka nos filtra los procesos de java que contienen la palabra kafka
# El comando awk '{print $1}' nos imprime la primera columna del resultado
# El comando xargs kill -9 mata el proceso
# ps -ef | grep kafka | grep -v grep | awk '{print $2}' | xargs kill -9
pgrep -f kafka | xargs kill -9
pgrep -f zookeeper | xargs kill -9

# Buscamos el proceso que escucha en el puerto 9092 y lo matamos
# El comando netstat -tuln nos lista los puertos que estan escuchando
# El comando grep 9092 nos filtra los puertos que contienen el puerto 9092
# El comando awk '{print $7}' nos imprime la septima columna del resultado
# El comando cut -d/ -f1 nos imprime la primera parte del resultado
# El comando xargs kill -9 mata el proceso
netstat -tuln | grep 9092 | awk '{print $7}' | cut -d/ -f1 | xargs kill -9
netstat -tuln | grep 9093 | awk '{print $7}' | cut -d/ -f1 | xargs kill -9
netstat -tuln | grep 9094 | awk '{print $7}' | cut -d/ -f1 | xargs kill -9
netstat -tuln | grep 2181 | awk '{print $7}' | cut -d/ -f1 | xargs kill -9