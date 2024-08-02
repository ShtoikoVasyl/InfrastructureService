package edu.shtoiko.infrastructureservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerminalRequest {
    private String address;
    private String password;
    private String name;
}
