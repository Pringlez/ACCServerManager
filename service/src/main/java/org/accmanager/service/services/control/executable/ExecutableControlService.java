package org.accmanager.service.services.control.executable;

import org.accmanager.model.Instance;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_RUNNING_EXECUTABLE;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_STARTING_EXECUTABLE;
import static org.accmanager.service.enums.FilesEnum.ACC_SERVER_EXE;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE_EXECUTABLE;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE_LOGS;

@Service
public class ExecutableControlService extends ServerControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutableControlService.class);

    private Process process;
    private ProcessBuilder processBuilder;

    private static final Map<String, Instance> instanceMap = new HashMap<>();

    @Autowired
    public ExecutableControlService(DirectoryReadWriteService directoryReadWriteService, FileReadWriteService fileReadWriteService) {
        super(directoryReadWriteService, fileReadWriteService);
    }

    @Override
    public String createInstance(Instance instance) {
        instanceMap.put(instance.getId(), instance);
        createDirectories(instance);
        writeInstanceConfiguration(instance);
        copyExecutable(instance.getId());
        try {
            processBuilder = new ProcessBuilder(format(PATH_HOST_SERVER_INSTANCE_EXECUTABLE.toString(),
                    instance.getId()) + ACC_SERVER_EXE);
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_RUNNING_EXECUTABLE.toString(), instance.getId()));
        }
        return instance.getId();
    }

    @Override
    public void startInstance(String instanceId) {
        try {
            processBuilder.redirectErrorStream(true);
            File log = new File(format(PATH_HOST_SERVER_INSTANCE_LOGS.toString(), instanceId),
                    format("acc-server-%s-%s.log", instanceId, Instant.now()));
            processBuilder.redirectOutput(log);
            process = processBuilder.start();
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_STARTING_EXECUTABLE.toString(), instanceId));
        }
    }

    @Override
    public void stopInstance(String instanceId) {
        try {
            if (process.isAlive()) { process.destroy(); }
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_STARTING_EXECUTABLE.toString(), instanceId));
        }
    }

    @Override
    public void restartInstance(String instanceId) {
        stopInstance(instanceId);
        createInstance(instanceMap.get(instanceId));
    }

    @Override
    public void deleteConfigFiles(String instanceId) {
        getFileReadWriteService().deleteInstanceDirectoryConfigsAndFiles(instanceId);
    }

    @Override
    public String inspectInstance(String instanceId) {
        return null;
    }
}
