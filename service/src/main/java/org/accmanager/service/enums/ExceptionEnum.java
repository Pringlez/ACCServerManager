package org.accmanager.service.enums;

public enum ExceptionEnum {

    ERROR_KILLING_CONTAINER_INSTANCE_ID("Error killing container instance id: %s"),
    ERROR_REMOVING_CONTAINER_INSTANCE_ID("Error removing container instance id: %s"),
    ERROR_RETRIEVING_LIST_OF_CONTAINERS("Error retrieving list of containers"),
    ERROR_DELETING_INSTANCE_DIRECTORY("Error deleting instance directory: %s"),
    ERROR_COPYING_EXECUTABLE("Error copying executable: %s"),
    ERROR_INITIALIZING_EXECUTABLE("Error initializing executable: %s - Error: %s"),
    ERROR_STARTING_EXECUTABLE("Error starting executable: %s - Error: %s"),
    ERROR_STOPPING_EXECUTABLE("Error stopping executable: %s - Error: %s"),
    ERROR_CREATING_DIRECTORY("Error creating directory: %s"),
    ERROR_GETTING_SERVER_DIRECTORIES("Error getting server directories: %s"),
    ERROR_READING_FILE("Error reading '%s' file: %s"),
    ERROR_WRITING_FILE("Error writing '%s' file: %s"),
    ERROR_STARTING_CONTAINER("Error starting container: %s - Error: %s"),
    ERROR_STOPPING_CONTAINER("Error stopping container: %s - Error: %s"),
    ERROR_RESTARTING_CONTAINER("Error restarting container: %s - Error: %s"),
    ACC_SERVER_INSTANCE_NOT_FOUND("ACC Server instance not found: %s");

    private final String volumePath;

    ExceptionEnum(String volumePath) {
        this.volumePath = volumePath;
    }

    @Override
    public String toString() {
        return volumePath;
    }
    }
