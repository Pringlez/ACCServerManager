# ACCServerManager
The ACC Manager has been designed to manage multiple ACC Server docker containers. Currently, only
capable of managing containers local to the ACC Manager.

## Setup Host
First, you'll need to install [docker](https://docs.docker.com/get-started/) on your hosting machine. 
I'll assume your using Linux to host your game servers, an [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) guide for setting up docker. 

Docker has support for Windows, the official [guide](https://docs.docker.com/docker-for-windows/install/) for Windows 10.

### Runtime
 * Java 8+ (JDK, IDE) - (Running ACC Manager)
 * Docker (Running ACC Server)

## Running ACC Manager
Build a production ready build before running the built jar:
```
mvn clean install -DskipTests -Pprod
```
In the project's root directory, run the spring boot application with the following command:
```
java -jar -Dspring.profiles.active=local,h2 -Ddocker.username=<your-system-username> service/target/acc-manager-service-0.4.3.jar
```

### Running ACC Manager Docker Container (_Experimental_)
Running the pre-built container by executing:
```
docker run -d -p 80:8080 -t acc-manager:0.4.3
```
The application should respond on port 80 from your machines ip/domain address.

You can also run in `-it` attached mode to verify the application is starting up correctly:
```
docker run -it -p 80:8080 -t acc-manager:0.4.3
```
**Note:** _Work in progress, need to mount volume & sharing files correctly_

## Building ACC Manager Docker Image (_Experimental_)
I'd recommend first running a production maven build to package the optimized jar & frontend build:
```
mvn clean install -DskipTests -Pprod
```
Run the following command in the __root__ project directory to package everything into a docker image:
```
docker build -t acc-manager:0.4.3 .
```

### Building ACC Server Docker Image
Building the docker image `acc-server-wine` in the [readme](docs/docker/acc/README.md). For hosting the acc server inside docker using Wine.

## Development
To contribute to the project you need to set up a basic development environment. You'll need the following:

 * Java 8+ (JDK, IDE)
 * Maven (Build & Compile)
 * React Tools (Node, NPM, Browser Extensions)
 * Docker

Use whatever development tools you feel comfortable to use, for example I normally use VS Code & Intellij for most of my work.

### API Spec
The [API specification](api/yaml/acc-manager.yaml) is used at compile time to generate a number of interfaces & java classes used within the application.
The project adheres to [OpenAPI 3.0.3 specification](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#infoObject) where possible.

### Compile & Test
To compile and test the application locally, run the following command:
```
mvn clean install
```
Instead, you could run the production maven build by adding `-Pprod` flag. This will run the production optimised webpack that will bundle the application's assets & source code.
