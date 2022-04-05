package com.iashandyman.IASHandyman.core.domain.technician;

public class Technician {

    private final TechnicianId id;
    private final TechnicianFirstname firstname;
    private final TechnicianLastname lastname;

    public Technician(TechnicianId id, TechnicianFirstname firstname, TechnicianLastname lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public TechnicianId getId() {
        return id;
    }

    public TechnicianFirstname getFirstname() {
        return firstname;
    }

    public TechnicianLastname getLastname() {
        return lastname;
    }
}
