package org.accmanager.service.enums;

public enum ExceptionEnum {

    ERROR_KILLING_CONTAINER_INSTANCE_ID("Error killing container instance id: %s"),
    ERROR_REMOVING_CONTAINER_INSTANCE_ID("Error removing container instance id: %s"),
    ERROR_RETRIEVING_LIST_OF_CONTAINERS("Error retrieving list of containers"),
    ERROR_DELETING_INSTANCE_DIRECTORY("Error deleting instance directory: %s"),
    ERROR_COPYING_EXECUTABLE("Error copying executable: %s"),
    ERROR_RUNNING_EXECUTABLE("Error running executable: %s"),
    ERROR_STARTING_EXECUTABLE("Error starting executable: %s"),
    ERROR_CREATING_DIRECTORY("Error creating directory: %s"),
    ERROR_GETTING_SERVER_DIRECTORIES("Error getting server directories: %s"),
    ERROR_READING_FILE("Error reading '%s' file: %s"),
    ERROR_WRITING_FILE("Error writing '%s' file: %s"),
    ERROR_EXECUTING_DOCKER_COMMAND("Error executing Docker command: %s"),
    ACC_SERVER_INSTANCE_NOT_FOUND("ACC Server instance not found: %s"),
    DOCKER_CONTAINER_NOT_FOUND("Docker container not found: %s"),
    DOCKER_CONTAINER_NAME_IN_USE("Docker container name in use: %s");

    private final String volumePath;

    ExceptionEnum(String volumePath) {
        this.volumePath = volumePath;
    }

    @Override
    public String toString() {
        return volumePath;
    }
}
