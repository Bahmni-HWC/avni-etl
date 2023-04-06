package org.avniproject.etl.dto;


public record MediaDTO(String uuid, String url, String thumbnailUrl, String subjectTypeName, String programEnrolment, String encounterTypeName, String lastModifiedDateTime, String createdDateTime, String syncConcept1Name, String syncConcept2Name, String syncParameterValue1, String syncParameterValue2, Object address){
}