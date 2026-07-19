package org.accmanager.service.api.web;

import org.accmanager.model.Instance;
import org.accmanager.service.services.control.ServerControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Index {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private final ServerControl serverControl;

    public Index(ServerControl serverControl) {
        this.serverControl = serverControl;
    }

    @GetMapping("/")
    public String overview(Model model) {
        List<Instance> instances = serverControl.getDaoService().listOfInstances();
        long serverCount = instances.size();
        long activeCount = instances.stream()
                .filter(inst -> isInstanceRunning(inst.getId()))
                .count();

        List<Servers.InstanceViewModel> vms = instances.stream()
                .map(inst -> new Servers.InstanceViewModel(inst, isInstanceRunning(inst.getId()) ? "running" : "stopped"))
                .collect(Collectors.toList());

        // Determine control backend type
        String controlType = serverControl.getClass().getSimpleName().replace("ControlService", "");
        if (controlType.equalsIgnoreCase("Container")) {
            controlType = "Docker Container";
        } else if (controlType.equalsIgnoreCase("Executable")) {
            controlType = "Native Executable";
        }

        // Dynamically compute host hardware load using JVM OperatingSystemMXBean Extensions
        double cpuLoad = 0.0;
        long totalPhysicalMemory = 0;
        long freePhysicalMemory = 0;
        try {
            java.lang.management.OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunBean = (com.sun.management.OperatingSystemMXBean) osBean;
                cpuLoad = sunBean.getCpuLoad() * 100.0;
                totalPhysicalMemory = sunBean.getTotalPhysicalMemorySize() / (1024 * 1024); // MB
                freePhysicalMemory = sunBean.getFreePhysicalMemorySize() / (1024 * 1024);   // MB
            }
        } catch (Exception e) {
            // Ignore
        }

        // Fallback to JVM heap if physical metrics are unavailable
        if (totalPhysicalMemory <= 0) {
            totalPhysicalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            freePhysicalMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        }
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;

        if (Double.isNaN(cpuLoad) || cpuLoad <= 0.0) {
            cpuLoad = 1.2; // default idle load
        }

        model.addAttribute("now", new Date());
        model.addAttribute("isDarkMode", darkMode);
        model.addAttribute("serverCount", serverCount);
        model.addAttribute("activeServerCount", activeCount);
        model.addAttribute("instances", vms);
        model.addAttribute("controlType", controlType);
        model.addAttribute("cpuLoad", String.format("%.1f", cpuLoad));
        model.addAttribute("usedMem", usedPhysicalMemory);
        model.addAttribute("totalMem", totalPhysicalMemory);

        return "index";
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
