package com.gurinder.clinicals.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gurinder.clinicals.dto.ClinicalDataRequest;
import com.gurinder.clinicals.model.ClinicalData;
import com.gurinder.clinicals.model.Patient;
import com.gurinder.clinicals.repos.ClinicalDataRepository;
import com.gurinder.clinicals.repos.PatientRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClinicalDataController {
	
	
	private ClinicalDataRepository clinicalDataRepository;
	private PatientRepository patientRepository;
	
	@Autowired
	ClinicalDataController(ClinicalDataRepository clinicalDataRepository, PatientRepository patientRepository){
		this.clinicalDataRepository = clinicalDataRepository;
		this.patientRepository = patientRepository;
	}
	
	@RequestMapping(value="/clinicals", method = RequestMethod.POST)
	public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest request) {
		Patient patient = patientRepository.findById(request.getPatientId()).get();
		ClinicalData clinicalData = new ClinicalData();
		clinicalData.setComponentName(request.getComponentName());
		clinicalData.setComponentValue(request.getComponentValue());
		clinicalData.setPatient(patient);
		return clinicalDataRepository.save(clinicalData);
	}
	
	@RequestMapping(value="/clinicals/{patientId}/{componentName}", method = RequestMethod.GET)
	public List<ClinicalData> getClinicalData(@PathVariable("patientId") int patientId, @PathVariable("componentName") String componentName){
		boolean IsBmi=false;
		if(componentName.equals("bmi")) {
			componentName = "hw";
			IsBmi=true;
		}
		List<ClinicalData> clinicalData = clinicalDataRepository.findByPatientIdAndComponentNameOrderByMeasuredDateTime(patientId,componentName);
		if(IsBmi) {
			
			ArrayList<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
			for (ClinicalData eachEntry : duplicateClinicalData) {
				if (eachEntry.getComponentName().equals("hw")) {
					String[] heightAndWeight = eachEntry.getComponentValue().split("/");
					float heightInMeters = Float.parseFloat(heightAndWeight[0]) * 0.4536F;
					float bmi = Float.parseFloat(heightAndWeight[1]) / (heightInMeters * heightInMeters);
					ClinicalData bmidata = new ClinicalData();
					bmidata.setComponentName("bmi");
					bmidata.setComponentValue(Float.toString(bmi));
					clinicalData.add(bmidata);
					clinicalData.remove(eachEntry);
				}
			}
		}
		return clinicalData;
		
	}

}
