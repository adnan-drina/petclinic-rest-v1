package org.springframework.samples.petclinic.rest;

import java.util.List;
import org.springframework.samples.petclinic.dto.VetDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VetResource {
    private final ClinicService clinicService;

    public VetResource(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/vets")
    public List<VetDto> getAllVets() {
        return List.of();
    }
}
