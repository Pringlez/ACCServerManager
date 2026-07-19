package org.accmanager.service.api.web;

import org.accmanager.model.Instance;
import org.accmanager.model.Config;
import org.accmanager.model.Settings;
import org.accmanager.model.EventRules;
import org.accmanager.model.EntriesList;
import org.accmanager.model.AssistRules;
import org.accmanager.model.BoP;
import org.accmanager.model.Event;
import org.accmanager.service.entity.EventEntity;
import org.accmanager.service.entity.ConfigEntity;
import org.accmanager.service.entity.SettingsEntity;
import org.accmanager.service.entity.AssistRulesEntity;
import org.accmanager.service.entity.InstancesEntity;
import org.accmanager.service.repository.EventRepository;
import org.accmanager.service.repository.InstancesRepository;
import org.accmanager.service.repository.ConfigRepository;
import org.accmanager.service.repository.SettingsRepository;
import org.accmanager.service.repository.AssistRulesRepository;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.control.ServerControlManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/servers")
public class Servers {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private static final String IS_DARK_MODE = "isDarkMode";

    private final ServerControl serverControl;
    private final EventRepository eventRepository;
    private final InstancesRepository instancesRepository;
    private final ConfigRepository configRepository;
    private final SettingsRepository settingsRepository;
    private final AssistRulesRepository assistRulesRepository;

    public Servers(ServerControl serverControl,
                   EventRepository eventRepository,
                   InstancesRepository instancesRepository,
                   ConfigRepository configRepository,
                   SettingsRepository settingsRepository,
                   AssistRulesRepository assistRulesRepository) {
        this.serverControl = serverControl;
        this.eventRepository = eventRepository;
        this.instancesRepository = instancesRepository;
        this.configRepository = configRepository;
        this.settingsRepository = settingsRepository;
        this.assistRulesRepository = assistRulesRepository;
    }

    public static class InstanceViewModel {
        private final Instance instance;
        private final String status;

        public InstanceViewModel(Instance instance, String status) {
            this.instance = instance;
            this.status = status;
        }

        public Instance getInstance() {
            return instance;
        }

        public String getStatus() {
            return status;
        }
    }

    @GetMapping
    public String start(Model model) {
        List<InstanceViewModel> vms = serverControl.getDaoService().listOfInstances().stream()
                .map(inst -> new InstanceViewModel(inst, isInstanceRunning(inst.getId()) ? "running" : "stopped"))
                .collect(Collectors.toList());

        model.addAttribute("instances", vms);
        model.addAttribute("now", new Date().toInstant());
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/servers";
    }

    @GetMapping("/add")
    public String addServerForm(Model model) {
        model.addAttribute("presets", eventRepository.findAll());
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/servers-wizard";
    }

