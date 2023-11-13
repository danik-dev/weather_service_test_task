# Test task for Science Solutions
**Task:** Develop a system for collecting and processing weather data.

**Requirements:**
* Java
* Spring boot
* PostgreSQL / MySQL
* API documentation (Swagger)
* Publish on GitHub

## START UP INSTRUCTIONS


Repository contains 2 applications: weather_service and sensor

### Weather service
At first, you need to run ***weather service***. This application is dockerized so that 
you only need to navigate to weather service directory:

        cd weather_service

and then run:

        docker-compose up

then wait until all dependencies are downloaded.

### Sensor
To run the Sensor you need to have installed:
* jdk
* maven

Navigate to ***sensor*** directory:
        
         cd ../sensor/

and run following commands:

        mvn clean install
        java -jar target/sensor-1.0-SNAPSHOT.jar

congrats, you've finished!

## API Documentation
You can access it after application run with this link:

        http://localhost:8080/swagger-ui/index.html#
       


