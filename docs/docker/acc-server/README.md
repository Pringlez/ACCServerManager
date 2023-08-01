## Docker ACC Server Notes
Running the ACC Server inside docker or kubernetes using [Wine](https://www.winehq.org/). Notes on building images & running 
containers or pods.

### Building ACC Server Docker Image:
The docker build scripts have built & tested on Ubuntu distros. Navigate to the `docs/docker/acc/ubuntu/<version>` directory and 
execute the command below to build acc server docker image:
```
docker build -t acc-server:1.8.18 -t acc-server:latest .
```

### Running ACC Server Container:
You wouldn't normally run this container manually, ideally the ACC Manager application should interface with the docker engine or kubernetes 
cluster to spin up this container. However, your free to use run these containers outside the ACC Manager.

Container parameters:
* **name** - Name of the running docker container
* **restart** - Restart policy for docker container, read more on official [documentation](https://docs.docker.com/config/containers/start-containers-automatically/#use-a-restart-policy)
* **expose** - Exposed port ranges, for the running acc server inside docker
* **net** - The network driver, currently set to `host`, official [documentation](https://docs.docker.com/network/#network-drivers)
* **v(volume)** - Mounting a directory on the host machine to docker container, official [documentation](https://docs.docker.com/storage/volumes/)

Running the docker container and detach, lets the container run in the background:
```
docker run -d --name acc-server --restart unless-stopped --expose=9231-9232 --net=host -v <path-to-servers-directory>:/accserver -t acc-server:latest
```

#### File Based Config - Volume
Configure a volume to pass server configs & the server executable from the host to the docker container:
```
-v "$(pwd)/servers:/accserver"
```
The `$(pwd)` variable passes the current directory, should work on both Windows & Linux machines.

#### Example - Run Container
An example run container command for `acc-server` using the latest tag:
```
docker run -d --name acc-server --restart unless-stopped --expose=9231-9232 --net=host -v "$(pwd)/servers:/accserver" -e SERVER_INSTANCE=ae85423a-b502-4833-bcc2-a424d3f8281e -t acc-server:latest
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
docker exec -it acc-server /bin/bash
```