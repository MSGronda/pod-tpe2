<div style="display: flex; justify-content: space-between;">
  <div>
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
    <img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white">
  </div>
</div>

# Programación de Objetos Distribuidos - TPE2

This project is a distributed system that uses Hazelcast to perform map-reduce operations on a dataset of traffic tickets and infractions. The dataset is divided into two cities: Chicago and New York City.

## 1. Prerequisites

The following prerequisites are needed to run the server executable as well as the client applications:
- Maven
- Java 17 or above
- Hazelcast 3.8.6

## 2. Compiling and Generating Resources

1. First, to compile the project, run the following command in the root directory of the project:

```bash
mvn clean package
```

2. Now, we must extract the `tar.gz`. This command will extract the generated tar files, and grant execution permission to the shell files used for running the clients and server.
> 🚨 Be sure to run it in the root directory of the project.

```shell
sh scripts/generate_sources.sh
```
>  ⚠️ Depending on your Linux distribution, you may need to run the following command to change the line-ending of the .sh files: ```find . -name '*.sh' -exec sed -i -e 's/\r$//' {} \;```

## 3. Running the Server

Now, we can run the server. To do this, first we must navigate to the directory where we extracted the server tar file, and then we can run the shell script used to start a Hazelcast node. This will start it, listening on the port 5701.

```shell
cd server/target/tpe2-g11-server-1.0-SNAPSHOT && sh run-server.sh [ -Dinteface="<mask>" ]
```
Where:
- `-Dinterface` is a subnet mask which limits the IP addresses of other server instances which it'll try and connect to.

## 4 Running the Clients

Similarly, to run each client, first we must navigate to the directory where the extracted shell files are:

> 🚨 Be sure to run it in the root directory of the project.

```shell
cd client/target/tpe2-g11-client-1.0-SNAPSHOT
```

### 4.1 Query 1 - Total fines per infraction

This executable will obtain the total amount of fines for each infraction.

To run the query, execute the following command:

```shell
sh query1.sh -Daddresses="<addresses>" -Dcity="<city>" -DinPath="<path>" -DoutPath="<path>"
```
Where:
* `-Daddresses` refers to the IP addresses of the Hazelcast nodes with their ports (separated by semicolons).
* `-Dcity` refers to the city to be queried. It can be either `CHI` for Chicago or `NYC` for New York City.
* `-DinPath` refers to the path where `ticketsCHI.csv`, `ticketsNYC.csv`, `infractionsCHI.csv` and `infractionsNYC.csv` are located.
* `-DoutPath` refers to the path where both output files will be saved (`query1.csv` and `time1.txt`)


The results will be written in a comma seperated value file, using a semicolon as delimiter, named `query1.csv`. This file will have the following format:
```Text
Infractions;Tickets
...
```

A file with the timestamps of the start and end of the data loading and map-reduce processes will be saved as `time1.txt`.


### 4.2 Query 2 - Top 3 most popular infractions in each neighborhood

This executable will obtain the top 3 most popular infractions in each neighborhood, sorted by alphabetical name.

To run the query, execute the following command:

```shell
sh query2.sh -Daddresses="<addresses>" -Dcity="<city>" -DinPath="<path>" -DoutPath="<path>"
```
Where:
* `-Daddresses` refers to the IP addresses of the Hazelcast nodes with their ports (separated by semicolons).
* `-Dcity` refers to the city to be queried. It can be either `CHI` for Chicago or `NYC` for New York City.
* `-DinPath` refers to the path where `ticketsCHI.csv`, `ticketsNYC.csv`, `infractionsCHI.csv` and `infractionsNYC.csv` are located.
* `-DoutPath` refers to the path where both output files will be saved (`query2.csv` and `time2.txt`)


The results will be written in a comma seperated value file, using a semicolon as delimiter, named `query2.csv`. This file will have the following format:
```Text
County;InfractionTop1;InfractionTop2;InfractionTop3
...
```

