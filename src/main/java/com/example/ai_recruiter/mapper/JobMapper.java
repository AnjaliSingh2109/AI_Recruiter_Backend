package com.example.ai_recruiter.mapper;

import com.example.ai_recruiter.convertoDTO.JobDesc.Request;
import com.example.ai_recruiter.convertoDTO.JobDesc.Response;
import com.example.ai_recruiter.entity.JobDesc;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
@UtilityClass
public class JobMapper {

    public static JobDesc toEntity(Request request) {
        JobDesc entity = new JobDesc();
//        entity.setJob_id(request.getJobId());
        entity.setRole(request.getRole());
        entity.setJobId(request.getJob_Id()); // This is the business jobId string
        entity.setExperience(request.getExperience());
        entity.setJob_type(request.getJobType());
        entity.setJob_location(request.getJobLocation());
        entity.setSalary(request.getSalary());
        entity.setResponsibilities(request.getResponsibilities());
        entity.setEducational_requirements(request.getEducationalRequirements());
        entity.setTechnologies_tools(request.getTechnologiesTools());
        entity.setNo_of_openings(request.getNoOfOpenings());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public static Response toResponse(JobDesc entity) {
        Response response = new Response();
        response.setJob_Id(entity.getJobId());
        response.setRole(entity.getRole());
//        response.setJob_Id(entity.getJobId());
        response.setExperience(entity.getExperience());
        response.setJobType(entity.getJob_type());
        response.setJobLocation(entity.getJob_location());
        response.setSalary(entity.getSalary());
        response.setResponsibilities(entity.getResponsibilities());
        response.setEducationalRequirements(entity.getEducational_requirements());
        response.setTechnologiesTools(entity.getTechnologies_tools());
        response.setNoOfOpenings(entity.getNo_of_openings());
        response.setPostedBy(entity.getPosted_by());
        response.setCreatedAt(entity.getCreatedAt());
       // response.setGeneratedDescription(entity.getGeneratedDescription());
        return response;
    }

    public static Request toRequest(JobDesc entity) {
        Request request = new Request();
        //request.setJobId(entity.getJob_id());
//        request.setJob_Id(entity.getJobId());
        request.setExperience(entity.getExperience());
        request.setRole(entity.getRole());
        request.setJobType(entity.getJob_type());
        request.setJobLocation(entity.getJob_location());
        request.setSalary(entity.getSalary());
        request.setResponsibilities(entity.getResponsibilities());
        request.setEducationalRequirements(entity.getEducational_requirements());
        request.setTechnologiesTools(entity.getTechnologies_tools());
        request.setNoOfOpenings(entity.getNo_of_openings());
        return request;
    }
}
