package org.springframework.samples.petclinic.web;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
public class OwnerControllerTest {


    public static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";



    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    @Test
    void testNewOwnerPostValid() throws Exception {


        //given
        mockMvc.perform(post("/owners/new")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("Address", "123 Duyal st ")
                .param("City", "Key West")
                .param("telephone", "3151231234")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/null"));
    }

    @Test
    void testNewOwnerPostNoValid() throws Exception {


        //given
        mockMvc.perform(post("/owners/new")
                        .param("firstName", "Jimmy")
                        .param("lastName", "Buffett")
//                        .param("Address", "123 Duyal st ")
                        .param("City", "Key West")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name(OWNERS_CREATE_OR_UPDATE_OWNER_FORM));
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testInitCreationFormMVC() throws Exception {

        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(OWNERS_CREATE_OR_UPDATE_OWNER_FORM))
                .andExpect(model().attributeExists("owner"));
    }


    @Test
    void testFindFormWithNullLastName() throws Exception {
        // given
        given(clinicService.findOwnerByLastName(stringArgumentCaptor.capture())).willReturn(
                new ArrayList<>(Arrays.asList(
                        new Owner(),
                        new Owner()
                ))
        );

        // when
        mockMvc.perform(get("/owners")
//                .param("lastName", "")
                )
        // then
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attributeExists("selections"));

        then(clinicService).should(times(1)).findOwnerByLastName("");
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("");

    }

    @Test
    void testFindFormReturnOneOwnerObject() throws Exception {
        // given
        var owner = new Owner();
        owner.setId(1);
        owner.setLastName("Tom");
        given(clinicService.findOwnerByLastName("Tom")).willReturn(
                new ArrayList<>(Arrays.asList(
                        owner
                ))
        );

        // when
        mockMvc.perform(get("/owners").param("lastName", "Tom"))
        //then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        then(clinicService).should(times(1)).findOwnerByLastName("Tom");

    }

    @Test
    void testFindFormReturnListOfOwnerObjects() throws Exception {
        // given
        var owner1 = new Owner();
        owner1.setId(1);
        owner1.setLastName("Tom");
        var owner2 = new Owner();
        owner2.setId(2);
        owner2.setLastName("Tom");
        given(clinicService.findOwnerByLastName("Tom")).willReturn(
                new ArrayList<>(Arrays.asList(
                        owner1,
                        owner2
                ))
        );

        // when
        mockMvc.perform(get("/owners").param("lastName", "Tom"))
        //then
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attributeExists("selections"));

        then(clinicService).should(times(1)).findOwnerByLastName("Tom");

    }
}
