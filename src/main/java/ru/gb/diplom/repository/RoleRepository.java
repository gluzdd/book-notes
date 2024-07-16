package ru.gb.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.diplom.model.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
