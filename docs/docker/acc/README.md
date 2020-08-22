## Docker Notes
Running the ACC server within docker using [Wine](https://www.winehq.org/).

### Running docker build:
Build container & expose server port:
```
docker build --build-arg ACC_SERVER_PORT=9231 --rm -t acc-server-1.X.X-wine .
```
Build container without exposing port:
```
docker build --rm -t acc-server-1.X.X-wine .
```
### Running docker image:
```
docker run -d --name acc-server-1 --restart unless-stopped --expose=9231-9232 --net=host -v /directory/to/config/on/host:/home/server/config acc-server-1.X.X-wine
```
### Run & attach to container:
Run & attach to container:
```
docker run -it --name acc-server-1 --restart unless-stopped --expose=9231-9232 --net=host -v /directory/to/config/on/host:/home/server/config acc-server-1.X.X-wine
```
### Cleaning up:
Clean up docker images:
```
docker system prune -a
```

### Attach to running container:
Attach to running container:
```
docker exec -it acc-server-1 /bin/install
```