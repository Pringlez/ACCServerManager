package org.accmanager.service.api.web;

import org.accmanager.model.Instance;
import org.accmanager.service.entity.EventEntity;
import org.accmanager.service.entity.SessionsEntity;
import org.accmanager.service.repository.EventRepository;
import org.accmanager.service.repository.SessionsRepository;
import org.accmanager.service.services.control.ServerControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web/configs")
public class Configs {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private static final String IS_DARK_MODE = "isDarkMode";

    private final EventRepository eventRepository;
    private final SessionsRepository sessionsRepository;
    private final ServerControl serverControl;

    public Configs(EventRepository eventRepository, SessionsRepository sessionsRepository, ServerControl serverControl) {
        this.eventRepository = eventRepository;
        this.sessionsRepository = sessionsRepository;
        this.serverControl = serverControl;
    }

    private void seedDefaultEventsIfEmpty() {
        if (eventRepository.count() == 0) {
            // Preset 1: Monza Wet Endurance
            EventEntity monza = new EventEntity();
            monza.setEventName("Monza 3H Wet Endurance");
            monza.setTrack("monza");
            monza.setAmbientTemp(16);
            monza.setTrackTemp(18);
            monza.setCloudLevel(0.8);
            monza.setRain(0.6);
            monza.setWeatherRandomness(4);
            monza.setPreRaceWaitingTimeSec(80);
            monza.setSessionOverTimeSec(120);
            monza.setPostQualyTimeSec(15);
            monza.setPostRaceTimeSec(30);
            EventEntity savedMonza = eventRepository.save(monza);

            SessionsEntity p1 = new SessionsEntity();
            p1.setEventId(savedMonza.getEventId());
            p1.setHourOfDay(14);
            p1.setDayOfWeekend(1);
            p1.setTimeMultiplier(1);
            p1.setSessionType("Practice");
            p1.setSessionDurationMin(60);
            sessionsRepository.save(p1);

            SessionsEntity r1 = new SessionsEntity();
            r1.setEventId(savedMonza.getEventId());
            r1.setHourOfDay(16);
            r1.setDayOfWeekend(2);
            r1.setTimeMultiplier(2);
            r1.setSessionType("Race");
            r1.setSessionDurationMin(180);
            sessionsRepository.save(r1);

            // Preset 2: Spa Sprint Championship
            EventEntity spa = new EventEntity();
            spa.setEventName("Spa Clear Sprint");
            spa.setTrack("spa");
            spa.setAmbientTemp(22);
            spa.setTrackTemp(26);
            spa.setCloudLevel(0.1);
            spa.setRain(0.0);
            spa.setWeatherRandomness(1);
            spa.setPreRaceWaitingTimeSec(60);
            spa.setSessionOverTimeSec(60);
            spa.setPostQualyTimeSec(15);
            spa.setPostRaceTimeSec(15);
            EventEntity savedSpa = eventRepository.save(spa);

            SessionsEntity q2 = new SessionsEntity();
            q2.setEventId(savedSpa.getEventId());
            q2.setHourOfDay(11);
            q2.setDayOfWeekend(1);
            q2.setTimeMultiplier(1);
            q2.setSessionType("Qualifying");
            q2.setSessionDurationMin(20);
            sessionsRepository.save(q2);

            SessionsEntity r2 = new SessionsEntity();
            r2.setEventId(savedSpa.getEventId());
            r2.setHourOfDay(14);
            r2.setDayOfWeekend(1);
            r2.setTimeMultiplier(1);
            r2.setSessionType("Race");
            r2.setSessionDurationMin(45);
            sessionsRepository.save(r2);
        }
    }

    @GetMapping
    public String start(Model model) {
        seedDefaultEventsIfEmpty();

        List<EventEntity> presets = new ArrayList<>();
        eventRepository.findAll().forEach(presets::add);

        List<Instance> instances = serverControl.getDaoService().listOfInstances();

        model.addAttribute("presets", presets);
        model.addAttribute("instances", instances);
        model.addAttribute("now", new Date().toInstant());
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/configs";
    }

