package org.accmanager.service.services.dao;

import com.github.dockerjava.api.exception.DockerException;
import org.accmanager.model.*;
import org.accmanager.service.entity.*;
import org.accmanager.service.repository.*;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_RETRIEVING_LIST_OF_CONTAINERS;
import static org.accmanager.service.enums.FilesEnum.*;
import static org.accmanager.service.enums.PathsEnum.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class InstanceDaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerControl.class);

    private final InstancesRepository instancesRepository;
    private final EventRepository eventRepository;
    private final EventRulesRepository eventRulesRepository;
    private final CarEntriesRepository carEntriesRepository;
    private final AssistRulesRepository assistRulesRepository;
    private final BopRepository bopRepository;
    private final ConfigRepository configRepository;
    private final SettingsRepository settingsRepository;
    private final SessionsRepository sessionsRepository;
    private final CarEntryRepository carEntryRepository;
    private final DriverRepository driverRepository;
    private final BopEntryRepository bopEntryRepository;
    private final DirectoryReadWriteService directoryReadWriteService;
    private final FileReadWriteService fileReadWriteService;

    @Value("${accserver.files.directory.override:}")
    private String accFileDirectoryOverride;

    public InstanceDaoService(InstancesRepository instancesRepository, EventRepository eventRepository, EventRulesRepository eventRulesRepository,
                              CarEntriesRepository carEntriesRepository, AssistRulesRepository assistRulesRepository, BopRepository bopRepository,
                              ConfigRepository configRepository, SettingsRepository settingsRepository, SessionsRepository sessionsRepository,
                              CarEntryRepository carEntryRepository, DriverRepository driverRepository, BopEntryRepository bopEntryRepository,
                              DirectoryReadWriteService directoryReadWriteService, FileReadWriteService fileReadWriteService) {
        this.instancesRepository = instancesRepository;
        this.eventRepository = eventRepository;
        this.eventRulesRepository = eventRulesRepository;
        this.carEntriesRepository = carEntriesRepository;
        this.assistRulesRepository = assistRulesRepository;
        this.bopRepository = bopRepository;
        this.configRepository = configRepository;
        this.settingsRepository = settingsRepository;
        this.sessionsRepository = sessionsRepository;
        this.carEntryRepository = carEntryRepository;
        this.driverRepository = driverRepository;
        this.bopEntryRepository = bopEntryRepository;
        this.directoryReadWriteService = directoryReadWriteService;
        this.fileReadWriteService = fileReadWriteService;
    }

    public Optional<Instance> retrieveById(String instanceId) {
        Optional<InstancesEntity> instanceOptDB = instancesRepository.findInstancesEntityByInstanceId(instanceId);
        Instance instance = new Instance();
        if (instanceOptDB.isPresent()) {
            instance.setId(instanceOptDB.get().getInstanceId());
            instance.setName(instanceOptDB.get().getInstanceName());
            instance.setContainerImage(instanceOptDB.get().getContainerImage());
            instance.setEvent(getAndBuildEventById(instanceOptDB.get().getEventId()));
            instance.setEventRules(getAndBuildEventRulesById(instanceOptDB.get().getEventRulesId()));
            instance.setEntriesList(getAndBuildEntriesListById(instanceOptDB));
            instance.setAssistRules(getAndBuildAssistRulesById(instanceOptDB.get().getAssistRulesId()));
            instance.setBop(getAndBuildBopById(instanceOptDB.get().getBopId()));
            instance.setConfig(getAndBuildConfigById(instanceOptDB.get().getConfigId()));
            instance.setSettings(getAndSettingsById(instanceOptDB.get().getSettingsId()));
        }
        return Optional.of(instance);
    }

    private Event getAndBuildEventById(String eventId) {
        if (!isEmpty(eventId)) {
            Optional<EventEntity> eventEntityOpt = eventRepository.findEventEntityByEventId(eventId);
            if (eventEntityOpt.isPresent()) {
                Event event = new Event();
                event.setId(eventEntityOpt.get().getEventId());
                event.setName(eventEntityOpt.get().getEventName());
                event.setTrack(Event.TrackEnum.valueOf(eventEntityOpt.get().getTrack()));
                event.setPreRaceWaitingTimeSeconds(eventEntityOpt.get().getPreRaceWaitingTimeSec());
                event.setSessionOverTimeSeconds(eventEntityOpt.get().getSessionOverTimeSec());
                event.setAmbientTemp(eventEntityOpt.get().getAmbientTemp());
                event.setTrackTemp(eventEntityOpt.get().getTrackTemp());
                event.setCloudLevel((float) eventEntityOpt.get().getCloudLevel());
                event.setRain((int) eventEntityOpt.get().getRain());
                event.setWeatherRandomness(eventEntityOpt.get().getWeatherRandomness());
                event.setPostQualySeconds(eventEntityOpt.get().getPostQualyTimeSec());
                event.setPostRaceSeconds(eventEntityOpt.get().getPostRaceTimeSec());
                event.setMetaData(eventEntityOpt.get().getMetaData());
                event.setSessions(getAndBuildSessionsById(eventEntityOpt.get().getEventId()));
                return event;
            }
        }
        return new Event();
    }

    private EventRules getAndBuildEventRulesById(String eventRulesId) {
        if (!isEmpty(eventRulesId)) {
            Optional<EventRulesEntity> eventRulesEntityOpt = eventRulesRepository.findEventRulesEntityByEventRulesId(eventRulesId);
            if (eventRulesEntityOpt.isPresent()) {
                EventRules eventRules = new EventRules();
                eventRules.setId(eventRulesEntityOpt.get().getEventRulesId());
                eventRules.setQualifyStandingType(eventRulesEntityOpt.get().getQualifyStandingType());
                eventRules.setSuperpoleMaxCar(eventRulesEntityOpt.get().getSuperpoleMaxCar());
                eventRules.setPitWindowLengthSec(eventRulesEntityOpt.get().getPitWindowLengthSec());
                eventRules.setDriverStintTimeSec(eventRulesEntityOpt.get().getDriverStintTimeSec());
                eventRules.setMandatoryPitstopCount(eventRulesEntityOpt.get().getMandatoryPitstopCount());
                eventRules.setMaxTotalDrivingTime(eventRulesEntityOpt.get().getMaxTotalDrivingTime());
                eventRules.setMaxDriversCount(eventRulesEntityOpt.get().getMaxDriversCount());
                eventRules.setIsRefuellingAllowedInRace(eventRulesEntityOpt.get().getIsRefuellingAllowedInRace());
                eventRules.setIsRefuellingTimeFixed(eventRulesEntityOpt.get().getIsRefuellingTimeFixed());
                eventRules.setIsMandatoryPitstopRefuellingRequired(eventRulesEntityOpt.get().getIsMandatoryPitstopRefuellingRequired());
                eventRules.setIsMandatoryPitstopTyreChangeRequired(eventRulesEntityOpt.get().getIsMandatoryPitstopTyreChangeRequired());
                eventRules.setIsMandatoryPitstopSwapDriverRequired(eventRulesEntityOpt.get().getIsMandatoryPitstopSwapDriverRequired());
                return eventRules;
            }
        }
        return new EventRules();
    }

    private EntriesList getAndBuildEntriesListById(Optional<InstancesEntity> instanceOptDB) {
        if (instanceOptDB.isPresent()) {
            Optional<CarEntriesEntity> carEntriesEntityOpt = carEntriesRepository.findCarEntriesEntityByCarEntriesId(instanceOptDB.get().getEntriesId());
            if (carEntriesEntityOpt.isPresent()) {
                EntriesList entriesList = new EntriesList();
                entriesList.setId(carEntriesEntityOpt.get().getCarEntriesId());
                entriesList.setEntries(getAndBuildEntryList(instanceOptDB));
                entriesList.setForceEntryList(carEntriesEntityOpt.get().getForceEntryList());
                entriesList.setConfigVersion(carEntriesEntityOpt.get().getConfigVersion());
                return entriesList;
            }
        }
        return new EntriesList();
    }

    private AssistRules getAndBuildAssistRulesById(String assistsId) {
        if (!isEmpty(assistsId)) {
            Optional<AssistRulesEntity> assistsEntityOpt = assistRulesRepository.findAssistsEntityByAssistsId(assistsId);
            if (assistsEntityOpt.isPresent()) {
                AssistRules assistRules = new AssistRules();
                assistRules.setId(assistsEntityOpt.get().getAssistsId());
                assistRules.setStabilityControlLevelMax(assistsEntityOpt.get().getStabilityControlLevelMax());
                assistRules.setDisableAutosteer(assistsEntityOpt.get().getDisableAutoSteer());
                assistRules.setDisableAutoLights(assistsEntityOpt.get().getDisableAutoLights());
                assistRules.setDisableAutoWiper(assistsEntityOpt.get().getDisableAutoWiper());
                assistRules.setDisableAutoEngineStart(assistsEntityOpt.get().getDisableAutoEngineStart());
                assistRules.setDisableAutoPitLimiter(assistsEntityOpt.get().getDisableAutoPitLimiter());
                assistRules.setDisableAutoGear(assistsEntityOpt.get().getDisableAutoGear());
                assistRules.setDisableAutoClutch(assistsEntityOpt.get().getDisableAutoClutch());
                assistRules.setDisableIdealLine(assistsEntityOpt.get().getDisableIdealLine());
                return assistRules;
            }
        }
        return new AssistRules();
    }

    private BoP getAndBuildBopById(String bopId) {
        if (!isEmpty(bopId)) {
            Optional<BopEntity> bopEntityOpt = bopRepository.findBopEntityByBopId(bopId);
            if (bopEntityOpt.isPresent()) {
                BoP bop = new BoP();
                bop.setId(bopEntityOpt.get().getBopId());
                bop.setEntries(getAndBuildBopEntries(bopEntityOpt.get().getBopEntryId()));
                bop.setDisableAutosteer(bopEntityOpt.get().getDisableAutoSteer());
                bop.setDisableAutoLights(bopEntityOpt.get().getDisableAutoLights());
                bop.setDisableAutoWiper(bopEntityOpt.get().getDisableAutoWiper());
                return bop;
            }
        }
        return new BoP();
    }

    private Config getAndBuildConfigById(String configId) {
        if (!isEmpty(configId)) {
            Optional<ConfigEntity> configEntityOpt = configRepository.findConfigEntityByConfigId(configId);
            if (configEntityOpt.isPresent()) {
                Config config = new Config();
                config.setId(configEntityOpt.get().getConfigId());
                config.setTcpPort(configEntityOpt.get().getTcpPort());
                config.setUdpPort(configEntityOpt.get().getUdpPort());
                config.setRegisterToLobby(configEntityOpt.get().getRegisterToLobby());
                config.setMaxConnections(configEntityOpt.get().getMaxConnections());
                config.setLanDiscovery(configEntityOpt.get().getLanDiscovery());
                config.setPublicIP(configEntityOpt.get().getPublicIP());
                config.setConfigVersion(configEntityOpt.get().getConfigVersion());
                return config;
            }
        }
        return new Config();
    }

    private Settings getAndSettingsById(String settingsId) {
        if (!isEmpty(settingsId)) {
            Optional<SettingsEntity> settingsEntityOpt = settingsRepository.findSettingsEntityBySettingsId(settingsId);
            if (settingsEntityOpt.isPresent()) {
                Settings settings = new Settings();
                settings.setId(settingsEntityOpt.get().getSettingsId());
                settings.setServerName(settingsEntityOpt.get().getServerInstanceName());
                settings.setAdminPassword(settingsEntityOpt.get().getAdminPassword());
                settings.setCarGroup(Settings.CarGroupEnum.valueOf(settingsEntityOpt.get().getCarGroup()));
                settings.setTrackMedalsRequirement(settingsEntityOpt.get().getTrackMedalsRequirement());
                settings.setSafetyRatingRequirement(settingsEntityOpt.get().getSafetyRatingRequirement());
                settings.setRacecraftRatingRequirement(settingsEntityOpt.get().getRaceCraftRatingRequirement());
                settings.setPassword(settingsEntityOpt.get().getServerPassword());
                settings.setSpectatorPassword(settingsEntityOpt.get().getSpectatorPassword());
                settings.setMaxCarSlots(settingsEntityOpt.get().getMaxCarSlots());
                settings.setDumpLeaderboards(settingsEntityOpt.get().getDumpLeaderBoards());
                settings.setIsRaceLocked(settingsEntityOpt.get().getIsRaceLocked());
                settings.setIsPrepPhaseLocked(settingsEntityOpt.get().getIsPrepPhaseLocked());
                settings.setRandomizeTrackWhenEmpty(settingsEntityOpt.get().getRandomizeTrackWhenEmpty());
                settings.setCentralEntryListPath(settingsEntityOpt.get().getCentralEntryListPath());
                settings.setAllowAutoDQ(settingsEntityOpt.get().getAllowAutoDq());
                settings.setShortFormationLap(settingsEntityOpt.get().getShortFormationLap());
                settings.setDumpEntryList(settingsEntityOpt.get().getDumpEntryList());
                settings.setFormationLapType(settingsEntityOpt.get().getFormationLapType());
                settings.setDoDriverSwapBroadcast(settingsEntityOpt.get().getDoDriverSwapBroadcast());
                settings.setConfigVersion(settingsEntityOpt.get().getConfigVersion());
                return settings;
            }
        }
        return new Settings();
    }

    private List<EntryBoP> getAndBuildBopEntries(String bopEntryId) {
        if (!isEmpty(bopEntryId)) {
            Optional<List<BopEntryEntity>> bopEntryEntitiesOpt = bopEntryRepository.findBopEntryEntitiesByBopEntryId(bopEntryId);
            if (bopEntryEntitiesOpt.isPresent()) {
                List<EntryBoP> entryBopList = new ArrayList<>();
                for (BopEntryEntity bopEntryEntity : bopEntryEntitiesOpt.get()) {
                    EntryBoP entryBop = new EntryBoP();
                    entryBop.setId(bopEntryEntity.getBopId());
                    entryBop.setTrack(EntryBoP.TrackEnum.valueOf(bopEntryEntity.getTrack()));
                    entryBop.setCarModel(bopEntryEntity.getCarModel());
                    entryBop.setBallastKg(bopEntryEntity.getBallest());
                    entryBop.setRestrictor(bopEntryEntity.getRestrictor());
                    entryBopList.add(entryBop);
                }
                return entryBopList;
            }
        }
        return new ArrayList<>();
    }

    private List<Entry> getAndBuildEntryList(Optional<InstancesEntity> instanceOptDB) {
        if (instanceOptDB.isPresent()) {
            Optional<List<CarEntryEntity>> carEntryEntitiesOpt = carEntryRepository.findCarEntryEntitiesByCarEntriesId(instanceOptDB.get().getEntriesId());
            if (carEntryEntitiesOpt.isPresent()) {
                List<Entry> entryList = new ArrayList<>();
                for (CarEntryEntity carEntryEntity : carEntryEntitiesOpt.get()) {
                    Entry entry = new Entry();
                    entry.setDrivers(getAndBuildDriversList(carEntryEntity));
                    entry.setRaceNumber(carEntryEntity.getRaceNumber());
                    entry.setForcedCarModel(carEntryEntity.getForceCarModel());
                    entry.setOverrideDriverInfo(carEntryEntity.getOverrideDriverInfo());
                    entry.setCustomCar(carEntryEntity.getCustomCar());
                    entry.setOverrideCarModelForCustomCar(carEntryEntity.getOverrideCarModelForCustomCar());
                    entry.setIsServerAdmin(carEntryEntity.getIsServerAdmin());
                    entry.setDefaultGridPosition(carEntryEntity.getDefaultGridPosition());
                    entry.setBallastKg(carEntryEntity.getBallastKg());
                    entry.setRestrictor(carEntryEntity.getRestrictor());
                    entryList.add(entry);
                }
                return entryList;
            }
        }
        return new ArrayList<>();
    }

    private List<Driver> getAndBuildDriversList(CarEntryEntity carEntryEntity) {
        if (!isEmpty(carEntryEntity)) {
            Optional<List<DriverEntity>> driverEntitiesOpt = driverRepository.findDriverEntitiesByCarEntryId(carEntryEntity.getCarEntryId());
            List<Driver> driverList = new ArrayList<>();
            if (driverEntitiesOpt.isPresent()) {
                for (DriverEntity driverEntity : driverEntitiesOpt.get()) {
                    Driver driver = new Driver();
                    driver.setId(driverEntity.getDriverId());
                    driver.setFirstName(driverEntity.getFirstName());
                    driver.setLastName(driverEntity.getLastName());
                    driver.setShortName(driverEntity.getShortName());
                    driver.setDriverCategory(driverEntity.getDriverCategory());
                    driver.setPlayerID(driverEntity.getPlayerId());
                    driverList.add(driver);
                }
                return driverList;
            }
        }
        return new ArrayList<>();
    }

    private List<Session> getAndBuildSessionsById(String eventId) {
        if (!isEmpty(eventId)) {
            Optional<List<SessionsEntity>> sessionsEntitiesOpt = sessionsRepository.findSessionsEntitiesByEventId(eventId);
            if (sessionsEntitiesOpt.isPresent()) {
                List<Session> sessionList = new ArrayList<>();
                for (SessionsEntity session : sessionsEntitiesOpt.get()) {
                    sessionList.add(buildSessionFromEntity(session));
                }
                return sessionList;
            }
        }
        return new ArrayList<>();
    }

    private Session buildSessionFromEntity(SessionsEntity sessionsEntity) {
        Session session = new Session();
        session.setId(sessionsEntity.getSessionId());
        session.setHourOfDay(sessionsEntity.getHourOfDay());
        session.setDayOfWeekend(sessionsEntity.getDayOfWeekend());
        session.setTimeMultiplier(sessionsEntity.getTimeMultiplier());
        return session;
    }

    public Optional<Instance> retrieveAll() {
        return Optional.empty();
    }

    public void updateServerById(String id, Instance obj) {

    }

    public void saveData(Instance obj) {

    }

    public List<Instance> listOfInstances() {
        List<Instance> instancesList = getInstancesByFile();
        // TODO - Combine all results from file system & database
        return instancesList;
    }

    private List<Instance> getInstancesByFile() {
        List<Instance> instancesList = new ArrayList<>();
        try {
            Optional<List<Path>> accServerDirs = directoryReadWriteService.getAllServerDirectories();
            accServerDirs.ifPresent(dirs -> dirs.forEach(
                    dir -> instancesList.add(readInstanceConfiguration(dir.toString()))));
        } catch (Exception ex) {
            LOGGER.warn(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString());
            throw new DockerException(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString(), INTERNAL_SERVER_ERROR.value(), ex);
        }
        return instancesList;
    }

    private List<Instance> getInstancesByDB() {
        List<Instance> instancesList = new ArrayList<>();
        try {
            retrieveAll();
        } catch (Exception ex) {
            LOGGER.warn(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString());
            throw new DockerException(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString(), INTERNAL_SERVER_ERROR.value(), ex);
        }
        return instancesList;
    }

    public void writeInstanceConfiguration(Instance instance) {
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_JSON, instance.getEvent());
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_RULES_JSON, instance.getEventRules());
        fileReadWriteService.writeJsonFile(instance.getId(), ENTRY_LIST_JSON, instance.getEntriesList());
        fileReadWriteService.writeJsonFile(instance.getId(), ASSIST_RULES_JSON, instance.getAssistRules());
        fileReadWriteService.writeJsonFile(instance.getId(), BOP_JSON, instance.getBop());
        fileReadWriteService.writeJsonFile(instance.getId(), CONFIGURATION_JSON, instance.getConfig());
        fileReadWriteService.writeJsonFile(instance.getId(), SETTINGS_JSON, instance.getSettings());
    }

    public Instance readInstanceConfiguration(String instanceId) {
        Instance instance = new Instance();
        instance.setId(instanceId);
        instance.setEvent((Event) fileReadWriteService.readJsonFile(instanceId, EVENT_JSON, Event.class).orElse(new Event()));
        instance.setEventRules((EventRules) fileReadWriteService.readJsonFile(instanceId, EVENT_RULES_JSON, EventRules.class).orElse(new EventRules()));
        instance.setEntriesList((EntriesList) fileReadWriteService.readJsonFile(instanceId, ENTRY_LIST_JSON, EntriesList.class).orElse(new EntriesList()));
        instance.setAssistRules((AssistRules) fileReadWriteService.readJsonFile(instanceId, ASSIST_RULES_JSON, AssistRules.class).orElse(new AssistRules()));
        instance.setBop((BoP) fileReadWriteService.readJsonFile(instanceId, BOP_JSON, BoP.class).orElse(new BoP()));
        instance.setConfig((Config) fileReadWriteService.readJsonFile(instanceId, CONFIGURATION_JSON, Config.class).orElse(new Config()));
        instance.setSettings((Settings) fileReadWriteService.readJsonFile(instanceId, SETTINGS_JSON, Settings.class).orElse(new Settings()));
        return instance;
    }

    public void copyExecutable(String instanceId) {
        fileReadWriteService.copyExecutable(instanceId);
    }

    public void createDirectories(Instance instance) {
        fileReadWriteService.createNewDirectory(format(accFileDirectoryOverride + PATH_HOST_SERVER_INSTANCE_CFG, instance.getId()));
        fileReadWriteService.createNewDirectory(format(accFileDirectoryOverride + PATH_HOST_SERVER_INSTANCE_EXECUTABLE, instance.getId()));
        fileReadWriteService.createNewDirectory(format(accFileDirectoryOverride + PATH_HOST_SERVER_INSTANCE_LOGS, instance.getId()));
    }

    public DirectoryReadWriteService getDirectoryReadWriteService() {
        return directoryReadWriteService;
    }

    public FileReadWriteService getFileReadWriteService() {
        return fileReadWriteService;
    }
}
