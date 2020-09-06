package org.accmanager.service.services.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_HOST_INSTANCES;

@Service
public class DirectoryReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryReaderService.class);

    public Optional<List<Path>> getAllServerDirectories(String dockerUsername) {
        try {
            Path dir = Paths.get(format(VOLUME_PATH_HOST_INSTANCES.getVolumePath(), dockerUsername));
            return Optional.of(Files.walk(dir, 1)
                    .filter(p -> Files.isDirectory(p) && !p.equals(dir))
                    .map(Path::getFileName)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.error(format("Error getting server directories: %s", e.getCause()));
        }
        return Optional.empty();
    }
}
