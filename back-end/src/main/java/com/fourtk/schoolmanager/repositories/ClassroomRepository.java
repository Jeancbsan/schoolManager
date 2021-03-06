package com.fourtk.schoolmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fourtk.schoolmanager.entities.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long>{

}
