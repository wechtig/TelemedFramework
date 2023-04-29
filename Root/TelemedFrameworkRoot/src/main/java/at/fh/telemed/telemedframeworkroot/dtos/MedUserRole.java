package at.fh.telemed.telemedframeworkroot.dtos;

import java.util.UUID;

public class MedUserRole {
    private UUID uid;
    private String role;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
