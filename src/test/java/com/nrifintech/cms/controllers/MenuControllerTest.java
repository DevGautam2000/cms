package com.nrifintech.cms.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.config.jwt.JwtResponse;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RunWith(MockitoJUnitRunner.class)
public class MenuControllerTest extends MockMvcSetup{
    List<Item> items=new ArrayList<>();
    List<Menu> menus = new ArrayList<>(); 
    
	@Mock
	private MenuService menuService;

	@Mock
	private UserService userService;

	@InjectMocks
    private MenuController menuController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Menu.prefix, this, menuController);
        loadData();
    }
    void loadData(){
        items.add(new Item(999,12,10.0,ItemType.Veg,"abc.png","tasty","food1"));
        items.add(new Item(998,13,10.0,ItemType.Veg,"abc.png","tasty","food2"));
        items.add(new Item(997,14,10.0,ItemType.Veg,"abc.png","tasty","food3"));
        items.add(new Item(996,15,10.0,ItemType.NonVeg,"abc.png","yum","food4"));
        items.add(new Item(995,16,10.0,ItemType.NonVeg,"abc.png","yum","food5"));
        items.add(new Item(994,17,10.0,ItemType.NonVeg,"abc.png","yum","food6"));
        
        menus.add(new Menu(1,Approval.Incomplete,new Date(System.currentTimeMillis()),MealType.Breakfast,items.subList(0, 3)));
        menus.add(new Menu(2,Approval.Pending,new Date(System.currentTimeMillis()),MealType.Lunch,items.subList(3, 6)));
        menus.add(new Menu(3,Approval.Approved,new Date(System.currentTimeMillis()),MealType.Lunch,items));
        
        // Mockito.when(itemService.saveAll(items)).thenReturn( items);
        // Mockito.when(menuRepo.findAll()).thenReturn( menues);
        // for(Menu i:menues){
        //     Mockito.when(menuRepo.save( Mockito.eq(i))).thenReturn( i);
        //     Mockito.when(menuRepo.findById(  Mockito.eq(i.getId()))).thenReturn( Optional.of(i));
        //     // Mockito.when(menuRepo.deleteById(  Mockito.eq(i.getId())));
            
        // }
        // Mockito.when(menuRepo.findById( Mockito.eq(11))).thenThrow(NotFoundException.class);
        // Mockito.when(menuRepo.findById( Mockito.eq(12))).thenThrow(NotFoundException.class);
     
        // Mockito.when(itemService.saveAll(items)).thenReturn( items);
        // Mockito.when(itemService.findAll()).thenReturn( items);
        // for(Item i:items){
        //     // Mockito.when(itemService.save( Mockito.eq(i))).thenReturn( i);
        //     Mockito.when(itemService.getItem(  Mockito.eq(i.getId()))).thenReturn( i );
        // }
        // Mockito.when(itemService.findById( Mockito.eq(11))).thenThrow(NotFoundException.class);
        // Mockito.when(itemService.findById( Mockito.eq(12))).thenThrow(NotFoundException.class);

        // when(ItemService.getItems("abc2@gamil.com")).thenReturn( items.get(1));
        // when(ItemService.getItems("abc3@gamil.com")).thenReturn( items.get(2));
        // when(ItemService.getItems("abc4@gamil.com")).thenReturn( items.get(3));
    }
    
    // @AfterEach
    // void destroy(){
    //     items.clear();
    // }
    @Test
    public void testAddItemsToMenu() throws Exception{
        when(menuService.addItemsToMenu(Mockito.eq(1), Mockito.any())).thenReturn(menus.get(0));
        when(menuService.addItemsToMenu(Mockito.eq(2), Mockito.any())).thenReturn(null);
        when(menuService.isNotNull(menus.get(0))).thenReturn(true);
        when(menuService.isNotNull(null)).thenReturn(false);

        int menuId = 1;
        List<Integer> itemIds = menus.get(0).getItems().stream()
            .map((e)->{
                return e.getId();
            }).collect(Collectors.toList());

        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .post(
                        prefix(Route.Item.addItems + "tomenu/" + menuId + "/" + 
                        "1,2") 
                    ).contentType(MediaType.APPLICATION_JSON)
                    //.content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r,  Response.JsonEntity.class);

        // Mockito.verify(menuService).addItemsToMenu(1, Mockito.any());
        // Mockito.verify(menuService).isNotNull(menus.get(0));
        assert res.getMessage().equals("Added items to menu.");

            
        menuId=2;
        r = mockMvc.perform(
            MockMvcRequestBuilders
                    .post(
                        prefix(Route.Item.addItems + "tomenu/" + menuId + "/" + 
                        "994,995,996,997,998")//itemIds.toString().replaceAll("[]", "")) 
                    ).contentType(MediaType.APPLICATION_JSON)
                    //.content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        ).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();

        res = mapFromJson(r, Response.JsonEntity.class);

        // Mockito.verify(menuService).addItemsToMenu(1, Mockito.any());
        // Mockito.verify(menuService).isNotNull(menus.get(0));
        assert res.getMessage().equals("Error adding items to menu.");
    }

    @Test
    public void testApproveMenu() {

    }

    @Test
    public void testGetMenu() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        //httpStatus OK
        when(menuService.getMenu(Mockito.anyInt())).thenReturn(menus.get(0));

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Menu.getMenu + "/" + "1")
                ).contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(menus.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Menu res = mapFromJson(r, Menu.class);

        assertEquals( menus.get(0).getId() , res.getId());
        Mockito.verify(menuService,times(1)).getMenu(Mockito.any());
    
    }

    @Test
    public void testGetMenuByDate() {

    }

    @Test
    public void testGetMonthlyMenu() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        //httpStatus OK
        when(menuService.getAllMenu(Mockito.any())).thenReturn(menus);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Menu.getMonthMenu)
                ).contentType(MediaType.APPLICATION_JSON)
                .principal(()->"abc@gmail.com")// .content(mapToJson(menus.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Menu> res = mapFromJson(r, List.class);

        // assertEquals( menus.get(0).getId() , res.getId());
        Mockito.verify(menuService,times(1)).getAllMenu(Mockito.any());

    }

    @Test
    public void testNewMenu() throws UnsupportedEncodingException, Exception {
        //httpStatus OK
        when(menuService.addMenu(Mockito.any())).thenReturn(menus.get(0));
        Mockito.when(menuService.isNotNull(any())).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix(Route.Menu.addMenu)
                ).contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(menus.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertEquals( "Added new menu." , res.getMessage());
        // Mockito.verify(menuService).addMenu(Mockito.any());
        // Mockito.verify(menuService).isNotNull(Mockito.any());
    

        //httpStatus INTERNAL_SERVER_ERROR
        when(menuService.addMenu(Mockito.any())).thenReturn(menus.get(0));
        Mockito.when(menuService.isNotNull(any())).thenReturn(false);

        r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix(Route.Menu.addMenu)
                ).contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(menus.get(0)))
        ).andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value())).andReturn().getResponse().getContentAsString();

        res = mapFromJson(r, Response.JsonEntity.class);

        assertEquals( "Error creating menu." , res.getMessage());
        Mockito.verify(menuService,times(2)).addMenu(Mockito.any());
        Mockito.verify(menuService,times(2)).isNotNull(Mockito.any());
    }

    @Test
    public void testRemoveItemFromMenu() {

    }

    @Test
    public void testRemoveMenu() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        //httpStatus OK
        when(menuService.removeMenu(Mockito.any())).thenReturn(Optional.of(menus.get(0)));
        
        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix(Route.Menu.removeMenu + "/" + "1")
                ).contentType(MediaType.APPLICATION_JSON)
                // .content(mapToJson(menus.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertEquals( "Menu removed." , res.getMessage());
        // Mockito.verify(menuService).addMenu(Mockito.any());
        // Mockito.verify(menuService).isNotNull(Mockito.any());


        //httpStatus INTERNAL_SERVER_ERROR
        when(menuService.removeMenu(Mockito.any())).thenReturn(Optional.ofNullable(null));
        
        r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix(Route.Menu.removeMenu + "/" + "1")
                ).contentType(MediaType.APPLICATION_JSON)
                // .content(mapToJson(menus.get(0)))
        ).andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value())).andReturn().getResponse().getContentAsString();

        res = mapFromJson(r, Response.JsonEntity.class);

        assertEquals( "Error removing menu." , res.getMessage());
        Mockito.verify(menuService,times(2)).removeMenu(Mockito.any());
       
    }

    @Test
    public void testSubmitMenu() throws UnsupportedEncodingException, Exception {
        when(userService.getuser(anyString())).thenReturn(
            new User(1,"avatar.png","abc@gamil.com","password","9876543210",
            Role.Canteen,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
		when(menuService.getMenu(anyInt())).thenReturn(menus.get(0));
		when(menuService.isNotNull(any())).thenReturn(true);
        
        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix(Route.Menu.submitMenu + "/"+menus.get(0).getId())
                ).contentType(MediaType.APPLICATION_JSON)
                .principal(()->"abc@gmail.com")
                // .content(mapToJson(menus.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity response = mapFromJson(r, Response.JsonEntity.class);

		assertEquals("Menu added for review.", response.getMessage());
		assertEquals(Approval.Pending, menus.get(0).getApproval());




        // when(userService.getuser(anyString())).thenReturn(
        //     new User(1,"avatar.png","abc@gamil.com","password","9876543210",
        //     Role.Canteen,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
		// when(menuService.getMenu(anyInt())).thenReturn(menus.get(0));
		// when(menuService.isNotNull(any())).thenReturn(true);
        
        r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix(Route.Menu.submitMenu + "/"+menus.get(0).getId())
                ).contentType(MediaType.APPLICATION_JSON)
                .principal(()->"abc@gmail.com")
                // .content(mapToJson(menus.get(0)))
        ).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();

        response = mapFromJson(r, Response.JsonEntity.class);

		assert (((String)response.getMessage()).contains("Menu already "));
		assertEquals(Approval.Pending, menus.get(0).getApproval());
  
    }
}
