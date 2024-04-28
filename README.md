
<div style="display: flex; justify-content: space-between;">
  <div>
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
    <img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white">
    <img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white">
  </div>
</div>

- [1. Requisitos](#1-requisitos)
- [2. Compilacion](#2-compilacion)
- [3. Ejecucion del servidor](#3-ejecucion-del-servidor)
- [4. Ejecucion del cliente](#4-ejecucion-del-cliente)
    - [4.1. Servicio de Administración del Aeropuerto](#41-servicio-de-administración-del-aeropuerto)
        - [4.1.1. Agregar sector](#411-agregar-sector)
        - [4.1.2. Agregar rango de mostradores](#412-agregar-rango-de-mostradores)
        - [4.1.3. Agregar pasajeros esperados](#413-agregar-pasajeros-esperados)
    - [4.2. Servicio de reserva de mostradores](#42-servicio-de-reserva-de-mostradores)
        - [4.2.1. Consultar los sectores](#421-consultar-los-sectores)
        - [4.2.2. Consultar un rango de mostradores](#422-consultar-un-rango-de-mostradores)
        - [4.2.3. Asignar un rango de mostradores](#423-asignar-un-rango-de-mostradores)
        - [4.2.4. Liberar un rango de mostradores](#424-liberar-un-rango-de-mostradores)
        - [4.2.5. Realizar un check-in para cada mostrador](#425-realizar-un-check-in-para-cada-mostrador)
        - [4.2.6. Consultar las asignaciones pendientes](#426-consultar-las-asignaciones-pendientes)
    - [4.3. Servicio de Check-in de Pasajeros](#43-servicio-de-check-in-de-pasajeros)
        - [4.3.1. Obtener el rango de mostradores asignado para check-in](#431-obtener-el-rango-de-mostradores-asignado-para-check-in)
        - [4.3.2. Ingresar a la cola del rango de mostradores](#432-ingresar-a-la-cola-del-rango-de-mostradores)
        - [4.3.3. Consultar el estado de check-in](#433-consultar-el-estado-de-check-in)
    - [4.4. Servicio de notificaciones de aerolineas](#44-servicio-de-notificaciones-de-aerolineas)
        - [4.4.1. Registrar a una aerolínea para ser notificada](#441-registrar-a-una-aerolínea-para-ser-notificada)
        - [4.4.2. Anular el registro de una aerolínea](#442-anular-el-registro-de-una-aerolínea)
    - [4.5. Servicio de Consulta de Mostradores](#45-servicio-de-consulta-de-mostradores)
        - [4.5.1. Consultar el estado de los mostradores, filtrando por sector](#451-consultar-el-estado-de-los-mostradores-filtrando-por-sector)
        - [4.5.2. Consultar los check-ins realizados, filtrando por sector y aerolínea](#452-consultar-los-check-ins-realizados-filtrando-por-sector-y-aerolínea)
- [5. Tests](#5-tests)
  - [5.1 Test de cliente](#51-test-de-cliente)
  - [5.2 Test de servidor](#52-test-de-servidor)
- [6. Logs](#6-logs)
- [7. Aclaraciones sobre el proyecto](#7-aclaraciones-sobre-el-proyecto)


# ✈️ Aiport service <!-- omit in toc -->
En este proyecto se implemento un servicio de aeropuerto en el cual se pueden realizar la gestion de pasajeros, de aerolineas y de vuelos que tiene el mismo.

Tanto el cliente como el servidor estan implementados en Java y se utiliza gRPC para la comunicacion entre ellos.

## 1. Requisitos
- Java 21.
- Maven.

## 2. Compilacion

Para la compilacion del proyecto se deben seguir los siguientes pasos:

1. Clonar el repositorio.
2. Ubicarse en la carpeta del proyecto:
   ```Bash 
    cd pod-tpe1
   ```
3. Compilar el proyecto: 
    ````Bash
    mvn clean package
   ````
Luego de la compilacion se generaran los siguientes archivos:
   - ``server/target/tpe1-g6-server-1.0-SNAPSHOT-bin.tar.gz``: Este archivo contiene el servidor.
   - ``client/target/tpe1-g6-client-1.0-SNAPSHOT-bin.tar.gz``: Este archivo contiene el cliente.
## 3. Ejecucion del servidor
Para la ejecucion del servidor se deben seguir los siguientes pasos:
1. Ir al directorio del servidor 
    ```Bash 
    cd server/target
   ```
2. Descomprimir el archivo
    ```Bash
    tar -xzf tpe1-g6-server-1.0-SNAPSHOT-bin.tar.gz
    ```
3. Ubicarse dentro de la carpeta ``tpe1-g6-server-1.0-SNAPSHOT``
    ```Bash
    cd tpe1-g6-server-1.0-SNAPSHOT
    ```
4. Darle permisos de ejecucion al archivo
    ```Bash
    chmod u+x run-server.sh
    ```
5. Ejecutar el servidor, donde PORT es el puerto en el cual se desea que escuche. Si no se especifica el puerto por defecto es el 50051.
    ```Bash
    ./run-server.sh [-Dport=PORT]
    ```
## 4. Ejecucion del cliente
Para la ejecucion del client se deben seguir los siguientes pasos:
1. Ir al directorio del cliente
    ```Bash 
    cd client/target
   ```
2. Descomprimir el archivo
    ```Bash
    tar -xzf tpe1-g6-client-1.0-SNAPSHOT-bin.tar.gz
    ```
3. Ubicarse dentro de la carpeta ``tpe1-g6-client-1.0-SNAPSHOT``
    ```Bash
    cd tpe1-g6-client-1.0-SNAPSHOT
    ```
4. Darle permisos de ejecucion a los archivos
    ```Bash
    chmod u+x *Client.sh
    ```
### 4.1. Servicio de Administración del Aeropuerto
Para la ejecucion del cliente de administracion del aeropuerto se debe ejecutar el siguiente comando:

#### 4.1.1. Agregar sector
```Bash
sh adminClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=addSector -Dsector=<sector>
```
#### 4.1.2. Agregar rango de mostradores
```Bash
sh adminClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=addCounters -Dsector=<sector> -Dcounters=<cantidad de mostradores>
```
#### 4.1.3. Agregar pasajeros esperados
```Bash
sh adminClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=manifest -DinPath=<path al archivo de entrada>
```
Debemos tener en cuenta que el archivo de entrada debe tener el siguiente formato(CSV):
```CSV
booking;flight;airline
ABC123;AC987;AirCanada
```
### 4.2. Servicio de reserva de mostradores


#### 4.2.1. Consultar los sectores
```Bash
sh counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=listSectors
```
#### 4.2.2. Consultar un rango de mostradores
```Bash
sh counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=listCounters -Dsector=<sector> -DcounterFrom=<inicio del rango> -DcounterTo=<fin del rango>
```
#### 4.2.3. Asignar un rango de mostradores
```Bash
sh counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=assignCounters -Dsector=<sector> -Dflights=<lista de vuelos> -Dairline=<aerolinea> -DcounterCount=<cantidad>
```
#### 4.2.4. Liberar un rango de mostradores
```Bash
sh counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=freeCounters -Dsector=<sector> -DcounterFrom=<inicio del rango> -Dairline=<aerolinea>
```
#### 4.2.5. Realizar un check-in para cada mostrador
```Bash
sh counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=checkinCounters -Dsector=<sector> -DcounterFrom=<inicio del rango> -Dairline=<aerolinea>
```
#### 4.2.6. Consultar las asignaciones pendientes
```Bash
sh counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=listPendingAssignments -Dsector=<sector>
```

### 4.3. Servicio de Check-in de Pasajeros
Para la ejecución del cliente de check-in de pasajeros se debe ejecutar el siguiente comando:
#### 4.3.1. Obtener el rango de mostradores asignado para check-in
```Bash
sh passengerClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=fetchCounter -Dbooking=<booking id>
```
#### 4.3.2. Ingresar a la cola del rango de mostradores
```Bash
sh passengerClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=passengerCheckin -Dbooking=<booking id> -Dsector=<sector> -Dcounter=<counter>
```
#### 4.3.3. Consultar el estado de check-in
```Bash
sh passengerClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=passengerStatus -Dbooking=<booking id>
```
### 4.4. Servicio de notificaciones de aerolineas
Para la ejecucion del cliente de notificaciones de aerolineas se deben ejecutar los siguientes comandos:
#### 4.4.1. Registrar a una aerolínea para ser notificada
```Bash
sh eventsClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=register -Dairline=<nombre de la aerolinea>
```

#### 4.4.2. Anular el registro de una aerolínea
```Bash
sh eventsClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=unregister -Dairline=<nombre de la aerolinea>
```
### 4.5. Servicio de Consulta de Mostradores
Para la ejecucion del cliente de consulta de mostradores se deben ejecutar los siguientes comandos:
#### 4.5.1. Consultar el estado de los mostradores, filtrando por sector
```Bash
sh queryClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=queryCounters -DoutPath=<path de salida> [-Dsector=<sector>]
```
#### 4.5.2. Consultar los check-ins realizados, filtrando por sector y aerolínea
```Bash
sh queryClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=checkins -DoutPath=<path de salida> [-Dsector=<sector>] [-Dairline=<aerolinea>]
```
## 5. Tests

### 5.1 Test de cliente
Dentro de la carpeta ``client/src/test/resources`` se encuentran test de integracion para cada uno de los servicios que se pueden realizar en los clientes.
Cada uno de los tests se puede correr con el flag ``-b`` el cual va a buildear tanto el servidor como el cliente para su funcionamiento.
**Los tests se deben correr desde la raíz del proyecto**

> **Nota** : Los tests son pruebas diseñadas para verificar que las invocaciones de los clientes se comporten según lo esperado en diferentes escenarios. La certeza de su funcionamiento se determina al evaluar la salida resultante de estas pruebas.
### 5.2 Test de servidor
Dentro del servidor se encuentran test unitarios para la estructura de datos llamada  ``RangeList`` que se utiliza para la asignación de mostradores. Para correr los tests se debe ejecutar el siguiente comando:
```Bash
mvn test
```

## 6. Logs
Tanto el servidor como el cliente imprimen los logs en un archivo diferenciado dentro de la carpeta logs, para consultarlos dirigirse a ``logs/pod-client.log`` y ``logs/pod-server.log``.

## 7. Aclaraciones sobre el proyecto
Este mismo proyecto es realizado para la materia Programación de Objetos Distribuidos del ITBA.

**Los integrantes del grupo son:**
- 62248 - [José Rodolfo Mentasti](https://github.com/JoseMenta)
- 62618 - [Axel Facundo Preiti Tasat](https://github.com/AxelPreitiT)
- 62500 - [Gastón Ariel Francois](https://github.com/francoisgaston)
- 62329 - [Lautaro Hernando](https://github.com/laucha12)

