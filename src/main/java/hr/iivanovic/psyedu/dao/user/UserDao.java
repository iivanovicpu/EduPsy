package hr.iivanovic.psyedu.dao.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import hr.iivanovic.psyedu.dao.Dao;
import hr.iivanovic.psyedu.entity.User;


public interface UserDao extends Dao<User, Long>, UserDetailsService {

    User findByName(String name);

}