package com.lec2.task2.xmlTask;

public class AccountServiceImpl implements UserService  {
	
	
	 private PersonService personService;

    public AccountServiceImpl(PersonService personService) {
        this.personService = personService;
    }
	
	@Override
	public void save(String name) {
        personService.save(name);
	}

	@Override
	public void update(String name) {
        personService.update(name);
	}
	

}
