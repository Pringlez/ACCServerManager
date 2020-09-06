package org.accmanager.service.services.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_HOST_INSTANCES;

@Service
public class DirectoryReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryReader.class);

    public List<Path> getAllServerDirectories(String dockerUsername) {
        List<Path> accServersList = new ArrayList<>();

        try {
            List<Path> testList = Files.walk(Paths.get(format(VOLUME_PATH_HOST_INSTANCES.getVolumePath(), dockerUsername)), 1)
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .collect(Collectors.toList());

            LOGGER.info("Test 1: " + testList);

            try (Stream<Path> walk = Files.walk(Paths.get(format(VOLUME_PATH_HOST_INSTANCES.getVolumePath(), dockerUsername)))) {
                List<String> result = walk.filter(Files::isDirectory)
                        .map(x -> x.toString()).collect(Collectors.toList());
                result.forEach(System.out::println);
            }

            Path dir = Paths.get(format(VOLUME_PATH_HOST_INSTANCES.getVolumePath(), dockerUsername));
            accServersList = Files.walk(dir, 1)
                    .filter(p -> Files.isDirectory(p) && !p.equals(dir))
                    .map(Path::getFileName)
                    .collect(Collectors.toList());

            LOGGER.info("Test 2: " + accServersList);

        } catch (Exception e) {
            LOGGER.error("Error getting server directories: " + e.getMessage());
        }

        return accServersList;
    }
}
