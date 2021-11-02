# user-ops

This service is consumer-facing service which will accept request from the user which validates the request and send the validated request to **data-handler** service, to process and store the data in file format.

**Steps to setup and run the user-ops service :**

1.Go to [Apache Kafka downloads](https://kafka.apache.org/downloads) page and download the [Scala 2.3.0 kafka_2.12-2.3.0.tgz](https://archive.apache.org/dist/kafka/2.3.0/kafka_2.12-2.3.0.tgz).

2.Unzip the file to a particular location

>   a. Open a command prompt and start the Zookeeper using the below command.
>   
  	zookeeper-server-start.bat {file_location}\config\zookeeper.properties
  	      
>   b. Open a new command prompt and start the Apache Kafka using the below command.
>   
  	kafka-server-start.bat {file_location}\config\server.properties	      

>   c. Open a new command prompt and create a topic with name data-processor, it has only one partition & one replica using below command.
>   
  	kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 -topic data-processor 	
 
3.Change the SQL connection details as per your system in application properties and create new schema with name **user_data**.
 	       	       	      
4.Clone the project **user-ops** and import the project in Eclipse or STS(Spring Tool Suite).

5.Run Maven install in Eclipse/STS or mvn install from cmd prompt from the project location.

6.Once build is **Success**, run the application as java or spring boot App.

7.After the application is started, access the API's using swagger [user-ops swager](http://localhost:8082/user-ops/swagger-ui.html).

8.Application supports 3 below listed API's :

>  a. Store : Accepts valid JSON as input and file type as request parameter (Email should be unique and mandatory value),sample input : 
>
			{
			  "id": 1,
			  "name": "sam",
			  "place": "Bangalore",
			  "phone": "97123341",
			  "ext":123,
			  "email":"sam@gmail.com"
			}

>  b. Read : Accepts email id as input and response will be JSON data.

>  c. Update : Process is similar to store API where the additional feature is to update the existing data with email as reference.
	