    @PostMapping("/add")
    public String addServer(@RequestParam String name,
                             @RequestParam int tcpPort,
                             @RequestParam int udpPort,
                             @RequestParam Instance.ControlTypeEnum controlType,
                             @RequestParam(required = false) String presetId,
                             Model model) {
        if (!((ServerControlManager) serverControl).isPortFree(tcpPort) || !((ServerControlManager) serverControl).isPortFree(udpPort)) {
             model.addAttribute("error", "Ports already in use.");
             return addServerForm(model);
        }

        Instance instance = new Instance();
        instance.setId(name.toLowerCase().replaceAll("\\s+", "_"));
        instance.setName(name);
        instance.setControlType(controlType);
        
        Config config = new Config();
        config.setTcpPort(tcpPort);
        config.setUdpPort(udpPort);
        config.setMaxConnections(85);
        config.setLanDiscovery(1);
        config.setRegisterToLobby(0);
        config.setPublicIP("0");
        config.setConfigVersion(1);
        instance.setConfig(config);

        Settings settings = new Settings();
        settings.setServerName(name);
        settings.setAdminPassword("adminPassword123");
        settings.setCarGroup(Settings.CarGroupEnum.FREE_FOR_ALL);
        settings.setTrackMedalsRequirement(0);
        settings.setSafetyRatingRequirement(-1);
        settings.setRacecraftRatingRequirement(-1);
        settings.setPassword("");
        settings.setSpectatorPassword("");
        settings.setMaxCarSlots(30);
        settings.setDumpLeaderboards(0);
        settings.setIsRaceLocked(1);
        settings.setIsPrepPhaseLocked(0);
        settings.setRandomizeTrackWhenEmpty(0);
        settings.setCentralEntryListPath("");
        settings.setAllowAutoDQ(0);
        settings.setShortFormationLap(1);
        settings.setDumpEntryList(0);
        settings.setFormationLapType(3);
        settings.setDoDriverSwapBroadcast(1);
        settings.setConfigVersion(1);
        instance.setSettings(settings);

        instance.setEventRules(new EventRules());
        instance.setEntriesList(new EntriesList());
        instance.setAssistRules(new AssistRules());
        instance.setBop(new BoP());

        if (presetId != null && !presetId.isEmpty()) {
            Event event = serverControl.getDaoService().getAndBuildEventById(presetId);
            instance.setEvent(event);
        } else {
            instance.setEvent(new Event());
        }

        serverControl.createInstance(instance);
        
        // Save to DB
        org.accmanager.service.entity.InstancesEntity entity = new org.accmanager.service.entity.InstancesEntity();
        entity.setInstanceId(instance.getId());
        entity.setInstanceName(instance.getName());
        entity.setControlType(controlType.getValue());
        instancesRepository.save(entity);

        serverControl.startInstance(instance.getId());
        return "redirect:/web/servers";
    }

    @PostMapping("/{id}/start")
    public String startServer(@PathVariable("id") String id, Model model) {
        serverControl.startInstance(id);
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        model.addAttribute("item", new InstanceViewModel(instance, "running"));
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/servers :: server-row";
    }

    @PostMapping("/{id}/stop")
    public String stopServer(@PathVariable("id") String id, Model model) {
        serverControl.stopInstance(id);
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        model.addAttribute("item", new InstanceViewModel(instance, "stopped"));
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/servers :: server-row";
    }

    @PostMapping("/{id}/restart")
    public String restartServer(@PathVariable("id") String id, Model model) {
        serverControl.restartInstance(id);
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        model.addAttribute("item", new InstanceViewModel(instance, "running"));
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/servers :: server-row";
    }

    @GetMapping("/{id}/stats")
    @ResponseBody
    public String getServerStats(@PathVariable("id") String id) {
        return serverControl.getContainerStats(id);
    }

    @GetMapping("/{id}/edit")
    public String editServerForm(@PathVariable("id") String id, Model model) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        boolean isRunning = isInstanceRunning(id);
        
