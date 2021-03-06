package com.fourtk.schoolmanager.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fourtk.schoolmanager.dto.SchoolDTO;
import com.fourtk.schoolmanager.entities.School;
import com.fourtk.schoolmanager.repositories.SchoolRepository;
import com.fourtk.schoolmanager.services.exceptions.DataBaseException;
import com.fourtk.schoolmanager.services.exceptions.ResourcesNotFoundException;

@Service
public class SchoolService {
	
	@Autowired
	private SchoolRepository repository;
	
	@Transactional(readOnly = true )
	public Page<SchoolDTO> findAllPager(PageRequest pageRequest){		
		Page<School> list= repository.findAll(pageRequest);
		return list.map(x -> new SchoolDTO(x));
	}
	
	@Transactional(readOnly = true ) 
	public SchoolDTO findById(Long id) {
		
		Optional<School> obj = repository.findById(id);
		School entity = obj.orElseThrow(() -> new ResourcesNotFoundException("Entity not found"));
		return new SchoolDTO(entity, entity.getStudents());		
	}
	
	@Transactional
	public SchoolDTO insert(SchoolDTO dto) {
		
		School entity = new School();
		copyTDOtoEntity(dto, entity);
		entity = repository.save(entity);
		
		return new SchoolDTO(entity);
		
	}
	
	@Transactional
	public SchoolDTO update(Long id, SchoolDTO dto) {
		
		try {
			School entity = repository.getOne(id);
			copyTDOtoEntity(dto, entity);
			entity = repository.save(entity);
			return new SchoolDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourcesNotFoundException("ID not found: " + id);
		}
	}
	
	private void copyTDOtoEntity(SchoolDTO dto, School entity) {
		
		entity.setInep(dto.getInep());
		entity.setName(dto.getName());
		entity.setAdress(dto.getAdress());
		entity.setContact(dto.getContact());
	}

	public void delete(Long id) {
		
		try {
			
			repository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourcesNotFoundException("ID not found " + id); 
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation "); 
		}
		
		
	}
	
}
