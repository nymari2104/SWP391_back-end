package com.example.demo.repository;

import com.example.demo.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface KoiRepository extends JpaRepository<Koi, Integer> {
    boolean findByName(String name);

}