        model.addAttribute("instance", instance);
        model.addAttribute("isRunning", isRunning);
        model.addAttribute("now", new Date().toInstant());
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/servers-edit";
    }

    @PostMapping("/{id}/edit")
    public String editServer(@PathVariable("id") String id,
                             @RequestParam String serverName,
                             @RequestParam String adminPassword,
                             @RequestParam(required = false, defaultValue = "") String password,
                             @RequestParam(required = false, defaultValue = "") String spectatorPassword,
                             @RequestParam int maxCarSlots,
                             @RequestParam int tcpPort,
                             @RequestParam int udpPort,
                             @RequestParam int maxConnections,
                             
                             // Assists Rules
                             @RequestParam int stabilityControlLevelMax,
                             @RequestParam(required = false, defaultValue = "0") int disableAutosteer,
                             @RequestParam(required = false, defaultValue = "0") int disableAutoLights,
                             @RequestParam(required = false, defaultValue = "0") int disableAutoWiper,
                             @RequestParam(required = false, defaultValue = "0") int disableAutoEngineStart,
                             @RequestParam(required = false, defaultValue = "0") int disableAutoPitLimiter,
                             @RequestParam(required = false, defaultValue = "0") int disableAutoGear,
                             @RequestParam(required = false, defaultValue = "0") int disableAutoClutch,
                             @RequestParam(required = false, defaultValue = "0") int disableIdealLine,
                             
                             // Event Details
                             @RequestParam String track,
                             @RequestParam int preRaceWaitingTimeSeconds,
                             @RequestParam int sessionOverTimeSeconds,
                             @RequestParam int ambientTemp,
                             @RequestParam float cloudLevel,
                             @RequestParam int rain,
                             @RequestParam int weatherRandomness,
                             @RequestParam int postQualySeconds,
                             @RequestParam int postRaceSeconds,
                             
                             // Persistence to DB Option
                             @RequestParam(required = false, defaultValue = "false") boolean persistToDb,
                             Model model) {
        boolean isRunning = isInstanceRunning(id);
        if (isRunning) {
            model.addAttribute("error", "Cannot modify configuration while server is running. Please stop the server first.");
            Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
            model.addAttribute("instance", instance);
            model.addAttribute("isRunning", true);
            model.addAttribute("now", new Date().toInstant());
            model.addAttribute(IS_DARK_MODE, darkMode);
            return "pages/general/servers-edit";
        }

        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        if (instance.getSettings() == null) {
            instance.setSettings(new Settings());
        }
        if (instance.getConfig() == null) {
            instance.setConfig(new Config());
        }

        instance.getSettings().setServerName(serverName);
        instance.getSettings().setAdminPassword(adminPassword);
        instance.getSettings().setPassword(password);
        instance.getSettings().setSpectatorPassword(spectatorPassword);
        instance.getSettings().setMaxCarSlots(maxCarSlots);

        instance.getConfig().setTcpPort(tcpPort);
        instance.getConfig().setUdpPort(udpPort);
        instance.getConfig().setMaxConnections(maxConnections);

        // Update Assist Rules
        if (instance.getAssistRules() == null) {
            instance.setAssistRules(new AssistRules());
        }
        instance.getAssistRules().setStabilityControlLevelMax(stabilityControlLevelMax);
        instance.getAssistRules().setDisableAutosteer(disableAutosteer);
        instance.getAssistRules().setDisableAutoLights(disableAutoLights);
        instance.getAssistRules().setDisableAutoWiper(disableAutoWiper);
        instance.getAssistRules().setDisableAutoEngineStart(disableAutoEngineStart);
        instance.getAssistRules().setDisableAutoPitLimiter(disableAutoPitLimiter);
        instance.getAssistRules().setDisableAutoGear(disableAutoGear);
        instance.getAssistRules().setDisableAutoClutch(disableAutoClutch);
        instance.getAssistRules().setDisableIdealLine(disableIdealLine);

        // Update Event Details
        if (instance.getEvent() == null) {
            instance.setEvent(new Event());
        }
        try {
            instance.getEvent().setTrack(org.accmanager.model.Event.TrackEnum.fromValue(track));
        } catch (Exception e) {
            try {
                instance.getEvent().setTrack(org.accmanager.model.Event.TrackEnum.valueOf(track.toUpperCase()));
            } catch (Exception ex) {
                instance.getEvent().setTrack(org.accmanager.model.Event.TrackEnum.values()[0]);
            }
        }
        instance.getEvent().setPreRaceWaitingTimeSeconds(preRaceWaitingTimeSeconds);
        instance.getEvent().setSessionOverTimeSeconds(sessionOverTimeSeconds);
        instance.getEvent().setAmbientTemp(ambientTemp);
        instance.getEvent().setCloudLevel(cloudLevel);
        instance.getEvent().setRain(rain);
        instance.getEvent().setWeatherRandomness(weatherRandomness);
        instance.getEvent().setPostQualySeconds(postQualySeconds);
        instance.getEvent().setPostRaceSeconds(postRaceSeconds);

        serverControl.getDaoService().writeInstanceConfiguration(instance);

        if (persistToDb) {
            try {
                InstancesEntity instancesEntity = instancesRepository.findById(id)
                        .orElseGet(() -> {
                            InstancesEntity n = new InstancesEntity();
                            n.setInstanceId(id);
                            n.setControlType(instance.getControlType() != null ? instance.getControlType().getValue() : "DOCKER");
                            return n;
                        });
                instancesEntity.setInstanceName(serverName);

                // Persist Settings
                SettingsEntity settingsEntity;
                if (instancesEntity.getSettingsId() != null) {
                    settingsEntity = settingsRepository.findSettingsEntityBySettingsId(instancesEntity.getSettingsId()).orElse(new SettingsEntity());
                } else {
                    settingsEntity = new SettingsEntity();
                }
                settingsEntity.setServerInstanceName(serverName);
                settingsEntity.setAdminPassword(adminPassword);
                settingsEntity.setServerPassword(password);
                settingsEntity.setSpectatorPassword(spectatorPassword);
                settingsEntity.setMaxCarSlots(maxCarSlots);
                settingsEntity.setCarGroup("FREE_FOR_ALL");
                settingsEntity.setTrackMedalsRequirement(0);
                settingsEntity.setSafetyRatingRequirement(-1);
                settingsEntity.setRaceCraftRatingRequirement(-1);
                settingsEntity.setDumpLeaderBoards(0);
                settingsEntity.setIsRaceLocked(1);
                settingsEntity.setIsPrepPhaseLocked(0);
                settingsEntity.setRandomizeTrackWhenEmpty(0);
                settingsEntity.setCentralEntryListPath("");
                settingsEntity.setAllowAutoDq(0);
                settingsEntity.setShortFormationLap(1);
                settingsEntity.setDumpEntryList(0);
                settingsEntity.setFormationLapType(3);
                settingsEntity.setDoDriverSwapBroadcast(1);
                settingsEntity.setConfigVersion(1);
                settingsEntity = settingsRepository.save(settingsEntity);
                instancesEntity.setSettingsId(settingsEntity.getSettingsId());

                // Persist Config
                ConfigEntity configEntity;
                if (instancesEntity.getConfigId() != null) {
                    configEntity = configRepository.findConfigEntityByConfigId(instancesEntity.getConfigId()).orElse(new ConfigEntity());
                } else {
                    configEntity = new ConfigEntity();
                }
                configEntity.setTcpPort(tcpPort);
                configEntity.setUdpPort(udpPort);
                configEntity.setMaxConnections(maxConnections);
                configEntity.setLanDiscovery(1);
                configEntity.setRegisterToLobby(0);
                configEntity.setPublicIP("0");
                configEntity.setConfigVersion(1);
                configEntity = configRepository.save(configEntity);
                instancesEntity.setConfigId(configEntity.getConfigId());

                // Persist AssistRules
                AssistRulesEntity assistRulesEntity;
                if (instancesEntity.getAssistRulesId() != null) {
                    assistRulesEntity = assistRulesRepository.findAssistsEntityByAssistsId(instancesEntity.getAssistRulesId()).orElse(new AssistRulesEntity());
                } else {
                    assistRulesEntity = new AssistRulesEntity();
                }
                assistRulesEntity.setStabilityControlLevelMax(stabilityControlLevelMax);
                assistRulesEntity.setDisableAutoSteer(disableAutosteer);
                assistRulesEntity.setDisableAutoLights(disableAutoLights);
                assistRulesEntity.setDisableAutoWiper(disableAutoWiper);
                assistRulesEntity.setDisableAutoEngineStart(disableAutoEngineStart);
                assistRulesEntity.setDisableAutoPitLimiter(disableAutoPitLimiter);
                assistRulesEntity.setDisableAutoGear(disableAutoGear);
                assistRulesEntity.setDisableAutoClutch(disableAutoClutch);
                assistRulesEntity.setDisableIdealLine(disableIdealLine);
                assistRulesEntity = assistRulesRepository.save(assistRulesEntity);
                instancesEntity.setAssistRulesId(assistRulesEntity.getAssistsId());

                instancesRepository.save(instancesEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                instancesRepository.findById(id).ifPresent(entity -> {
                    entity.setInstanceName(serverName);
                    instancesRepository.save(entity);
                });
            } catch (Exception e) {
                // Ignore
            }
        }

        return "redirect:/web/servers";
    }

    private boolean isInstanceRunning(String id) {
        try {
            String inspection = serverControl.inspectInstance(id);
            if (inspection != null && inspection.contains("\"containerStatus\" : \"running\"")) {
                return true;
            }
        } catch (Exception e) {
            // Ignore
        }
        return false;
    }
}