package com.chinagps.fee.dao;

import com.chinagps.fee.entity.ldap.Users;

public interface UsersDao{
	public Users findByName(String name);
}
