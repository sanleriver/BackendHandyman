package com.iashandyman.IASHandyman.core.domain.service;

public class Service {

    private final ServiceId id;
    private final ServiceName name;
    private final ServiceType type;

    public Service(ServiceId id, ServiceName name, ServiceType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public ServiceId getId() {
        return id;
    }

    public ServiceName getName() {
        return name;
    }

    public ServiceType getType() {
        return type;
    }
}
