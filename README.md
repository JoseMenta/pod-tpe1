
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
        - [4.2.7. Obtener el rango de mostradores asignado para check-in](#427-obtener-el-rango-de-mostradores-asignado-para-check-in)
        - [4.2.8. Ingresar a la cola del rango de mostradores](#428-ingresar-a-la-cola-del-rango-de-mostradores)
        - [4.2.9. Consultar el estado de check-in](#429-consultar-el-estado-de-check-in)
    - [4.3. Servicio de notificaciones de aerolineas](#43-servicio-de-notificaciones-de-aerolineas)
        - [4.3.1. Registrar a una aerolínea para ser notificada](#431-registrar-a-una-aerolínea-para-ser-notificada)
        - [4.3.2. Anular el registro de una aerolínea](#432-anular-el-registro-de-una-aerolínea)
    - [4.4. Servicio de Consulta de Mostradores](#44-servicio-de-consulta-de-mostradores)
        - [4.4.1. Consultar el estado de los mostradores, filtrando por sector](#441-consultar-el-estado-de-los-mostradores-filtrando-por-sector)
        - [4.4.2. Consultar los check-ins realizados, filtrando por sector y aerolínea](#442-consultar-los-check-ins-realizados-filtrando-por-sector-y-aerolínea)
- [5. Aclaraciones sobre el proyecto](#5-aclaraciones-sobre-el-proyecto)

# Aiport service <!-- omit in toc -->
En este proyecto se implemento un servicio de aeropuerto en el cual se pueden realizar la gestion de pasajeros, de aerolineas y de vuelos que tiene el mismo.

Tanto el cliente como el servidor estan implementados en JAVA y se utiliza grpc para la comunicacion entre ellos.

## 1. Requisitos
- JAVA 21.
- Maven.

## 2. Compilacion

Para la compilacion del proyecto se deben seguir los siguientes pasos:

1. Clonar el repositorio.
2. Ubicarse en la carpeta del proyecto:``cd pod-tpe1``
3. Compilar el proyecto: ``mvn clean package``
    - Luego de la compilacion se generaran los siguientes archivos:
        - ``tpe1-g6-server-1.0-SNAPSHOT-bin.tar.gz``: Este archivo contiene el servidor.
        - ``tpe1-g6-client-1.0-SNAPSHOT-bin.tar.gz``: Este archivo contiene el cliente.
## 3. Ejecucion del servidor
Para la ejecucion del servidor se deben seguir los siguientes pasos:
1. Descomprimir el archivo ``tpe1-g6-server-1.0-SNAPSHOT-bin.tar.gz``.
2. Ubicarse dentro de la carpeta ``tpe1-g6-server-1.0-SNAPSHOT``
3. Darle permisos de ejecucion al archivo: ``chmod u+x run-server.sh``
3. Ejecutar ``./run-server.sh [-Dport=PORT]`` donde PORT es el puerto en el cual se desea que el servidor escuche. Si no se especifica el puerto por defecto es el 50051.

## 4. Ejecucion del cliente
Para la ejecucion del client se deben seguir los siguientes pasos:
1. Descomprimir el archivo ``tpe1-g6-client-1.0-SNAPSHOT-bin.tar.gz``.
2. Ubicarse dentro de la carpeta ``tpe1-g6-client-1.0-SNAPSHOT``
3. Darle permisos de ejecucion al archivo: ``chmod u+x *Client.sh``

### 4.1. Servicio de Administración del Aeropuerto
Para la ejecucion del cliente de administracion del aeropuerto se debe ejecutar el siguiente comando:

#### 4.1.1. Agregar sector
```Bash
./adminClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=addSector -Dsector=<sector>
```
#### 4.1.2. Agregar rango de mostradores
```Bash
./adminClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=addCounters -Dsector=<sector> -Dcounters=<cantidad de mostradores>
```
#### 4.1.3. Agregar pasajeros esperados
```Bash
./adminClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=manifest -DinPath=<path al archivo de entrada>
```
Debemos tener en cuenta que el archivo de entrada debe tener el siguiente formato:
```CSV
booking;flight;airline
1;1;Aerolinea1
```
### 4.2. Servicio de reserva de mostradores
Para la ejecucion del cliente de reserva de mostradores se debe ejecutar el siguiente comando:

#### 4.2.1. Consultar los sectores
```Bash
./counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=listSectors
```
#### 4.2.2. Consultar un rango de mostradores
```Bash
./counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=listCounters -Dsector=<sector> -DcounterFrom=<inicio del rango> -DcounterTo=<fin del rango>
```
#### 4.2.3. Asignar un rango de mostradores
```Bash
./counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=assignCounters -Dsector=<sector> -Dflights=<lista de vuelos> -Dairline=<aerolinea> -DcounterCount=<cantidad>
```
#### 4.2.4. Liberar un rango de mostradores
```Bash
./counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=freeCounters -Dsector=<sector> -DcounterFrom=<inicio del rango> -Dairline=<aerolinea>
```
#### 4.2.5. Realizar un check-in para cada mostrador
```Bash
./counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=checkinCounters -Dsector=<sector> -DcounterFrom=<inicio del rango> -Dairline=<aerolinea>
```
#### 4.2.6. Consultar las asignaciones pendientes

```Bash
./counterClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=listPendingAssignments -Dsector=<sector>
```
#### 4.2.7. Obtener el rango de mostradores asignado para check-in
```Bash
./passengerClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=fetchCounter -Dbooking=<booking id>
```
#### 4.2.8. Ingresar a la cola del rango de mostradores
```Bash
./passengerClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=passengerCheckin -Dbooking=<booking id> -Dsector=<sector> -Dcounter=<counter>
```
#### 4.2.9. Consultar el estado de check-in
```Bash
./passengerClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=passengerStatus -Dbooking=<booking id>
```
### 4.3. Servicio de notificaciones de aerolineas
Para la ejecucion del cliente de notificaciones de aerolineas se deben ejecutar los siguientes comandos:
#### 4.3.1. Registrar a una aerolínea para ser notificada
```Bash
./eventsClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=register -Dairline=<nombre de la aerolinea>
```

#### 4.3.2. Anular el registro de una aerolínea
```Bash
./eventsClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=unregister -Dairline=<nombre de la aerolinea>
```
### 4.4. Servicio de Consulta de Mostradores
Para la ejecucion del cliente de consulta de mostradores se deben ejecutar los siguientes comandos:
#### 4.4.1. Consultar el estado de los mostradores, filtrando por sector
```Bash
./queryClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=counters -DoutPath=<path de salida> [-Dsector=<sector>]
```
#### 4.4.2. Consultar los check-ins realizados, filtrando por sector y aerolínea
```Bash
./queryClient.sh -DserverAddress=<IP>:<Puerto en donde corre el servidor> -Daction=checkins -DoutPath=<path de salida> [-Dsector=<sector>] [-Dairline=<aerolinea>]
```
## 5. Aclaraciones sobre el proyecto
Este mismo proyecto es realizado para la materia Programación de Objetos Distribuidos del ITBA.

**Los integrantes del grupo son:**
- Lautaro Hernando
- Axel Facundo Preiti Tasat
- Gaston Francois
- Jose Rodolfo Mentasti

