package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);
	
	private final ComponentDependency componentDependency;

	private MyBean myBean;

	private MyBeanWithDependency myBeanWithDependency;

	private MyBeanWithProperties myBeanWithProperties;

	private UserPojo userPojo;

	private UserRepository userRepository;

	private UserService userService;

	@Autowired
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties,UserPojo userPojo,UserRepository userRepository,UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();
	}

	private void getInformationJpqlFromUser(){
		LOGGER.info("Usuario con el  método findMyUserByEmail " + userRepository.findMyUserByEmail("julie@domain.com").orElseThrow(()-> new RuntimeException("No se encontro un usuario")));

		userRepository.findByAndSort("user", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con método sort " + user));
		userRepository.findByNameLike("John")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method " +  user));

		LOGGER.info("User with method findUserByNameAndEmail: " + userRepository.findUsersByNameAndAndEmail("John", "john@domain.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario por el email dado")));
		LOGGER.info("User with method findUserByNameOrEmail: " + userRepository.findUsersByNameOrAndEmail(null, "john@domain.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario por el email dado")));
		userRepository.findByBirthDateBetween(LocalDate.of(2021, 03, 15), LocalDate.of(2021, 03, 25))
				.stream()
				.forEach(user -> LOGGER.info("User with method findByBirthDateBetween:" + user));

		userRepository.findByNameLikeOrderByIdDesc("%T%")
				.stream()
				.forEach(user -> LOGGER.info("User with method findByNameLikeOrderByIdDesc:" + user));

		LOGGER.info("User with method findMyUserByEmailNative: " + userRepository.findMyUserByEmailNative("Test5@domain.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario por el email dado")));

		userRepository.findByAndSort("Test", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("User with method findByAndSort:" + user));

		LOGGER.info("User with method findByNameOrEmail: " + userRepository.findByNameOrEmail(null, "Test5@domain.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario por el email dado")));

		LOGGER.info(userRepository.getAllByBirthdateAndEmail(LocalDate.of(2021, 03, 25),"daniela@domain.com")
				.orElseThrow(() ->
						new RuntimeException("No se encontro el usuario a partir del named parameter")));
	}

	private void saveUsersInDataBase(){
		User user1 = new User("John", "john@domain.com", LocalDate.of(2021, 03, 15));
		User user2 = new User("Julie", "julie@domain.com", LocalDate.of(2021, 03, 20));
		User user3 = new User("Daniela", "daniela@domain.com", LocalDate.of(2021, 03, 25));
		User user4 = new User("Oscar", "oscar@domain.com", LocalDate.now());
		User user5 = new User("Test1", "Test1@domain.com", LocalDate.now());
		User user6 = new User("Test2", "Test2@domain.com", LocalDate.now());
		User user7 = new User("Test3", "Test3@domain.com", LocalDate.now());
		User user8 = new User("Test4", "Test4@domain.com", LocalDate.now());
		User user9 = new User("Test5", "Test5@domain.com", LocalDate.now());
		User user10 = new User("Test6", "Test6@domain.com", LocalDate.now());
		User user11 = new User("Test7", "Test7@domain.com", LocalDate.now());
		User user12 = new User("Test8", "Test8@domain.com", LocalDate.now());
		User user13 = new User("Test9", "Test9@domain.com", LocalDate.now());
		List<User> list = Arrays.asList(user4, user1, user3, user2, user5, user6, user7, user8, user9, user10, user11, user12, user13);
		list.stream().forEach(userRepository::save);
		//postRepository.save(new Posts("Mi post test1", user12));
		//postRepository.save(new Posts("Mi post test2", user13));
		//postRepository.save(new Posts("Mi post test3", user13));
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "TestTransactional1@domain.com", LocalDate.now());
		User test2 = new User("TestTransactional2", "TestTransactional2@domain.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "TestTransactional3@domain.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "TestTransactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1,test2,test3,test4);

		try {
			userService.saveTransactional(users);

		}catch (Exception e){
			LOGGER.error("ESta es una excepcion dentro del metodo");
		}

		userService.getAllUsers().stream()
				.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transacctional " + user));


	}

	private void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + " - " + userPojo.getPassword());

		try {
			int value = 10/0;
			LOGGER.info("Mi valor: " + value);
		}catch (Exception e){
			LOGGER.error("Esto es un error al dividir por cero " + e.getMessage());
		}
	}


}
