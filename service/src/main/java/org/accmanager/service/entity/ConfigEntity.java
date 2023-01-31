package org.accmanager.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIG")
public class ConfigEntity {

    private String configId;
    private int udpPort;
    private int tcpPort;
    private int maxConnections;
    private int lanDiscovery;
    private int registerToLobby;
    private String publicIP;
    private int configVersion;

    @Id
    @Column(name = "CONFIG_ID")
    public String getConfigId() {
        return configId;
    }

    public void setConfigId(Object String) {
        this.configId = configId;
    }

    @Basic
    @Column(name = "UDP_PORT")
    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    @Basic
    @Column(name = "TCP_PORT")
    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    @Basic
    @Column(name = "MAX_CONNECTIONS")
    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    @Basic
    @Column(name = "LAN_DISCOVERY")
    public int getLanDiscovery() {
        return lanDiscovery;
    }

    public void setLanDiscovery(int lanDiscovery) {
        this.lanDiscovery = lanDiscovery;
    }

    @Basic
    @Column(name = "REGISTER_TO_LOBBY")
    public int getRegisterToLobby() {
        return registerToLobby;
    }

    public void setRegisterToLobby(int registerToLobby) {
        this.registerToLobby = registerToLobby;
    }

    @Basic
    @Column(name = "PUBLIC_IP")
    public String getPublicIP() {
        return publicIP;
    }

    public void setPublicIP(String registerToLobby) {
        this.publicIP = publicIP;
    }

    @Basic
    @Column(name = "CONFIG_VERSION")
    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }
}
