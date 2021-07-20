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
	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency,MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService){
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
	public void run(String... args){
		//ejemplosAnteriores();
		savesUsersInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("test1Transactional1", "test1ransactional@domain.com", LocalDate.now());
		User test2 = new User("test2Transactional1", "test2Transactional@domain.com", LocalDate.now());
		User test3 = new User("test3Transactional1", "test1Transactional@domain.com", LocalDate.now());
		User test4 = new User("test4Transactional1", "test4Transactional@domain.com", LocalDate.now());
		List<User> users = Arrays.asList(test1,test2,test3,test4);
		try {
			userService.saveTransactional(users);
		}catch(Exception e){
			LOGGER.error("Esta es una exception dentro del metodo transaccional" + e);
		}
		userService.getAllUsers().stream()
				.forEach(user ->
						LOGGER.info("Este es el usuario dentro del metodo transaccional"+user));
	}

	private void getInformationJpqlFromUser(){
		LOGGER.info("Usuario con el metodo findByUserEmail"+
				userRepository.findByUserEmail("julia@domain.com")
						.orElseThrow(()-> new RuntimeException("No se encontró el usuario")));
		userRepository.findAndSort("user", Sort.by("id").descending())
				.stream()
				.forEach(user->LOGGER.info("Usuario con metodo sort" + user)
				);

		userRepository.findByName("John")
				.stream()
				.forEach(user -> LOGGER.info("usuario con query method"+ user));
		LOGGER.info("Usuario con query method findByEmailAndName" + userRepository.findByEmailAndName("daniela@domain.com","Daniela")
		.orElseThrow(()-> new RuntimeException("Usuario no encontrado")));
		userRepository.findByNameLike("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike" + user));

		userRepository.findByNameOrEmail("user10", null)
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike" + user));


		userRepository
				.findByBirthDateBetween(LocalDate.of(2021,3,1), LocalDate.of(2021,4,1))
				.stream()
				.forEach(user->LOGGER.info("Usuario con intervalo de fecha"+user));

		userRepository.findByNameLikeOrderByIdDesc("user")
		.stream()
		.forEach(user -> LOGGER.info("Usuario encontrado con Like y ordenado" + user));
		LOGGER.info("El usuario a partir del named parameter es:" + userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021,07,21)
				,"daniela@domain.com")
				.orElseThrow(()->
						new RuntimeException("No se encontró el usuario a partir del named parameter")));
	}


	private void savesUsersInDataBase(){
		User user1 = new User("Jhon", "jhon@domain.com", LocalDate.of(2021,2,22));
		User user2 = new User("Jose", "jose@domain.com", LocalDate.of(2021,3,23));
		User user3 = new User("Pedro", "pedro@domain.com", LocalDate.of(2021,5,24));
		User user4 = new User("Sofia", "sofia@domain.com", LocalDate.of(2021,11,25));
		User user5 = new User("Julia", "julia@domain.com", LocalDate.of(2021,4,26));
		User user6 = new User("Karen", "karen@domain.com", LocalDate.of(2021,12,28));
		User user7 = new User("Judith", "judith@domain.com", LocalDate.of(2021,7,2));
		User user8 = new User("Antonio", "antonio@domain.com", LocalDate.of(2021,6,20));
		User user9 = new User("Mundo", "mundo@domain.com", LocalDate.of(2021,5,22));
		User user10 = new User("Homero", "homero@domain.com", LocalDate.of(2021,4,10));
		User user11= new User("Catalina", "catalina@domain.com", LocalDate.of(2021,1,22));
		User user12= new User("Esther", "esther@domain.com", LocalDate.of(2021,10,1));
		List<User> list= Arrays.asList(user1, user2, user3, user4,user5,user6,user7,user8,user9,user10,user11,user12);
		list.stream().forEach(userRepository::save);
	}
	 private void ejemplosAnteriores(){

		componentDependency.saludar();
		myBean.print();

		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail()+userPojo.getPassword());
		try{
			int value  = 10/0;
			LOGGER.debug("Mi valor: " + value);
		}catch(Exception e){
			LOGGER.error("Esto es un error del aplicativo" + e.getMessage());

		}
	}
}
