package org.choongang.admin.config.repositories;

import org.choongang.admin.config.entites.Configs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigsRepository extends JpaRepository<Configs, String> {
}
