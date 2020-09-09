# ACCServerManager

**Note:**: Work in progress, don't expect this to develop quickly :)

The application will allow you to manage and run multiple ACC game servers at once. Built using springboot, react and docker, the idea is create a system that requires minimal setup on a host machine.

Currently targeting both Windows & Linux support.

I'm mainly using this a learning opportunity for some of these technologies.

## Setup
First, you'll need to install docker on your hosting machine. I'll assume your using Linux to host your game servers, an [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) guide for setting up docker. Windows is also supported, the official [guide](https://docs.docker.com/docker-for-windows/install/) for Windows 10.

### Running the ACCManager
Running the pre-built container by executing:
```
docker run -d -p 80:8080 -t acc-manager:0.4.0
```

The application should respond on port 80 from your machines ip/domain address.

You can also run in `-it` attached mode to verify the application is starting up correctly:
```
docker run -it -p 80:8080 -t acc-manager:0.4.0
```

## Configuration
TODO

## Docker
If you wish to build your own docker image, just run the following command in the __root__ project directory to package 
everything into a docker image:
```
docker build -t acc-manager:0.4.0 .
```
I'd recommend running production maven build to package the optimized jar for docker builds.

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

### Running Locally
You can easily run the spring boot application using a simple java command:
```
java -jar -Dspring.profiles.active=local,h2 acc-manager-service-0.4.0.jar
```

Once in the project's root directory, update specified jar with version number.
The second parameter must point towards the main java class.

## ToDo Features
Docker: Development of docker integration to spin up new containers using spring.
React: Get started on frontend development