    @DeleteMapping(path = "/delete/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String deletePreset(@PathVariable("id") String id) {
        eventRepository.deleteById(id);
        // Cascading session deletes optionally if database setup doesn't do it automatically:
        sessionsRepository.findSessionsEntitiesByEventId(id).ifPresent(sessionsRepository::deleteAll);
        return ""; // HTMX target closest row/card will swap to nothing (remove from DOM)
    }

    @PostMapping("/deploy")
    public String deployPreset(@RequestParam("eventId") String eventId,
                               @RequestParam("instanceId") String instanceId,
                               Model model) {
        Optional<EventEntity> eventOpt = eventRepository.findEventEntityByEventId(eventId);
        if (eventOpt.isPresent()) {
            EventEntity eventEntity = eventOpt.get();
            List<SessionsEntity> sessionsList = sessionsRepository.findSessionsEntitiesByEventId(eventId)
                    .orElse(new ArrayList<>());

            // Read existing target configuration
            Instance targetInstance = serverControl.getDaoService().readInstanceConfiguration(instanceId);

            // Translate entity to compliant model
            org.accmanager.model.Event eventModel = buildEventModel(eventEntity, sessionsList);

            // Apply new event configuration
            targetInstance.setEvent(eventModel);

            // Write filesystem configs & restart server instance
            serverControl.getDaoService().writeInstanceConfiguration(targetInstance);
            serverControl.restartInstance(instanceId);

            model.addAttribute("eventName", eventEntity.getEventName());
            model.addAttribute("serverName", targetInstance.getSettings().getServerName() != null && !targetInstance.getSettings().getServerName().isEmpty() ? targetInstance.getSettings().getServerName() : instanceId);
            model.addAttribute("success", true);
        } else {
            model.addAttribute("success", false);
            model.addAttribute("errorMsg", "Selected event preset was not found in database.");
        }
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/configs :: alert-banner";
    }

    private org.accmanager.model.Event buildEventModel(EventEntity entity, List<SessionsEntity> sessionsList) {
        org.accmanager.model.Event event = new org.accmanager.model.Event();
        event.setId(entity.getEventId());
        event.setName(entity.getEventName());

        // Safe Enum Mapping
        if (entity.getTrack() != null) {
            try {
                event.setTrack(org.accmanager.model.Event.TrackEnum.fromValue(entity.getTrack()));
            } catch (Exception e) {
                try {
                    event.setTrack(org.accmanager.model.Event.TrackEnum.valueOf(entity.getTrack().toUpperCase()));
                } catch (Exception ex) {
                    // Fallback to first available enum value if failed
                    event.setTrack(org.accmanager.model.Event.TrackEnum.values()[0]);
                }
            }
        }

        event.setPreRaceWaitingTimeSeconds(entity.getPreRaceWaitingTimeSec());
        event.setSessionOverTimeSeconds(entity.getSessionOverTimeSec());
        event.setAmbientTemp(entity.getAmbientTemp());
        event.setTrackTemp(entity.getTrackTemp());
        event.setCloudLevel((float) entity.getCloudLevel());
        event.setRain((int) entity.getRain());
        event.setWeatherRandomness(entity.getWeatherRandomness());
        event.setPostQualySeconds(entity.getPostQualyTimeSec());
        event.setPostRaceSeconds(entity.getPostRaceTimeSec());
        event.setMetaData(entity.getMetaData());

        List<org.accmanager.model.Session> sessions = new ArrayList<>();
        if (sessionsList != null) {
            for (SessionsEntity sEntity : sessionsList) {
                org.accmanager.model.Session session = new org.accmanager.model.Session();
                session.setId(sEntity.getSessionId());
                session.setHourOfDay(sEntity.getHourOfDay());
                session.setDayOfWeekend(sEntity.getDayOfWeekend());
                session.setTimeMultiplier(sEntity.getTimeMultiplier());
                sessions.add(session);
            }
        }
        event.setSessions(sessions);
        return event;
    }
}