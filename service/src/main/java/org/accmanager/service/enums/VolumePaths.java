package org.accmanager.service.enums;

public enum VolumePaths {

    VOLUME_PATH_HOST_CONFIGS("/home/%s/acc-manager/servers/%s/config"),
    VOLUME_PATH_HOST_INSTANCES("/home/%s/acc-manager/servers"),
    VOLUME_PATH_CONTAINER_CONFIGS(":/home/server/config");

    private final String volumePath;

    VolumePaths(String volumePath) {
        this.volumePath = volumePath;
    }

    public String getVolumePath() {
        return volumePath;
    }
}
