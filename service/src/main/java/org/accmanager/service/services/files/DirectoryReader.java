package org.accmanager.service.services.files;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_HOST_INSTANCES;

@Service
public class DirectoryReader {

    public Optional<String[]> getAllServerDirectories() {
        File file = new File(VOLUME_PATH_HOST_INSTANCES.toString());

        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        return Optional.ofNullable(directories);
    }
}
