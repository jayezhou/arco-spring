package com.tpzwl.be.api.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tpzwl.be.api.security.model.Permission;

@Mapper
public interface RoleMapper {  
	List<Permission> findMenuTreeByRoleIds(@Param("roleIds") List<Long> roleIds);
}
