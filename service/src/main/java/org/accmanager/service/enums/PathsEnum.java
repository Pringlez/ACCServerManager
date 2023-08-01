package org.accmanager.service.enums;

public enum PathsEnum {

    PATH_HOST_EXECUTABLE("servers/accmanager/executable"),
    PATH_HOST_SERVERS("servers/accmanager/instances"),
    PATH_HOST_SERVER_INSTANCE("servers/accmanager/instances/%s"),
    PATH_HOST_SERVER_INSTANCE_CFG("servers/accmanager/instances/%s/cfg"),
    PATH_HOST_SERVER_INSTANCE_EXECUTABLE("servers/accmanager/instances/%s"),
    PATH_HOST_SERVER_INSTANCE_LOGS("servers/accmanager/instances/%s/logs"),
    PATH_HOST_SERVER_INSTANCE_CFG_FILE("servers/accmanager/instances/%s/cfg/%s"),
    PATH_CONTAINER(":/accserver/host");

    private final String volumePath;

    PathsEnum(String volumePath) {
        this.volumePath = volumePath;
    }

    @Override
    public String toString() {
        return volumePath;
    }
}
