package de.tub.ise.anwsys.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.models.Topping;
import de.tub.ise.anwsys.repos.PizzaRepository;
import de.tub.ise.anwsys.repos.ToppingRepository;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class PizzaController {

       private PizzaRepository pizzaRepository;
       private ToppingRepository toppingrepository;
       private Iterable<Pizza> p;
       private Iterable<Topping> t;
       private Iterable<Integer> pizzaid;

    @Autowired
    public PizzaController(PizzaRepository pizzaRepository,ToppingRepository toppingrepository) {
        this.pizzaRepository = pizzaRepository;
        this.toppingrepository=toppingrepository;
        
    }

 
    @RequestMapping(method = RequestMethod.POST, produces="application/json", consumes="application/json", path = "/pizza")
    @ResponseBody
    public ResponseEntity addPizza(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    		HttpHeaders responseHeaders = new HttpHeaders();
    		try {
	    	    	Pizza p=new Pizza();
	    	    	ObjectMapper mapper = new ObjectMapper();
	    	    	p = mapper.readValue(json, Pizza.class);
	    	    	pizzaRepository.save(p);   
	    	    URI u = new URI("/pizza/"+p.getId());
	    	    	responseHeaders.setLocation(u);
	    	    	responseHeaders.set("MyResponseHeader", "MyValue");
	    	    	return new ResponseEntity<String>("Created new pizza", responseHeaders, HttpStatus.CREATED);
	    	    	
    	  } catch(Exception e) {
    			return new ResponseEntity<String>("Invalid input", responseHeaders, HttpStatus.BAD_REQUEST);
    	  }
    }
    
    @RequestMapping(method = RequestMethod.GET, produces="application/json", path = "/pizza/{pizzaId}")
    @ResponseBody
    public ResponseEntity getPizzabyId(@PathVariable(value="pizzaId")int pizzaid) throws JsonParseException, JsonMappingException, IOException
    {
    	HttpHeaders responseHeaders = new HttpHeaders();
    	try
    	{
    	Pizza p=pizzaRepository.findOne(pizzaid);
    	ObjectMapper mapper = new ObjectMapper();
    String output= mapper.writeValueAsString(p);

    	return new ResponseEntity<String>(output, responseHeaders, HttpStatus.OK);
    	
    	  } catch(Exception e) {
    			return new ResponseEntity<String>("Pizza could not be found", responseHeaders, HttpStatus.NOT_FOUND);
    	  }
    }
    
    @RequestMapping(method = RequestMethod.GET, produces="application/json", path = "/pizza")
    @ResponseBody
    public ResponseEntity getPizzas() throws JsonParseException, JsonMappingException, IOException
    {	
    	HttpHeaders responseHeaders = new HttpHeaders();
		try {
    	List<Pizza> pizzas =new ArrayList<Pizza>();
    	p= pizzaRepository.findAll();
    	p.forEach(pizzas::add);
    	String[] result=new String[pizzas.size()];
     	
    	ObjectMapper mapper = new ObjectMapper();    	
    	for(int i=0;i<pizzas.size();i++)
    	{
    		result[i]= mapper.writeValueAsString(pizzas.get(i).getId());
    	}
    		return new ResponseEntity<String>(Arrays.toString(result), responseHeaders, HttpStatus.OK);
    	
  	  } catch(Exception e) {
  			return new ResponseEntity<String>("No Pizza Exists", responseHeaders, HttpStatus.NOT_FOUND);
  	  }
    }
    
    @RequestMapping(method = RequestMethod.DELETE, produces="application/json", path = "/pizza/{pizzaId}")
    @ResponseBody
    public ResponseEntity deletePizza(@PathVariable(value="pizzaId")int pizzaid) throws JsonParseException, JsonMappingException, IOException
    {
    	HttpHeaders responseHeaders = new HttpHeaders();
		try {
			if(pizzaRepository.findOne(pizzaid) == null) {
				return new ResponseEntity<String>("Pizza not found", responseHeaders, HttpStatus.NOT_FOUND);
			}
			pizzaRepository.delete(pizzaid);
			return new ResponseEntity<String>("Deleted", responseHeaders, HttpStatus.NO_CONTENT);
		}
		catch(Exception e) {
  			return new ResponseEntity<String>("Invalid ID supplied", responseHeaders, HttpStatus.BAD_REQUEST);
  	  }
		
    	
    }
    
    
    @RequestMapping(method = RequestMethod.PUT, produces="application/json",consumes="application/json", path = "/pizza/{pizzaId}")
    @ResponseBody
    public ResponseEntity updatePizza(@PathVariable(value="pizzaId")int pizzaid,@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
		{
	 	HttpHeaders responseHeaders = new HttpHeaders();
		try {
		if(pizzaRepository.findOne(pizzaid) == null) {
		return new ResponseEntity<String>("Pizza not found", responseHeaders, HttpStatus.NOT_FOUND);
		}
	    	Pizza p=pizzaRepository.findOne(pizzaid); 
	    	Pizza p1;
	    	ObjectMapper mapper = new ObjectMapper();
	    	p1 = mapper.readValue(json, Pizza.class);
	    	if(p1.getId()!=p.getId()) {
	    		throw new Exception();
	    	}
	    	pizzaRepository.save(p); 
	    	String output= mapper.writeValueAsString(p);
	    	return new ResponseEntity<String>(output, responseHeaders, HttpStatus.NO_CONTENT);
		}
		catch(Exception e) {
			return new ResponseEntity<String>("Invalid ID supplied", responseHeaders, HttpStatus.BAD_REQUEST);
	  }
    	
    }
    
    @RequestMapping(method = RequestMethod.POST,consumes="application/json", produces="application/json", path = "/pizza/{pizzaId}/topping")
    @ResponseBody
    public ResponseEntity createTopping(@PathVariable(value="pizzaId")int pizzaid,@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    	HttpHeaders responseHeaders = new HttpHeaders();
		try {
    	Topping t=new Topping();
    	Pizza p = pizzaRepository.findOne(pizzaid);
    	ObjectMapper mapper = new ObjectMapper();
    	t = mapper.readValue(json, Topping.class);   
    	t.setPizza(p);
    	toppingrepository.save(t); 
    	URI u = new URI("/pizza/"+p.getId()+"/topping");
    	responseHeaders.setLocation(u);
    	responseHeaders.set("MyResponseHeader", "MyValue");
    	return new ResponseEntity<String>("Created new Topping", responseHeaders, HttpStatus.CREATED);
	    	
} catch(Exception e) {
		return new ResponseEntity<String>("Invalid input", responseHeaders, HttpStatus.BAD_REQUEST);
}
    } 
    
    @RequestMapping(method = RequestMethod.GET, produces="application/json", path = "/pizza/{pizzaId}/topping/{toppingId}")
    @ResponseBody
    public ResponseEntity getToppingbyId(@PathVariable(value="pizzaId")int pizzaid,@PathVariable(value="toppingId")int toppingid) throws JsonParseException, JsonMappingException, IOException
    {
    	HttpHeaders responseHeaders = new HttpHeaders();
    	try
    	{
 	Topping t=toppingrepository.findOne(toppingid);
 	ObjectMapper mapper = new ObjectMapper();
   	String output= mapper.writeValueAsString(t);
   	return new ResponseEntity<String>(output, responseHeaders, HttpStatus.OK);
    	
  	  } catch(Exception e) {
  			return new ResponseEntity<String>("Topping could not be found", responseHeaders, HttpStatus.NOT_FOUND);
  	  }
    }
    
    @RequestMapping(method = RequestMethod.GET, produces="application/json", path = "/pizza/{pizzaId}/topping")
    @ResponseBody
    public ResponseEntity listToppings(@PathVariable(value="pizzaId")int pizzaid) throws JsonParseException, JsonMappingException, IOException
    {
    	HttpHeaders responseHeaders = new HttpHeaders();
		try {
    	List<Topping> toppings =new ArrayList<Topping>();
    	Pizza p = pizzaRepository.findOne(pizzaid);
    	t = toppingrepository.findByPizza(p);
    	t.forEach(toppings::add);
    	String[] result=new String[toppings.size()];
    	ObjectMapper mapper = new ObjectMapper();    	
    	for(int i=0;i<toppings.size();i++)
    	{
    		result[i]= mapper.writeValueAsString(toppings.get(i));
    	}
    	return new ResponseEntity<String>(Arrays.toString(result), responseHeaders, HttpStatus.OK);
    	
	  	  } catch(Exception e) {
	  			return new ResponseEntity<String>("No Topping Exists", responseHeaders, HttpStatus.NOT_FOUND);
	  	  }
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, produces="application/json", path = "/pizza/{pizzaId}/topping/{toppingId}")
    @ResponseBody
    public ResponseEntity deleteToppingById(@PathVariable(value="toppingId")int toppingid) throws JsonParseException, JsonMappingException, IOException
    {
      	HttpHeaders responseHeaders = new HttpHeaders();
    		try {
    			if(toppingrepository.findOne(toppingid) == null) {
    				return new ResponseEntity<String>("Topping not found", responseHeaders, HttpStatus.NOT_FOUND);
    			}
			    	toppingrepository.delete(toppingid);
			    	return new ResponseEntity<String>("Deleted", responseHeaders, HttpStatus.NO_CONTENT);
    		}
    		catch(Exception e) {
      			return new ResponseEntity<String>("Invalid ID supplied", responseHeaders, HttpStatus.BAD_REQUEST);
      	  }
    }
    
   /* @RequestMapping(method = RequestMethod.POST, produces="application/json",consumes="application/json", path = "/order")
    @ResponseBody
    public String createOrder(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    		Order o = new Order();
    		ObjectMapper mapper = new ObjectMapper();
    		o=mapper.readValue(json, Order.class);
    		orderrepository.save(o);
    		return mapper.writeValueAsString(o);
    } */
    
   /* @RequestMapping(method = RequestMethod.GET, produces="application/json",path = "/order")
    @ResponseBody
    public String getOrders(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    	Pizza p=new Pizza();
    	ObjectMapper mapper = new ObjectMapper();
    	p = mapper.readValue(json, Pizza.class);
    	return mapper.writeValueAsString(p);
    }
    
    @RequestMapping(method = RequestMethod.GET, produces="application/json",path = "/order")
    @ResponseBody
    public String getOrderById(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    	Pizza p=new Pizza();
    	ObjectMapper mapper = new ObjectMapper();
    	p = mapper.readValue(json, Pizza.class);
    	return mapper.writeValueAsString(p);
    }
    
   
    
    @RequestMapping(method = RequestMethod.DELETE, produces="application/json",path = "/order")
    @ResponseBody
    public String deleteOrder(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    	Pizza p=new Pizza();
    	ObjectMapper mapper = new ObjectMapper();
    	p = mapper.readValue(json, Pizza.class);
    	return mapper.writeValueAsString(p);
    }
    */
    
}