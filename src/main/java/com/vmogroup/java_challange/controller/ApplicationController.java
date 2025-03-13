package com.vmogroup.java_challange.controller;

import com.vmogroup.java_challange.bean.dto.ApplicationDto;
import com.vmogroup.java_challange.bean.dto.ApplicationRequest;
import com.vmogroup.java_challange.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<PagedModel<ApplicationDto>> getAllApplications(
            @RequestParam(required = false)String text,
            @RequestParam(required = false)String description,
            @RequestParam(required = false)String type,
            @RequestParam(required = false)Boolean enabled,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PagedModel<ApplicationDto> serviceResponse = applicationService.getAllApplications(text, type, enabled, description, page, size);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable String id) {
        ApplicationDto serviceResponse = applicationService.getApplicationById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping
    public ResponseEntity<ApplicationDto> createApplication(@RequestBody ApplicationRequest applicationRequest) {
        return ResponseEntity.ok(applicationService.createApplication(applicationRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationDto> updateApplication(@PathVariable String id, @RequestBody ApplicationRequest applicationRequest) {
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationRequest));
    }
}
