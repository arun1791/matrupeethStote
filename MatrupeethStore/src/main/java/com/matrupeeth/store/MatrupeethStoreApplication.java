package com.matrupeeth.store;

import com.matrupeeth.store.entities.Role;
import com.matrupeeth.store.repositories.RoleRepsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class MatrupeethStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MatrupeethStoreApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepsitory roleRepsitory;

	@Value("${admin.role.id}")
	private String noramlroleId;

	@Value("${normal.role.id}")
	private String adminroleId;
	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("12345678"));

		try {
//			String roleAdminId="1234abcdef";
//			String roleNormalId="1234zxcvfds";
			Role roleAdmin = Role.builder()
					.roleId(adminroleId)
					.roleName("ROLE_ADMIN")
					.build();
			Role roleNormal = Role.builder()
					.roleId(noramlroleId)
					.roleName("ROLE_NORMAL")
					.build();
			roleRepsitory.save(roleAdmin);
			roleRepsitory.save(roleNormal);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
