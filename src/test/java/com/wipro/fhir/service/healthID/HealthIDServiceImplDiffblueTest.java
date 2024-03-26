package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {HealthIDServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class HealthIDServiceImplDiffblueTest {
    @MockBean
    private BenHealthIDMappingRepo benHealthIDMappingRepo;

    @Autowired
    private HealthIDServiceImpl healthIDServiceImpl;

    @MockBean
    private HttpUtils httpUtils;

    /**
     * Method under test:
     * {@link HealthIDServiceImpl#mapHealthIDToBeneficiary(String)}
     */
    @Test
    void testMapHealthIDToBeneficiary() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDServiceImpl.mapHealthIDToBeneficiary(""));
    }

    /**
     * Method under test: {@link HealthIDServiceImpl#getBenHealthID(Long)}
     */
    @Test
    void testGetBenHealthID() {
        // Arrange
        when(benHealthIDMappingRepo.getHealthDetails(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        // Act
        String actualBenHealthID = healthIDServiceImpl.getBenHealthID(1L);

        // Assert
        verify(benHealthIDMappingRepo).getHealthDetails(Mockito.<Long>any());
        assertEquals("{BenHealthDetails=[]}", actualBenHealthID);
    }
}
