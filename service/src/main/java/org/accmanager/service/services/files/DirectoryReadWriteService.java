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
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVERS;

@Service
public class DirectoryReadWriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryReadWriteService.class);

    public Optional<List<Path>> getAllServerDirectories(String dockerUsername) {
        try {
            Path dir = Paths.get(format(PATH_HOST_SERVERS.toString(), dockerUsername));
            return Optional.of(Files.walk(dir, 1)
                    .filter(p -> Files.isDirectory(p) && !p.equals(dir))
                    .map(Path::getFileName)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.error(format("Error getting server directories: %s", e));
        }
        return Optional.empty();
    }
}
