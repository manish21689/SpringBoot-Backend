package com.example.demo.converter;

import com.example.demo.dto.DailyLogDTO;
import com.example.demo.entity.DailyLog;

public class ConverterUtility {
	
	public DailyLogDTO convertToDTO(DailyLog log) {
	    DailyLogDTO dto = new DailyLogDTO();
	    dto.setId(log.getId());
	    dto.setActivity(log.getActivity());
	    dto.setDescription(log.getDescription());
	    dto.setDate(log.getDate());

	    if (log.getWorker() != null) {
	        dto.setWorkerId(log.getWorker().getId());
	        dto.setWorkerName(log.getWorker().getName()); // assuming getName() exists
	    }

	    if (log.getProject() != null) {
	        dto.setProjectId(log.getProject().getProjectid());
	        dto.setProjectName(log.getProject().getProjectName()); // assuming getName() exists
	    }

	    return dto;
	}


}