A file with the timestamps of the start and end of the data loading and map-reduce processes will be saved as `time2.txt`.



### 4.3 Query 3 - Top N agencies with most collection percentage

This executable will obtain the top N agencies with the highest fine collection percentage, sorted descending by percentage and resolves alphabetically if it has the same value.

To run the query, execute the following command:

```shell
sh query2.sh -Daddresses="<addresses>" -Dcity="<city>" -Dn="<n>" -DinPath="<path>" -DoutPath="<path>"
```
Where:
* `-Daddresses` refers to the IP addresses of the Hazelcast nodes with their ports (separated by semicolons).
* `-Dcity` refers to the city to be queried. It can be either `CHI` for Chicago or `NYC` for New York City.
* `-Dn` refers to the number of agencies to be shown.
* `-DinPath` refers to the path where `ticketsCHI.csv`, `ticketsNYC.csv`, `infractionsCHI.csv` and `infractionsNYC.csv` are located.
* `-DoutPath` refers to the path where both output files will be saved (`query3.csv` and `time3.txt`)


The results will be written in a comma seperated value file, using a semicolon as delimiter, named `query3.csv`. This file will have the following format:
```Text
Issuing Agency;Percentage
...
```

A file with the timestamps of the start and end of the data loading and map-reduce processes will be saved as `time3.txt`.


### 4.4 Query 4 - License plate with most infractions for each neighborhood

This executable will obtain the license plates with most infractions for each neighborhood between the range of two dates. It is sorted alphabetically by neighborhood name.

To run the query, execute the following command:

```shell
sh query4.sh -Daddresses="<addresses>" -Dcity="<city>" -Dfrom="<date>" -Dto="<date>" -DinPath="<path>" -DoutPath="<path>"
```
Where:
* `-Daddresses` refers to the IP addresses of the Hazelcast nodes with their ports (separated by semicolons).
* `-Dcity` refers to the city to be queried. It can be either `CHI` for Chicago or `NYC` for New York City.
* `-Dfrom` refers to the starting date of the range with format `DD/MM/YY`.
* `-Dto` refers to the ending date of the range with format `DD/MM/YY`.
* `-DinPath` refers to the path where `ticketsCHI.csv`, `ticketsNYC.csv`, `infractionsCHI.csv` and `infractionsNYC.csv` are located.
* `-DoutPath` refers to the path where both output files will be saved (`query4.csv` and `time4.txt`)


The results will be written in a comma seperated value file, using a semicolon as delimiter, named `query4.csv`. This file will have the following format:
```Text
County;Plate;Tickets
...
```

A file with the timestamps of the start and end of the data loading and map-reduce processes will be saved as `time4.txt`.


### 4.5 Query 5 - Infraction pares same average fine (in the hundreds)

This executable will obtain the pares of infractions that have the same average fine, rounded to the hundreds. It is sorted descending by the average fine and alphabetically by the infraction names.

To run the query, execute the following command:

```shell
sh query5.sh -Daddresses="<addresses>" -Dcity="<city>" -DinPath="<path>" -DoutPath="<path>"
```
Where:
* `-Daddresses` refers to the IP addresses of the Hazelcast nodes with their ports (separated by semicolons).
* `-Dcity` refers to the city to be queried. It can be either `CHI` for Chicago or `NYC` for New York City.
* `-DinPath` refers to the path where `ticketsCHI.csv`, `ticketsNYC.csv`, `infractionsCHI.csv` and `infractionsNYC.csv` are located.
* `-DoutPath` refers to the path where both output files will be saved (`query5.csv` and `time5.txt`)


The results will be written in a comma seperated value file, using a semicolon as delimiter, named `query5.csv`. This file will have the following format:
```Text
Group;Infraction A;Infraction B
...
```

A file with the timestamps of the start and end of the data loading and map-reduce processes will be saved as `time5.txt`.