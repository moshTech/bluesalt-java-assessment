package com.mosh.drone.dispatcher.service;

import com.mosh.drone.dispatcher.exception.ExceptionOf;
import com.mosh.drone.dispatcher.model.entity.Medication;
import com.mosh.drone.dispatcher.model.request.LoadDroneRequest;
import com.mosh.drone.dispatcher.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public List<Medication> getMedications(){
        return medicationRepository.findAll();
    }

    public List<Medication> getByIds(LoadDroneRequest loadDroneRequest){
        List<Medication> medications = medicationRepository.findByIdIn(loadDroneRequest.getMedicationIds());

        if(medications.isEmpty()){
            throw ExceptionOf.Business.BadRequest.BAD_REQUEST.exception("No valid medications passed");
        }

        if (medications.size() < loadDroneRequest.getMedicationIds().size()){
            throw ExceptionOf.Business.BadRequest.BAD_REQUEST.exception("One of the medications passed is invalid");
        }

        return medications;
    }
}
