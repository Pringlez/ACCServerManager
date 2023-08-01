## Docker ACC Manager Notes
Running the ACC Manager inside docker or kubernetes, notes on building images & running containers or pods.

### Building ACC Manager Docker Image
I'd recommend first running a production maven build to package the optimized jar & frontend build:
```
mvn clean install -DskipTests -Pprod
```
Run the following command in the __root__ project directory to package everything into a docker image:
```
docker build -f ./docs/docker/acc-manager/Dockerfile -t acc-manager:0.6.1 -t acc-manager:latest .
```

### Running ACC Manager Docker Container
Running the container and detach, lets the container run in the background:
```
docker run -d --name acc-manager --restart unless-stopped -p 80:8080 -v <path-to-servers-directory>:/accmanager/servers -t acc-manager:latest
```
Navigate to your hosts machines ip/domain address, the application's frontend & backend should respond on port 80 or whatever you specified
in your run command.

#### File Based Config - Volume
Configure a volume to pass server configs & the server executable from the host to the docker container:
```
-v "$(pwd)/servers:/accmanager/servers"
```
The `$(pwd)` variable passes the current directory, should work on both Windows & Linux machines.

#### Example - Running the Container
An example run container command for `acc-manager` using the latest tag:
```
docker run -d --name acc-manager --restart unless-stopped -p 80:8080 -v "$(pwd)/servers:/accmanager/servers" -t acc-manager:latest
```
This will map the project root -> `servers` directory, place the `accServer.exe` executable in `servers/accmanager/executable` directory. When you
POST a create instance request, it'll copy the executable and into an uuid generated `instance` directory along with all the written json config files.

#### Override Environment Variables
You can override a configuration parameter in the application, for example switching to the 'prod' & 'postgres' profile:
```
-e SPRING_PROFILES_ACTIVE='prod,postgres'
```

### Cleaning Up:
Clean up **all** docker images on system:
```
docker system prune -a
```

Clean up **all** images on system:
```
docker image prune -a
```

### Debug Running Container:
Attach to running container, for debugging:
```
docker exec -it acc-manager /bin/bash
```