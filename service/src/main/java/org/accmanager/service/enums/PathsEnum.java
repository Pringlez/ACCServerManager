package org.accmanager.service.enums;

public enum PathsEnum {

    PATH_HOST_EXECUTABLE("servers/accmanager/executable"),
    PATH_HOST_SERVERS("servers/accmanager/servers"),
    PATH_HOST_SERVER_INSTANCE("servers/accmanager/servers/%s"),
    PATH_HOST_SERVER_INSTANCE_CFG("servers/accmanager/servers/%s/cfg"),
    PATH_HOST_SERVER_INSTANCE_EXECUTABLE("servers/accmanager/servers/%s"),
    PATH_HOST_SERVER_INSTANCE_LOGS("servers/accmanager/servers/%s/logs"),
    PATH_HOST_SERVER_INSTANCE_CFG_FILE("servers/accmanager/servers/%s/cfg/%s"),
    PATH_CONTAINER(":/home/accserver/host");

    private final String volumePath;

    PathsEnum(String volumePath) {
        this.volumePath = volumePath;
    }

    @Override
    public String toString() {
        return volumePath;
    }
}
