package com.nrifintech.cms.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.nrifintech.cms.entities.Menu;

@RunWith(MockitoJUnitRunner.class)
public class MenuRepoTest {

    @Mock
    private MenuRepo menuRepo;
    

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindMenuByDate() {

        List<Menu> menus = new ArrayList<>();

        menus.add(
            Menu.builder()
            .id(12).build()
        );
        
        Mockito.when(menuRepo.findMenuByDate(any(Date.class)))
        .thenReturn(menus);

        List<Menu> expectedMenus = menuRepo.findMenuByDate(new Date(System.currentTimeMillis()));

        assertNotNull(expectedMenus);
        assertEquals(expectedMenus.size(), menus.size());
        assertEquals(expectedMenus.get(0).getId(), menus.get(0).getId());

    }
}
