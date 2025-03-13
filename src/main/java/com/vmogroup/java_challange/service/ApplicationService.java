package com.vmogroup.java_challange.service;

import com.google.common.base.Strings;
import com.vmogroup.java_challange.bean.dto.ApplicationDto;
import com.vmogroup.java_challange.bean.dto.ApplicationRequest;
import com.vmogroup.java_challange.documents.Application;
import com.vmogroup.java_challange.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final MongoTemplate mongoTemplate;

    public PagedModel<ApplicationDto> getAllApplications(String name, String type, Boolean enabled, String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Criteria criteria = buildCriteria(name, description, type, enabled);

        Query countQuery = Query.query(criteria);
        Query pageQuery = Query.query(buildCriteria(name, description, type, enabled))
                .with(pageable);

        long total = mongoTemplate.count(countQuery, Application.class);
        List<Application> applicationList = mongoTemplate.find(pageQuery, Application.class);
        List<ApplicationDto> applicationDtoList = applicationList.stream().map(document -> {
            ApplicationDto applicationDto = new ApplicationDto();
            BeanUtils.copyProperties(document, applicationDto);
            return applicationDto;
        }).toList();

        return new PagedModel<>(new PageImpl<>(applicationDtoList, pageable, total)) ;
    }

    private Criteria buildCriteria(String name, String description, String type, Boolean enabled) {
        List<Criteria> criteria = new ArrayList<>();

        if (!Strings.isNullOrEmpty(name)) {
            criteria.add(Criteria.where("name").regex(".*" + name + ".*", "i"));
        }

        if (!Strings.isNullOrEmpty(description)) {
            criteria.add(Criteria.where("description").regex(".*" + description + ".*", "i"));
        }

        if (!Strings.isNullOrEmpty(type)) {
            criteria.add(Criteria.where("type").is(type));
        }

        if (!Objects.isNull(enabled)) {
            criteria.add(Criteria.where("enabled").is(enabled));
        }

        return criteria.isEmpty() ? new Criteria() : new Criteria().andOperator(criteria);
    }

    public ApplicationDto getApplicationById(String id) {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application not found");
        }

        ApplicationDto applicationDto = new ApplicationDto();
        BeanUtils.copyProperties(application.get(), applicationDto);

        return applicationDto;
    }

    public ApplicationDto createApplication(ApplicationRequest applicationRequest) {
        Application newApplication = new Application();

        BeanUtils.copyProperties(applicationRequest, newApplication);
        applicationRepository.save(newApplication);

        ApplicationDto response = new ApplicationDto();
        BeanUtils.copyProperties(newApplication, response);
        return response;
    }

    public ApplicationDto updateApplication(String id, ApplicationRequest applicationRequest) {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application not found");
        }

        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update();
        for (Method method : applicationRequest.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                try {
                    Object value = method.invoke(applicationRequest);
                    if (value != null) {
                        String fieldName = method.getName().substring("get".length()).toLowerCase();
                        update.set(fieldName, value);
                    }
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when updating field", e);
                }
            }
        }

        Application applicationUpdate =  mongoTemplate.findAndModify(
                query, update,
                FindAndModifyOptions.options().returnNew(true),
                Application.class
        );

        Objects.requireNonNull(applicationUpdate, "Application update not found");

        ApplicationDto response = new ApplicationDto();
        BeanUtils.copyProperties(applicationUpdate, response);
        return response;
    }
}
