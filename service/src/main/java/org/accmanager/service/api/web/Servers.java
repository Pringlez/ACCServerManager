package org.accmanager.service.api.web;

import org.accmanager.model.*;
import org.accmanager.service.repository.*;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.control.ServerControlManager;
import org.accmanager.service.services.dao.InstanceDaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
    public String start(@RequestParam(required = false) String message,
                        @RequestParam(required = false) String dbMessage,
                        Model model) {
        List<InstanceViewModel> vms = serverControl.getDaoService().listOfInstances().stream()
                .map(inst -> new InstanceViewModel(inst, isInstanceRunning(inst.getId()) ? "running" : "stopped"))
                .collect(Collectors.toList());

        model.addAttribute("instances", vms);
        model.addAttribute("now", new Date().toInstant());
        model.addAttribute(IS_DARK_MODE, darkMode);
        if (message != null) {
            model.addAttribute("message", message);
        }
        if (dbMessage != null) {
            model.addAttribute("dbMessage", dbMessage);
        }
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

        boolean dbUpdated = ((InstanceDaoService) serverControl.getDaoService()).syncConfiguration(instance, persistToDb);

        String message = "Configuration saved to file successfully.";
        String dbMessage = dbUpdated ? "Existing database entry updated successfully." : null;

        String redirectUrl = "redirect:/web/servers?message=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
        if (dbMessage != null) {
            redirectUrl += "&dbMessage=" + java.net.URLEncoder.encode(dbMessage, java.nio.charset.StandardCharsets.UTF_8);
        }
        return redirectUrl;
    }

    // ==================== ENTRY LIST CRUD ====================

    @GetMapping("/{id}/edit/entrylist")
    public String editEntryList(@PathVariable("id") String id, Model model) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        model.addAttribute("instance", instance);
        model.addAttribute("isDarkMode", darkMode);
        model.addAttribute("now", new Date().toInstant());
        return "pages/general/servers-entrylist";
    }

    @GetMapping("/{id}/edit/entrylist/add")
    public String addEntryForm(@PathVariable("id") String id, Model model) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        model.addAttribute("instance", instance);
        model.addAttribute("index", -1);
        model.addAttribute("entry", new Entry());
        model.addAttribute("driver", new Driver());
        model.addAttribute("isDarkMode", darkMode);
        model.addAttribute("now", new Date().toInstant());
        return "pages/general/servers-entrylist-form";
    }

    @GetMapping("/{id}/edit/entrylist/edit/{index}")
    public String editEntryForm(@PathVariable("id") String id, @PathVariable("index") int index, Model model) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        model.addAttribute("instance", instance);
        model.addAttribute("index", index);
        
        Entry entry = new Entry();
        Driver driver = new Driver();
        if (instance.getEntriesList() != null && instance.getEntriesList().getEntries() != null && index < instance.getEntriesList().getEntries().size()) {
            entry = instance.getEntriesList().getEntries().get(index);
            if (entry.getDrivers() != null && !entry.getDrivers().isEmpty()) {
                driver = entry.getDrivers().get(0);
            }
        }
        
        model.addAttribute("entry", entry);
        model.addAttribute("driver", driver);
        model.addAttribute("isDarkMode", darkMode);
        model.addAttribute("now", new Date().toInstant());
        return "pages/general/servers-entrylist-form";
    }

    @PostMapping("/{id}/edit/entrylist/save")
    public String saveEntry(@PathVariable("id") String id,
                            @RequestParam(required = false, defaultValue = "-1") int index,
                            @RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam String shortName,
                            @RequestParam String playerID,
                            @RequestParam int driverCategory,
                            @RequestParam String customCar,
                            @RequestParam int raceNumber,
                            @RequestParam int ballastKg,
                            @RequestParam int restrictor,
                            @RequestParam int isServerAdmin,
                            Model model) {
        // Enforce validations
        if (firstName.length() > 64 || lastName.length() > 64 || customCar.length() > 64) {
            model.addAttribute("error", "Input values exceed maximum length of 64 characters.");
            Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
            model.addAttribute("instance", instance);
            model.addAttribute("index", index);
            Driver d = new Driver();
            d.setFirstName(firstName); d.setLastName(lastName); d.setShortName(shortName); d.setPlayerID(playerID); d.setDriverCategory(driverCategory);
            Entry e = new Entry();
            e.setCustomCar(customCar); e.setRaceNumber(raceNumber); e.setBallastKg(ballastKg); e.setRestrictor(restrictor); e.setIsServerAdmin(isServerAdmin);
            model.addAttribute("driver", d);
            model.addAttribute("entry", e);
            model.addAttribute("isDarkMode", darkMode);
            model.addAttribute("now", new Date().toInstant());
            return "pages/general/servers-entrylist-form";
        }

        if (raceNumber < 1 || raceNumber > 999 || ballastKg < 0 || ballastKg > 100 || restrictor < 0 || restrictor > 100) {
            model.addAttribute("error", "Numeric values are out of allowed ranges.");
            Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
            model.addAttribute("instance", instance);
            model.addAttribute("index", index);
            Driver d = new Driver();
            d.setFirstName(firstName); d.setLastName(lastName); d.setShortName(shortName); d.setPlayerID(playerID); d.setDriverCategory(driverCategory);
            Entry e = new Entry();
            e.setCustomCar(customCar); e.setRaceNumber(raceNumber); e.setBallastKg(ballastKg); e.setRestrictor(restrictor); e.setIsServerAdmin(isServerAdmin);
            model.addAttribute("driver", d);
            model.addAttribute("entry", e);
            model.addAttribute("isDarkMode", darkMode);
            model.addAttribute("now", new Date().toInstant());
            return "pages/general/servers-entrylist-form";
        }

        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        if (instance.getEntriesList() == null) {
            instance.setEntriesList(new EntriesList());
        }
        if (instance.getEntriesList().getEntries() == null) {
            instance.getEntriesList().setEntries(new java.util.ArrayList<>());
        }

        Driver driver = new Driver();
        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setShortName(shortName);
        driver.setPlayerID(playerID);
        driver.setDriverCategory(driverCategory);

        Entry entry;
        if (index >= 0 && index < instance.getEntriesList().getEntries().size()) {
            entry = instance.getEntriesList().getEntries().get(index);
        } else {
            entry = new Entry();
            instance.getEntriesList().getEntries().add(entry);
        }

        entry.getDrivers().clear();
        entry.getDrivers().add(driver);
        entry.setCustomCar(customCar);
        entry.setRaceNumber(raceNumber);
        entry.setBallastKg(ballastKg);
        entry.setRestrictor(restrictor);
        entry.setIsServerAdmin(isServerAdmin);

        // Standard: Always keep filesystem and DB in sync if a DB entry already exists
        boolean dbUpdated = ((InstanceDaoService) serverControl.getDaoService()).syncConfiguration(instance, false);

        String message = "Driver entry saved successfully.";
        String dbMessage = dbUpdated ? "Existing database entry updated successfully." : null;

        String redirectUrl = "redirect:/web/servers?message=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
        if (dbMessage != null) {
            redirectUrl += "&dbMessage=" + java.net.URLEncoder.encode(dbMessage, java.nio.charset.StandardCharsets.UTF_8);
        }
        return redirectUrl;
    }

    @PostMapping("/{id}/edit/entrylist/delete/{index}")
    public String deleteEntry(@PathVariable("id") String id, @PathVariable("index") int index) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        if (instance.getEntriesList() != null && instance.getEntriesList().getEntries() != null && index >= 0 && index < instance.getEntriesList().getEntries().size()) {
            instance.getEntriesList().getEntries().remove(index);
        }

        boolean dbUpdated = ((InstanceDaoService) serverControl.getDaoService()).syncConfiguration(instance, false);

        String message = "Driver entry deleted successfully.";
        String dbMessage = dbUpdated ? "Existing database entry updated successfully." : null;

        String redirectUrl = "redirect:/web/servers?message=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
        if (dbMessage != null) {
            redirectUrl += "&dbMessage=" + java.net.URLEncoder.encode(dbMessage, java.nio.charset.StandardCharsets.UTF_8);
        }
        return redirectUrl;
    }

    // ==================== BOP CRUD ====================

    @GetMapping("/{id}/edit/bop")
    public String editBop(@PathVariable("id") String id, Model model) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        model.addAttribute("instance", instance);
        model.addAttribute("isDarkMode", darkMode);
        model.addAttribute("now", new Date().toInstant());
        return "pages/general/servers-bop";
    }

    @PostMapping("/{id}/edit/bop/save")
    public String saveBopGlobal(@PathVariable("id") String id,
                                @RequestParam int disableAutosteer,
                                @RequestParam int disableAutoLights,
                                @RequestParam int disableAutoWiper) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        if (instance.getBop() == null) {
            instance.setBop(new BoP());
        }
        instance.getBop().setDisableAutosteer(disableAutosteer);
        instance.getBop().setDisableAutoLights(disableAutoLights);
        instance.getBop().setDisableAutoWiper(disableAutoWiper);

        boolean dbUpdated = ((InstanceDaoService) serverControl.getDaoService()).syncConfiguration(instance, false);

        String message = "Global BoP settings saved successfully.";
        String dbMessage = dbUpdated ? "Existing database entry updated successfully." : null;

        String redirectUrl = "redirect:/web/servers?message=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
        if (dbMessage != null) {
            redirectUrl += "&dbMessage=" + java.net.URLEncoder.encode(dbMessage, java.nio.charset.StandardCharsets.UTF_8);
        }
        return redirectUrl;
    }

    @PostMapping("/{id}/edit/bop/add")
    public String addBopEntry(@PathVariable("id") String id,
                              @RequestParam String track,
                              @RequestParam int carModel,
                              @RequestParam int ballastKg,
                              @RequestParam int restrictor,
                              Model model) {
        // Enforce validations
        if (carModel < 0 || carModel > 100 || ballastKg < 0 || ballastKg > 100 || restrictor < 0 || restrictor > 100) {
            model.addAttribute("error", "BoP override parameters are out of allowed ranges.");
            Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
            model.addAttribute("instance", instance);
            model.addAttribute("isDarkMode", darkMode);
            model.addAttribute("now", new Date().toInstant());
            return "pages/general/servers-bop";
        }

        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        if (instance.getBop() == null) {
            instance.setBop(new BoP());
        }
        if (instance.getBop().getEntries() == null) {
            instance.getBop().setEntries(new java.util.ArrayList<>());
        }

        EntryBoP bopEntry = new EntryBoP();
        try {
            bopEntry.setTrack(org.accmanager.model.EntryBoP.TrackEnum.fromValue(track));
        } catch (Exception e) {
            try {
                bopEntry.setTrack(org.accmanager.model.EntryBoP.TrackEnum.valueOf(track.toUpperCase()));
            } catch (Exception ex) {
                bopEntry.setTrack(org.accmanager.model.EntryBoP.TrackEnum.values()[0]);
            }
        }
        bopEntry.setCarModel(carModel);
        bopEntry.setBallastKg(ballastKg);
        bopEntry.setRestrictor(restrictor);

        instance.getBop().getEntries().add(bopEntry);

        boolean dbUpdated = ((InstanceDaoService) serverControl.getDaoService()).syncConfiguration(instance, false);

        String message = "Track BoP override entry added successfully.";
        String dbMessage = dbUpdated ? "Existing database entry updated successfully." : null;

        String redirectUrl = "redirect:/web/servers?message=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
        if (dbMessage != null) {
            redirectUrl += "&dbMessage=" + java.net.URLEncoder.encode(dbMessage, java.nio.charset.StandardCharsets.UTF_8);
        }
        return redirectUrl;
    }

    @PostMapping("/{id}/edit/bop/delete/{index}")
    public String deleteBopEntry(@PathVariable("id") String id, @PathVariable("index") int index) {
        Instance instance = serverControl.getDaoService().readInstanceConfiguration(id);
        if (instance.getBop() != null && instance.getBop().getEntries() != null && index >= 0 && index < instance.getBop().getEntries().size()) {
            instance.getBop().getEntries().remove(index);
        }

        boolean dbUpdated = ((InstanceDaoService) serverControl.getDaoService()).syncConfiguration(instance, false);

        String message = "Track BoP override entry deleted successfully.";
        String dbMessage = dbUpdated ? "Existing database entry updated successfully." : null;

        String redirectUrl = "redirect:/web/servers?message=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
        if (dbMessage != null) {
            redirectUrl += "&dbMessage=" + java.net.URLEncoder.encode(dbMessage, java.nio.charset.StandardCharsets.UTF_8);
        }
        return redirectUrl;
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