package org.accmanager.service.services.dao.instance;

import org.accmanager.model.AssistRules;
import org.accmanager.model.Driver;
import org.accmanager.model.EntriesList;
import org.accmanager.model.Entry;
import org.accmanager.model.Event;
import org.accmanager.model.EventRules;
import org.accmanager.model.Instance;
import org.accmanager.model.Session;
import org.accmanager.service.entity.AssistsEntity;
import org.accmanager.service.entity.CarEntriesEntity;
import org.accmanager.service.entity.CarEntryEntity;
import org.accmanager.service.entity.DriverEntity;
import org.accmanager.service.entity.EventEntity;
import org.accmanager.service.entity.EventRulesEntity;
import org.accmanager.service.entity.InstancesEntity;
import org.accmanager.service.entity.SessionsEntity;
import org.accmanager.service.repository.AssistsRepository;
import org.accmanager.service.repository.BopRepository;
import org.accmanager.service.repository.CarEntriesRepository;
import org.accmanager.service.repository.CarEntryRepository;
import org.accmanager.service.repository.ConfigRepository;
import org.accmanager.service.repository.DriverRepository;
import org.accmanager.service.repository.EventRepository;
import org.accmanager.service.repository.EventRulesRepository;
import org.accmanager.service.repository.InstancesRepository;
import org.accmanager.service.repository.SessionsRepository;
import org.accmanager.service.repository.SettingsRepository;
import org.accmanager.service.services.dao.DaoService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstanceDaoService implements DaoService<Instance> {

    private final InstancesRepository instancesRepository;
    private final EventRepository eventRepository;
    private final EventRulesRepository eventRulesRepository;
    private final CarEntriesRepository carEntriesRepository;
    private final AssistsRepository assistsRepository;
    private final BopRepository bopRepository;
    private final ConfigRepository configRepository;
    private final SettingsRepository settingsRepository;
    private final SessionsRepository sessionsRepository;
    private final CarEntryRepository carEntryRepository;
    private final DriverRepository driverRepository;

    public InstanceDaoService(InstancesRepository instancesRepository, EventRepository eventRepository, EventRulesRepository eventRulesRepository,
                              CarEntriesRepository carEntriesRepository, AssistsRepository assistsRepository, BopRepository bopRepository,
                              ConfigRepository configRepository, SettingsRepository settingsRepository, SessionsRepository sessionsRepository,
                              CarEntryRepository carEntryRepository, DriverRepository driverRepository) {
        this.instancesRepository = instancesRepository;
        this.eventRepository = eventRepository;
        this.eventRulesRepository = eventRulesRepository;
        this.carEntriesRepository = carEntriesRepository;
        this.assistsRepository = assistsRepository;
        this.bopRepository = bopRepository;
        this.configRepository = configRepository;
        this.settingsRepository = settingsRepository;
        this.sessionsRepository = sessionsRepository;
        this.carEntryRepository = carEntryRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Instance> retrieveById(String id, Instance instance) {
        if (ObjectUtils.isEmpty(id)) { id = instance.getId(); }
        Optional<InstancesEntity> instanceOptDB = instancesRepository.findInstancesEntityByInstanceId(id);
        Optional<Instance> instanceOpt = Optional.of(new Instance());
        if (instanceOptDB.isPresent()) {
            instanceOpt.get().setId(instanceOptDB.get().getInstanceId());
            instanceOpt.get().setEvent(getAndBuildEventById(instanceOptDB));
            instanceOpt.get().setEventRules(getAndBuildEventRulesById(instanceOptDB));
            instanceOpt.get().setEntriesList(getAndBuildEntriesListById(instanceOptDB));
            instanceOpt.get().setAssists(getAndBuildAssistRulesById(instanceOptDB));
        }
        return instanceOpt;
    }

    private Event getAndBuildEventById(Optional<InstancesEntity> instanceOptDB) {
        if (instanceOptDB.isPresent()) {
            Optional<EventEntity> eventEntityOpt = eventRepository.findEventEntityByEventId(instanceOptDB.get().getEventId());
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

    private EventRules getAndBuildEventRulesById(Optional<InstancesEntity> instanceOptDB) {
        if (instanceOptDB.isPresent()) {
            Optional<EventRulesEntity> eventRulesEntityOpt = eventRulesRepository.findEventRulesEntityByEventRulesId(instanceOptDB.get().getEventRulesId());
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

    private AssistRules getAndBuildAssistRulesById(Optional<InstancesEntity> instanceOptDB) {
        if (instanceOptDB.isPresent()) {
            Optional<AssistsEntity> assistsEntityOpt = assistsRepository.findAssistsEntityByAssistsId(instanceOptDB.get().getAssistsId());
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
        if (!ObjectUtils.isEmpty(carEntryEntity)) {
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
        Optional<List<SessionsEntity>> sessionsEntitiesOpt = sessionsRepository.findSessionsEntitiesByEventId(eventId);
        if (sessionsEntitiesOpt.isPresent()) {
            List<Session> sessionList = new ArrayList<>();
            for (SessionsEntity session : sessionsEntitiesOpt.get()) {
                sessionList.add(buildSessionFromEntity(session));
            }
            return sessionList;
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

    @Override
    public Optional<Instance> retrieveAll() {
        return Optional.empty();
    }

    @Override
    public void updateServerById(String id, Instance obj) {

    }

    @Override
    public void saveData(Instance obj) {

    }
}
