package com.nrifintech.cms.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.nrifintech.cms.dtos.MenuUpdateRequest;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.MenuRepo;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Role;
import com.stripe.param.PlanCreateParams.TransformUsage.Round;

@SpringBootTest
public class MenuServiceTest {
    private List<Item> items= new ArrayList<>();
    private List<Menu> menues = new ArrayList<>();

	@Mock
	private MenuRepo menuRepo;

	@Mock
	private ItemService itemService;

    @Mock
	private UserService userService;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setup(){
        items.add(new Item(999,12,10.0,ItemType.Veg,"abc.png","tasty","food1"));
        items.add(new Item(998,13,10.0,ItemType.Veg,"abc.png","tasty","food2"));
        items.add(new Item(997,14,10.0,ItemType.Veg,"abc.png","tasty","food3"));
        items.add(new Item(996,15,10.0,ItemType.NonVeg,"abc.png","yum","food4"));
        items.add(new Item(995,16,10.0,ItemType.NonVeg,"abc.png","yum","food5"));
        items.add(new Item(994,17,10.0,ItemType.NonVeg,"abc.png","yum","food6"));
        
        menues.add(new Menu(1,Approval.Pending,new Date(System.currentTimeMillis()),MealType.Breakfast,items.subList(0, 3)));
        menues.add(new Menu(2,Approval.Pending,new Date(System.currentTimeMillis()),MealType.Lunch,items.subList(3, 6)));
        menues.add(new Menu(3,Approval.Approved,new Date(System.currentTimeMillis()),MealType.Lunch,items));
        menues.add(new Menu(4,Approval.Incomplete,new Date(System.currentTimeMillis()),MealType.Lunch,items.subList(2, 6)));
        
        // Mockito.when(itemService.saveAll(items)).thenReturn( items);
        Mockito.when(menuRepo.findAll()).thenReturn( menues);
        for(Menu i:menues){
            Mockito.when(menuRepo.save( Mockito.eq(i))).thenReturn( i);
            Mockito.when(menuRepo.findById(  Mockito.eq(i.getId()))).thenReturn( Optional.of(i));
            // Mockito.when(menuRepo.deleteById(  Mockito.eq(i.getId())));
            
        }
        Mockito.when(menuRepo.findById( Mockito.eq(11))).thenThrow(NotFoundException.class);
        Mockito.when(menuRepo.findById( Mockito.eq(12))).thenThrow(NotFoundException.class);
     
        // Mockito.when(itemService.saveAll(items)).thenReturn( items);
        // Mockito.when(itemService.findAll()).thenReturn( items);
        for(Item i:items){
            // Mockito.when(itemService.save( Mockito.eq(i))).thenReturn( i);
            Mockito.when(itemService.getItem(  Mockito.eq(i.getId()))).thenReturn( i );
        }

        
    }
    
    @AfterEach
    void destroy(){
        items.clear();
    }

    @Test
    void testAddItemToMenu() {
        //td
        // assertEquals(menues.get(0), 
        // menuService.addItemToMenu(menues.get(0).getId(), items.get(4).getId()));
        assertEquals(null, 
            menuService.addItemToMenu(menues.get(0).getId(), items.get(4).getId()));
        
        assertEquals(null, 
            menuService.addItemToMenu(menues.get(0).getId(), 8));

        assertEquals(null, 
            menuService.addItemToMenu(menues.get(0).getId(), items.get(1).getId()));
    
    }

    @Test
    void testAddItemToMenu2() {
        assertNotEquals(menues.get(0).getItems().size(), 
            menuService.addItemToMenu(new MenuUpdateRequest(
                menues.get(0).getId(),994)).getItems().size());// items.get(4).getId())));
        
        assertEquals(menues.get(0).getItems().size(), 
            menuService.addItemToMenu(new MenuUpdateRequest(
                menues.get(0).getId(), 8)).getItems().size());

        assertEquals(menues.get(0).getItems().size(), 
            menuService.addItemToMenu(new MenuUpdateRequest(
                menues.get(0).getId(), items.get(1).getId())).getItems().size());
    }

