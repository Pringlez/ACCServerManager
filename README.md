# ACCServerManager

**Note:**: Work in progress, don't expect this to develop quickly :)

The application will allow you to manage and run multiple ACC game servers at once. Built using springboot, react and docker, the idea is create a system that requires minimal setup on a host machine.

Currently targeting both Windows & Linux support.

I'm mainly using this a learning opportunity for some of these technologies.

## Setup
First, you'll need to install docker on your hosting machine. I'll assume your using Linux to host your game servers, an [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) guide for setting up docker. Windows is also supported, the official [guide](https://docs.docker.com/docker-for-windows/install/) for Windows 10.

### Running the ACCManager
Running the pre-built container by exceuting:
```
sudo docker run -d -p 80:8080 -t acc-manager:latest
```

The application should respond on port 80 from your machines ip/domain address.

If your having issues try the following command to attach & see the start-up process of the application. Report any issues or error you may see for assistance. Run:
```
sudo docker run -d -p 80:8080 -t acc-manager:latest
```

## Configuration
TODO

## Docker
If you wish to build your own docker image, just run the following command to package everything into a docker image:
```
sudo docker build -t acc-manager:X.X.X .
```

Running the docker container is straight forward, execute the following command:
```
sudo docker run -it -p 80:8080 -t acc-manager:X.X.X
```

## Development
To contribute to the project you need to setup a basic development environment. You'll need the following:

 * Java 8+ (JDK, IDE)
 * Maven (Build & Compile)
 * React Tools

Use whatever development tools you feel comfortable to use, for example I normally use VS Code & Intellij for most of my work.

### API Spec
The [API specification](api/yaml/acc-manager.yaml) is used at compile time to generate a number of interfaces & java classes used within the application.
The project adheres to [OpenAPI 3.0.3 specification](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#infoObject) where possible.

### Complie & Test
To complie and test the application locally, run the following command:
```
mvn clean install
```
I would advise to run tests frequently to make sure everything works as expected.

### Running Locally
You can easily run the spring boot application using a simple java command:
```
java -jar service/target/acc-manager-service-X.X.X.jar org.acmanager.ACCManagerApplication
```

Once in the project's root directory, update specified jar with version number.
The second parameter must point towards the main java class.

## ToDo Features
Docker: Development of docker integration to spin up new containers using spring.
React: Get started on frontend development
