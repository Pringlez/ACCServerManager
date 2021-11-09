package org.accmanager.service.enums;

public enum PathsEnum {

    PATH_HOST_EXECUTABLE("/home/%s/accmanager/executable"),
    PATH_HOST_SERVERS("/home/%s/accmanager/servers"),
    PATH_HOST_SERVER_INSTANCE("/home/%s/accmanager/servers/%s"),
    PATH_HOST_SERVER_INSTANCE_CFG("/home/%s/accmanager/servers/%s/cfg"),
    PATH_HOST_SERVER_INSTANCE_EXECUTABLE("/home/%s/accmanager/servers/%s/executable"),
    PATH_HOST_SERVER_INSTANCE_CFG_FILE("/home/%s/accmanager/servers/%s/cfg/%s"),
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
