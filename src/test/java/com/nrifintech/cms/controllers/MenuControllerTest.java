package com.nrifintech.cms.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.config.jwt.JwtResponse;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.services.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.tomcat.util.http.parser.MediaType;

@RunWith(MockitoJUnitRunner.class)
public class MenuControllerTest extends MockMvcSetup{

    
	@Mock
	private MenuService menuService;

	@Mock
	private UserService userService;

	@InjectMocks
    private MenuController menuController;

    @Test
    public void testAddItemsToMenu() {
        int menuId;
        List<Integer> itemIds;
        String r = MockMvc.perform(
            MockMvcRequestBuilders
                    .post(prefix(Route.Item.addItems + menuId + "/" + itemIds.toString().strip()) ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    //.content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        JwtResponse res = mapFromJson(r, JwtResponse.class);

        Mockito.verify(authService).authenticate(anyString(),anyString());
        assert res.getToken().equals("Added items to menu.");
    }

    @Test
    public void testApproveMenu() {

    }

    @Test
    public void testGetMenu() {

    }

    @Test
    public void testGetMenuByDate() {

    }

    @Test
    public void testGetMonthlyMenu() {

    }

    @Test
    public void testNewMenu() {

    }

    @Test
    public void testRemoveItemFromMenu() {

    }

    @Test
    public void testRemoveMenu() {

    }

    @Test
    public void testSubmitMenu() {

    }
}
