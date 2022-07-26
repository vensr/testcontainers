package com.punch.testcontainers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.punch.testcontainers.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{

}
