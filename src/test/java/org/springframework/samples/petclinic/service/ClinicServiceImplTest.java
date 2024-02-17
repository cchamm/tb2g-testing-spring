package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private VetRepository vetRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private VisitRepository visitRepository;


    @InjectMocks
    private ClinicServiceImpl service;

    @Test
    void findPetTypes() {
        // given
        var list = new ArrayList<PetType>();
        list.add(new PetType());
        list.add(new PetType());
        given(petRepository.findPetTypes()).willReturn(list);
        // when
        var petTypes = service.findPetTypes();
        // then
        assertThat(petTypes).isEqualTo(list);

    }
}