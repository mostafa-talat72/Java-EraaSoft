package com.lec2.task2.javaNoXmlTask2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceImpl implements AccountService  {
	
	
	 private PersonService personService;
	@Autowired
    public AccountServiceImpl(PersonService personService) {
        this.personService = personService;
    }
	
	@Override
	public void getSavePerson(String name) {
        personService.save(name);
	}
	

}
