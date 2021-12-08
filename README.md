# ACCServerManager
The ACC Manager has been designed to manage multiple ACC Server docker containers. Currently, only
capable of managing containers local to the ACC Manager application.

## Setup Host
First, you'll need to install [docker](https://docs.docker.com/get-started/) on your hosting machine. 
I'll assume your using Linux to host your game servers, an [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) guide for setting up docker. 

Running the jar of ACC Manager requires you install java on your host machine. I've found this [guide](https://linuxize.com/post/install-java-on-ubuntu-18-04/) easy to follow for 18.04 Ubuntu.
The installation on other distros is fairly similar.

Docker has support for Windows, the official [guide](https://docs.docker.com/docker-for-windows/install/) for Windows 10.

### Step 1 - Runtime
 * Java 8+ (Running ACC Manager)
 * Docker (Running ACC Server)
 
### Step 2 - ACC Server Docker Image
Building or downloading the docker image `acc-server-wine` in the [readme](docs/docker/acc-server/README.md).

### Step 3 - Running ACC Manager
You can download the latest available jar from GitHub. TODO...

Run the pre-built jar by executing:
```
java -jar -Ddocker.username=<your-system-username> acc-manager-0.4.4.jar
```

## Build ACC Manager
Build a production ready jar from project root directory using maven:
```
mvn clean install -DskipTests -Pprod
```
Then, run the built jar from the `service/target` directory:
```
java -jar -Ddocker.username=<your-system-username> service/target/acc-manager-service-0.4.4.jar
```

Jar parameters:
* **-Ddocker.username** - Name of the host's username running the docker engine

### Building ACC Manager Docker Image (_Experimental_)
Building the docker image `acc-manager` in the [readme](docs/docker/acc-manager/README.md).

## Development
To contribute to the project you need to set up a basic development environment. You'll need the following:

 * Java 8+ (JDK, IDE)
 * Maven (Build & Compile)
 * React Tools (Node, NPM, Browser Extensions)
 * Docker

I'd generally recommend VS Code for frontend & Intellij for backend development.

### API Spec
The [API specification](api/yaml/acc-manager.yaml) is used at compile time to generate a number of interfaces & java classes used within the application.
The project adheres to [OpenAPI 3.0.3 specification](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#infoObject) where possible.

### Compile & Test
To compile and test the application locally, run the following command:
```
mvn clean install
```
You can run the production maven build by adding `-Pprod` flag. This will run optimised webpack that will bundle the frontends assets & source code.
