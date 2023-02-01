package org.accmanager.service.services.control.executable;

import org.accmanager.model.Instance;
import org.accmanager.model.StorageType;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.dao.InstanceDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_INITIALIZING_EXECUTABLE;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_PAUSING_EXECUTION;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_STARTING_EXECUTABLE;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_STOPPING_EXECUTABLE;
import static org.accmanager.service.enums.FilesEnum.ACC_SERVER_EXE;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE_EXECUTABLE;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE_LOGS;

@Service
@ConditionalOnProperty(prefix = "accserver", name = "control", havingValue = "executable")
public class ExecutableControlService extends ServerControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutableControlService.class);

    private Process process;
    private ProcessBuilder processBuilder;

    @Autowired
    public ExecutableControlService(InstanceDaoService instanceDaoService) {
        super(instanceDaoService);
    }

    @Override
    public String createInstance(Instance instance) {
        getDaoService().createDirectories(instance);
        getDaoService().writeInstanceConfiguration(instance);
        getDaoService().copyExecutable(instance.getId());
        initializeProcessBuilder(instance.getId());
        return instance.getId();
    }

    private void initializeProcessBuilder(String instanceId) {
        try {
            processBuilder = new ProcessBuilder(format(PATH_HOST_SERVER_INSTANCE_EXECUTABLE.toString(),
                    instanceId) + ACC_SERVER_EXE);
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_INITIALIZING_EXECUTABLE.toString(), instanceId, ex));
        }
    }

    @Override
    public void startInstance(String instanceId) {
        try {
            initializeProcessBuilder(instanceId);
            processBuilder.redirectErrorStream(true);
            File log = new File(format(PATH_HOST_SERVER_INSTANCE_LOGS.toString(), instanceId),
                    format("acc-server-%s-%s.log", instanceId, Instant.now().getEpochSecond()));
            processBuilder.redirectOutput(log);
            process = processBuilder.start();
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_STARTING_EXECUTABLE.toString(), instanceId, ex));
        }
    }

    @Override
    public void stopInstance(String instanceId) {
        try {
            if (process.isAlive()) {
                process.destroy();
            }
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_STOPPING_EXECUTABLE.toString(), instanceId, ex));
        }
    }

    @Override
    public void restartInstance(String instanceId) {
        stopInstance(instanceId);
        pauseExecution(5);
        startInstance(instanceId);
    }

    private static void pauseExecution(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ie) {
            LOGGER.warn(format(ERROR_PAUSING_EXECUTION.toString(), ie));
        }
    }

    @Override
    public void deleteConfigFiles(String instanceId) {
        getDaoService().getFileReadWriteService().deleteInstanceDirectoryConfigsAndFiles(instanceId);
    }

    @Override
    public String inspectInstance(String instanceId, StorageType storageType) {
        return null;
    }
}
