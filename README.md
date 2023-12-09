# ACC Manager
ACC Manager is tool for managing multiple game serer docker containers, and local servers on the filesystem.

## Setup Host
First, you'll need to install [docker](https://docs.docker.com/get-started/) on your hosting machine. 
I'll assume your using Linux to host your game servers, an [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) guide for setting up docker.

Docker has support for Windows, the official [guide](https://docs.docker.com/docker-for-windows/install/) for Windows 10 / 11.

### Step 1 - Runtime
 * Java 8+ (Running ACC Manager) or
 * Docker (Running ACC Manager / ACC Server)
 
### Step 2 - ACC Server Container (Optional)
Building or downloading the docker image `acc-server` in the [readme](docs/docker/acc-server/README.md).
Due to changes to the server executable, it no longer runs reliably using wine. I may build a windows based docker image to allow
instance to be spun up in kubernetes clusters or docker hosts.

### Step 3 (Java) - ACC Manager Jar
After running a maven build, you should be able to run the jar in your `service/target` directory by executing:
```
java -jar service/target/acc-manager-0.7.0.jar
```

After starting the application, place the `accServer.exe` executable in `servers/accmanager/executable` directory. When you
POST a create instance request, it'll copy the executable and into an uuid generated `instance` directory along with all the written json config files.

### Step 3 (Docker) - ACC Manager Container
Building and running `acc-manager` using docker in the [readme](docs/docker/acc-manager/README.md).

## Build ACC Manager - Production Build
Build a production ready jar from project root directory using maven:
```
mvn clean install -Pprod
```
Then, run the built jar from the `service/target` directory:
```
java -jar service/target/acc-manager-service-0.7.0.jar
```

## Development
To contribute to the project you need to set up a basic development environment. You'll need the following:

 * Java 8+ (JDK, IDE)
 * Maven (Build & Compile)
 * React Tools (Node, NPM, Browser Extensions)
 * Docker

### API Spec
The [API specification](api/yaml/acc-manager.yaml) is used at compile time to generate a number of interfaces & java classes used within the application.
The project adheres to [OpenAPI 3.0.3 specification](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#infoObject) where possible.

### Compile & Test
To compile and test the application locally, run the following command:
```
mvn clean install
```
You can run the production maven build by adding `-Pprod` flag. This will run optimised webpack that will bundle the frontends assets & source code.

To execute cucumber tests using the commandline, use the following maven profile:
```
mvn clean install -P dev-cucumber
```