    @Test
    void testAddItemsToMenu() {
        assertEquals(menues.get(0).getItems().size()+2, 
        menuService.addItemsToMenu(
            menues.get(0).getId(),Arrays.asList("994","995"))
                .getItems().size());// items.get(4).getId())));
    
    assertEquals(menues.get(0).getItems().size(), 
        menuService.addItemsToMenu(
            menues.get(0).getId(), Arrays.asList("8")).getItems().size());

    assertEquals(menues.get(0).getItems().size(), 
        menuService.addItemsToMenu(
            menues.get(0).getId(), Arrays.asList(items.get(1).getId().toString())).getItems().size());
    }

    @Test
    void testAddMenu() {

        assertEquals(menues.get(0) , menuService.addMenu(menues.get(0)));
        assertEquals(menues.get(1) , menuService.addMenu(menues.get(1)));
        assertEquals(menues.get(2) , menuService.addMenu(menues.get(2)));
    }

    @Test
    void testApproveMenu() {
        assertEquals(menues.get(0) , menuService.approveMenu(menues.get(0), 1));
        assertEquals(null, menuService.approveMenu(menues.get(2), 2));
    }

    @Test
    void testGetAllMenu() {
        when(userService.getuser(anyString())).thenReturn(
            User.builder().email("testuser").role(Role.Admin).build());
        assertEquals(menues.size() , 
            menuService.getAllMenu(()->"testuser").size());
    }

    @Test
    void testGetMenu() {
        assertEquals(menues.get(0) , menuService.getMenu(1));
        assertEquals(menues.get(1) , menuService.getMenu(2));
        assertEquals(menues.get(2) , menuService.getMenu(3));
        NotFoundException e1 = assertThrows(NotFoundException.class, ()-> menuService.getMenu(11));
        NotFoundException e2 = assertThrows(NotFoundException.class, ()-> menuService.getMenu(12));
        // assertEquals(" not found." , e1.getMessage());
        // assertEquals(" not found." , e2.getMessage());
    }

    @Test
    void testGetMenuByDate() {
        when(userService.getuser(anyString())).thenReturn(
            User.builder().email("testuser").role(Role.Admin).build());
        List<Menu> menuOut = menuService.getMenuByDate(new Date(2023,3,28), ()->"testuser");
        assert( menuOut.stream().allMatch((e)->e.getApproval().equals(Approval.Incomplete)));
    
        when(userService.getuser(anyString())).thenReturn(
            User.builder().email("testuser").role(Role.User).build());
        menuOut = menuService.getMenuByDate(new Date(2023,3,28), ()->"testuser");
        assert( menuOut.stream().allMatch((e)->e.getApproval().equals(Approval.Approved)));
    
    }

    @Test
    void testIsServingToday() {

        assert(menuService.isServingToday(new Date(2023, 3, 17)));
        // assertFalse(menuService.isServingToday(new Date(2023, 3, 19)));

    }

    @Test
    void testIsServingToday2() {
        assert(menuService.isServingToday());//today is monday
    }

    @Test
    void testRemoveItemFromMenu() {
        
        assertEquals(menues.get(0).getItems().size()-1, 
        menuService.removeItemFromMenu(
            menues.get(0).getId(),999)
                .getItems().size());// items.get(4).getId())));
    
    assertEquals(null, 
        menuService.removeItemFromMenu(
            menues.get(0).getId(), 8));

    // Menu m1 = menuService.removeItemFromMenu(
    //     menues.get(1).getId(),999);
    // assertEquals(null, 
    //     m1);//TODO: some error
    }

    @Test
    void testRemoveMenu() {
        assertEquals(menues.get(0), menuService.removeMenu(menues.get(0).getId()).get());
        assertEquals(menues.get(1), menuService.removeMenu(menues.get(1).getId()).get());
        NotFoundException e1 = assertThrows( NotFoundException.class , ()-> menuService.removeMenu(11));
        
        Mockito.verify(menuRepo,Mockito.times(2)).deleteById(Mockito.any());
    }
}