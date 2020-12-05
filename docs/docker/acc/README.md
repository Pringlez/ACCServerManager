## Docker ACC Server Notes
Running the ACC server within docker using [Wine](https://www.winehq.org/).

### Building ACC Server Docker Image:
The docker build scripts have built & tested on Ubuntu based distros. Navigate to the `docs/docker/acc/ubuntu/<version>`.

Build acc server docker container:
```
docker build --rm -t acc-server-1.X.X-wine .
```
### Running ACC Server Docker Container:
Container parameters:
* **name** - Name of the running docker container
* **restart** - Restart policy for docker container, read more on official [documentation](https://docs.docker.com/config/containers/start-containers-automatically/#use-a-restart-policy)
* **expose** - Exposed port ranges, for the running acc server inside docker
* **net** - The network driver, currently set to `host`, official [documentation](https://docs.docker.com/network/#network-drivers)
* **v(volume)** - Mounting a directory on the host machine to docker container, official [documentation](https://docs.docker.com/storage/volumes/)

Running the docker container and detach, lets the container run in the background.
```
docker run -d --name acc-server-1 --restart unless-stopped --expose=9231-9232 --net=host -v /directory/to/config/on/host:/home/server/accmanager/host acc-server-1.X.X-wine
```
For debugging purposes attach to the running container with the `-it` after docker run:

### Cleaning up:
Clean up **all** docker images on system:
```
docker system prune -a
```

Clean up **all* images on system:
```
docker image prune -a
```

### Attach to running container:
Attach to running container:
```
docker exec -it acc-server-1 /bin/install
```