package org.accmanager.service.services.files;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_HOST_INSTANCES;

@Service
public class DirectoryReader {

    public Optional<String[]> getAllServerDirectories() {
        File file = new File(VOLUME_PATH_HOST_INSTANCES.toString());
        return Optional.ofNullable(file.list((current, name) -> new File(current, name).isDirectory()));
    }
}
