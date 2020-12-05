## Docker ACC Manager Notes
Running the ACC Manager inside docker using [Wine](https://www.winehq.org/).

## Building ACC Manager Docker Image
I'd recommend first running a production maven build to package the optimized jar & frontend build:
```
mvn clean install -DskipTests -Pprod
```
Run the following command in the __root__ project directory to package everything into a docker image:
```
docker build -t acc-manager:0.4.3 .
```

### Running ACC Manager Docker Container
Running the pre-built container by executing:
```
docker run -d --name acc-manager --restart unless-stopped -p 80:8080 -t acc-manager:0.4.3
```
Navigate to your hosts machines ip/domain address The application should respond on port 80 from your machines ip/domain address. 

For debugging purposes attach to the running container with the `-it` after docker run.

**Note:** _Work in progress, need to mount volume & sharing files correctly_

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