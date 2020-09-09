package org.accmanager.service.enums;

public enum PathsEnum {

    PATH_HOST_EXECUTABLE("/home/%s/accmanager/executable"),
    PATH_HOST_SERVERS("/home/%s/accmanager/servers"),
    PATH_HOST_SERVER_INSTANCE("/home/%s/accmanager/servers/%s"),
    PATH_CONTAINER(":/home/server/accmanager/host");

    private final String volumePath;

    PathsEnum(String volumePath) {
        this.volumePath = volumePath;
    }

    @Override
    public String toString() {
        return volumePath;
    }
}
