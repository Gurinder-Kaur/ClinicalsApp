package com.gurinder.clinicals.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gurinder.clinicals.model.ClinicalData;
import com.gurinder.clinicals.model.Patient;
import com.gurinder.clinicals.repos.PatientRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {
	
	PatientRepository repository;
	Map<String,String> filters = new HashMap<>();
	
	@Autowired
	PatientController(PatientRepository repository){
		this.repository = repository;
	}
	
	@RequestMapping(value="/patients", method=RequestMethod.GET)
	public List<Patient> getPatients(){
		return repository.findAll();
	}
	
	@RequestMapping(value="/patients/{id}", method=RequestMethod.GET)
	public Patient getPatient(@PathVariable("id")int id){
		return repository.findById(id).get();
	}
	
	@RequestMapping(value="/patients", method=RequestMethod.POST)
	public Patient savePatient(@RequestBody Patient patient){
		return repository.save(patient);
	}
	
	@RequestMapping(value="/patients/analyze/{id}", method=RequestMethod.GET)
	public Patient analyze(@PathVariable("id") int id) {
		Patient patient = repository.findById(id).get();
		List<ClinicalData> clinicalData = patient.getClinicalData();
		ArrayList<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
		for(ClinicalData eachEntry:duplicateClinicalData) {
			if(filters.containsKey(eachEntry.getComponentName())) {
				clinicalData.remove(eachEntry);
				continue;
			}
			else {
				filters.put(eachEntry.getComponentName(), null);
			}
			if (eachEntry.getComponentName().equals("hw")) {
				String[] heightAndWeight = eachEntry.getComponentValue().split("/");
				float heightInMeters = Float.parseFloat(heightAndWeight[0]) * 0.4536F;
				float bmi = Float.parseFloat(heightAndWeight[1])/(heightInMeters*heightInMeters);
				ClinicalData bmidata = new ClinicalData();
				bmidata.setComponentName("bmi");
				bmidata.setComponentValue(Float.toString(bmi));
				clinicalData.add(bmidata);
			}
		}
		filters.clear();
		return patient;
	}

}
