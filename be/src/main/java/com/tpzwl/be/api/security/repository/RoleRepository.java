package com.tpzwl.be.api.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tpzwl.be.api.security.model.EnumRole;
import com.tpzwl.be.api.security.model.Permission;
import com.tpzwl.be.api.security.model.Role;
import com.tpzwl.be.api.security.model.RoleCount;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(EnumRole name);

	@Query(nativeQuery = true)
	List<RoleCount> countByRole();
	
	@Query(nativeQuery = true)
	List<Permission> findMenuTreeByRoleIds(@Param("roleIds") List<Long> roleIds);
}
