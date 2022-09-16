package org.accmanager.service.services.files;

import org.accmanager.service.exception.DirectoryReadException;
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
import static org.accmanager.service.enums.ExceptionEnum.ERROR_GETTING_SERVER_DIRECTORIES;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVERS;

@Service
public class DirectoryReadWriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryReadWriteService.class);

    public Optional<List<Path>> getAllServerDirectories() {
        try {
            Path dir = Paths.get(format(PATH_HOST_SERVERS.toString()));
            return Optional.of(Files.walk(dir, 1)
                    .filter(p -> Files.isDirectory(p) && !p.equals(dir))
                    .map(Path::getFileName)
                    .collect(Collectors.toList()));
        } catch (Exception ex) {
            LOGGER.error(format(ERROR_GETTING_SERVER_DIRECTORIES.toString(), ex));
            throw new DirectoryReadException(format(ERROR_GETTING_SERVER_DIRECTORIES.toString(), ex), ex);
        }
    }
}
