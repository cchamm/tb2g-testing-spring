package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService service;

    @InjectMocks
    VetController controller;


    Map<String, Object> map = new HashMap<>();

    List<Vet> vets = new ArrayList<>();

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        map = new HashMap<>();
        vets = new ArrayList<>();
        vets.add(new Vet());
        vets.add(new Vet());

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testControllerShowVetList() throws Exception {
        mockMvc.perform(get("/vets.html"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vets"))
                .andExpect(view().name("vets/vetList"));
    }

    @Test
    void showVetList() {

        // given
        given(service.findVets()).willReturn(vets);

        // when
        var result = controller.showVetList(map);

        // then
        assertThat(result).isEqualToIgnoringCase("vets/vetList");
        assertThat(map).isNotEmpty();
        assertThat(map).containsKeys("vets");
        assertThat(map.get("vets")).isOfAnyClassIn(Vets.class);
        assertThat(((Vets)map.get("vets")).getVetList()).isEqualTo(vets);

    }

    @Test
    void showResourcesVetList() {
        // given
        given(service.findVets()).willReturn(vets);

        // when
        Vets vetsReturn = controller.showResourcesVetList();

        // then
        assertThat(vetsReturn).isNotNull();
        assertThat(vetsReturn.getVetList()).isEqualTo(vets);
    }
}