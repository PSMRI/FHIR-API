package com.wipro.fhir.service.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.data.e_aushdhi.EAushadhiResponse;
import com.wipro.fhir.data.e_aushdhi.E_AusdhFacilityProcessLog;
import com.wipro.fhir.data.e_aushdhi.ItemMaster;
import com.wipro.fhir.data.e_aushdhi.M_Facility;
import com.wipro.fhir.data.e_aushdhi.M_ItemCategory;
import com.wipro.fhir.data.e_aushdhi.M_ItemForm;
import com.wipro.fhir.data.e_aushdhi.M_Route;
import com.wipro.fhir.data.e_aushdhi.M_itemfacilitymapping;
import com.wipro.fhir.data.e_aushdhi.SyncDispenseDetailsRequest;
import com.wipro.fhir.data.e_aushdhi.T_PatientIssue;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.e_aushdhi.E_AusdhFacilityProcessLogRepo;
import com.wipro.fhir.repo.e_aushdhi.FacilityRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemCategoryRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemFormRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemStockEntryRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemStockExitRepo;
import com.wipro.fhir.repo.e_aushdhi.M_itemfacilitymappingRepo;
import com.wipro.fhir.repo.e_aushdhi.ParkingPlaceRepo;
import com.wipro.fhir.repo.e_aushdhi.PatientIssueRepo;
import com.wipro.fhir.repo.e_aushdhi.RouteRepo;
import com.wipro.fhir.repo.e_aushdhi.VanMasterRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EAushadhiServiceImpl.class, ItemMaster.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class EAushadhiServiceImplDiffblueTest {
    @Autowired
    private ItemMaster itemMaster;

    @MockBean
    private APIChannel aPIChannel;

    @MockBean
    private BenHealthIDMappingRepo benHealthIDMappingRepo;

    @Autowired
    private EAushadhiServiceImpl eAushadhiServiceImpl;

    @MockBean
    private E_AusdhFacilityProcessLogRepo e_AusdhFacilityProcessLogRepo;

    @MockBean
    private FacilityRepo facilityRepo;

    @MockBean
    private HttpUtils httpUtils;

    @MockBean
    private ItemCategoryRepo itemCategoryRepo;

    @MockBean
    private ItemFormRepo itemFormRepo;

    @MockBean
    private ItemRepo itemRepo;

    @MockBean
    private ItemStockEntryRepo itemStockEntryRepo;

    @MockBean
    private ItemStockExitRepo itemStockExitRepo;

    @MockBean
    private M_itemfacilitymappingRepo m_itemfacilitymappingRepo;

    @MockBean
    private ParkingPlaceRepo parkingPlaceRepo;

    @MockBean
    private PatientIssueRepo patientIssueRepo;

    @MockBean
    private RouteRepo routeRepo;

    @MockBean
    private VanMasterRepo vanMasterRepo;

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getEaushadhiStoreDetailsByFacilityID(String)}
     */
    @Test
    void testGetEaushadhiStoreDetailsByFacilityID() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("Request"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("failure"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("0"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("42"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID(""));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("EDL"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("trans_RESULT"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("00"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getEaushadhiStoreDetailsByFacilityID("0EDL"));
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStockDetailsFromEAushadhi()}
     */
    @Test
    void testGetStockDetailsFromEAushadhi() {
        // Arrange
        when(facilityRepo.getFacilityDetails()).thenReturn(new ArrayList<>());

        // Act
        eAushadhiServiceImpl.getStockDetailsFromEAushadhi();

        // Assert that nothing has changed
        verify(facilityRepo).getFacilityDetails();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStockDetailsFromEAushadhi()}
     */
    @Test
    void testGetStockDetailsFromEAushadhi2() {
        // Arrange
        when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        M_Facility m_Facility = mock(M_Facility.class);
        when(m_Facility.getFacilityID()).thenReturn(1);
        when(m_Facility.geteAushadhiFacilityId()).thenReturn(1);
        doNothing().when(m_Facility).setCreatedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setCreatedDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setDeleted(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setFacilityCode(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityDesc(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setFacilityName(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setLastModDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setModifiedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setPageNo(Mockito.<Integer>any());
        doNothing().when(m_Facility).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setProcessed(Mockito.<Character>any());
        doNothing().when(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setStatus(Mockito.<String>any());
        doNothing().when(m_Facility).setStoreType(Mockito.<String>any());
        doNothing().when(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("failure");
        m_Facility.setFacilityDesc("failure");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("failure");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(Date.class));
        m_Facility.setLocation("failure");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("failure");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("failure");
        m_Facility.setStoreType("failure");
        m_Facility.seteAushadhiFacilityId(1);

        ArrayList<M_Facility> m_FacilityList = new ArrayList<>();
        m_FacilityList.add(m_Facility);
        when(facilityRepo.getFacilityDetails()).thenReturn(m_FacilityList);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(null);

        // Act
        eAushadhiServiceImpl.getStockDetailsFromEAushadhi();

        // Assert
        verify(m_Facility, atLeast(1)).getFacilityID();
        verify(m_Facility, atLeast(1)).geteAushadhiFacilityId();
        verify(m_Facility).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(m_Facility).setCreatedDate(Mockito.<Date>any());
        verify(m_Facility).setDeleted(Mockito.<Boolean>any());
        verify(m_Facility).setFacilityCode(eq("failure"));
        verify(m_Facility).setFacilityDesc(eq("failure"));
        verify(m_Facility).setFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setFacilityName(eq("failure"));
        verify(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        verify(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        verify(m_Facility).setLastModDate(Mockito.<Date>any());
        verify(m_Facility).setLocation(eq("failure"));
        verify(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(m_Facility).setPageNo(Mockito.<Integer>any());
        verify(m_Facility).setPhysicalLocation(eq("failure"));
        verify(m_Facility).setProcessed(Mockito.<Character>any());
        verify(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        verify(m_Facility).setStatus(eq("failure"));
        verify(m_Facility).setStoreType(eq("failure"));
        verify(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(e_AusdhFacilityProcessLogRepo).findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any());
        verify(facilityRepo).getFacilityDetails();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStockDetailsFromEAushadhi()}
     */
    @Test
    void testGetStockDetailsFromEAushadhi3() {
        // Arrange
        E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog = new E_AusdhFacilityProcessLog();
        e_AusdhFacilityProcessLog.setAcknowledge(true);
        e_AusdhFacilityProcessLog.setAmrithFacilityId(3);
        e_AusdhFacilityProcessLog.setCreatedBy("cron_job");
        e_AusdhFacilityProcessLog.setCreatedDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setDeleted(true);
        e_AusdhFacilityProcessLog.setEaushadiFacilityId(3);
        e_AusdhFacilityProcessLog.setLastFailureDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setLastModDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setLastSuccessDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setModifiedBy("cron_job");
        e_AusdhFacilityProcessLog.setProcessed("cron_Job");
        e_AusdhFacilityProcessLog.setStockUpdateAmrit(true);
        e_AusdhFacilityProcessLog.seteLID(3);

        ArrayList<E_AusdhFacilityProcessLog> e_AusdhFacilityProcessLogList = new ArrayList<>();
        e_AusdhFacilityProcessLogList.add(e_AusdhFacilityProcessLog);
        when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any())).thenReturn(e_AusdhFacilityProcessLogList);
        M_Facility m_Facility = mock(M_Facility.class);
        when(m_Facility.getFacilityID()).thenReturn(1);
        when(m_Facility.geteAushadhiFacilityId()).thenReturn(1);
        doNothing().when(m_Facility).setCreatedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setCreatedDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setDeleted(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setFacilityCode(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityDesc(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setFacilityName(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setLastModDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setModifiedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setPageNo(Mockito.<Integer>any());
        doNothing().when(m_Facility).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setProcessed(Mockito.<Character>any());
        doNothing().when(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setStatus(Mockito.<String>any());
        doNothing().when(m_Facility).setStoreType(Mockito.<String>any());
        doNothing().when(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("failure");
        m_Facility.setFacilityDesc("failure");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("failure");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(Date.class));
        m_Facility.setLocation("failure");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("failure");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("failure");
        m_Facility.setStoreType("failure");
        m_Facility.seteAushadhiFacilityId(1);

        ArrayList<M_Facility> m_FacilityList = new ArrayList<>();
        m_FacilityList.add(m_Facility);
        when(facilityRepo.getFacilityDetails()).thenReturn(m_FacilityList);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(null);

        // Act
        eAushadhiServiceImpl.getStockDetailsFromEAushadhi();

        // Assert
        verify(m_Facility, atLeast(1)).getFacilityID();
        verify(m_Facility, atLeast(1)).geteAushadhiFacilityId();
        verify(m_Facility).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(m_Facility).setCreatedDate(Mockito.<Date>any());
        verify(m_Facility).setDeleted(Mockito.<Boolean>any());
        verify(m_Facility).setFacilityCode(eq("failure"));
        verify(m_Facility).setFacilityDesc(eq("failure"));
        verify(m_Facility).setFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setFacilityName(eq("failure"));
        verify(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        verify(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        verify(m_Facility).setLastModDate(Mockito.<Date>any());
        verify(m_Facility).setLocation(eq("failure"));
        verify(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(m_Facility).setPageNo(Mockito.<Integer>any());
        verify(m_Facility).setPhysicalLocation(eq("failure"));
        verify(m_Facility).setProcessed(Mockito.<Character>any());
        verify(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        verify(m_Facility).setStatus(eq("failure"));
        verify(m_Facility).setStoreType(eq("failure"));
        verify(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(e_AusdhFacilityProcessLogRepo).findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any());
        verify(facilityRepo).getFacilityDetails();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStockDetailsFromEAushadhi()}
     */
    @Test
    void testGetStockDetailsFromEAushadhi4() {
        // Arrange
        when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        M_Facility m_Facility = mock(M_Facility.class);
        doNothing().when(m_Facility).setCreatedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setCreatedDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setDeleted(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setFacilityCode(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityDesc(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setFacilityName(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setLastModDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setModifiedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setPageNo(Mockito.<Integer>any());
        doNothing().when(m_Facility).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setProcessed(Mockito.<Character>any());
        doNothing().when(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setStatus(Mockito.<String>any());
        doNothing().when(m_Facility).setStoreType(Mockito.<String>any());
        doNothing().when(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("failure");
        m_Facility.setFacilityDesc("failure");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("failure");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(Date.class));
        m_Facility.setLocation("failure");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("failure");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("failure");
        m_Facility.setStoreType("failure");
        m_Facility.seteAushadhiFacilityId(1);

        M_Facility m_Facility2 = new M_Facility();
        m_Facility2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility2.setCreatedDate(mock(Date.class));
        m_Facility2.setDeleted(false);
        m_Facility2.setFacilityCode("failure");
        m_Facility2.setFacilityDesc("failure");
        m_Facility2.setFacilityID(1);
        m_Facility2.setFacilityName("failure");
        m_Facility2.setFacilityTypeID(1);
        m_Facility2.setIsMainFacility(false);
        m_Facility2.setLastModDate(mock(Date.class));
        m_Facility2.setLocation("failure");
        m_Facility2.setMainFacilityID(1);
        m_Facility2.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility2.setPageNo(1);
        m_Facility2.setPhysicalLocation("failure");
        m_Facility2.setProcessed('\u0001');
        m_Facility2.setProviderServiceMapID(1);
        m_Facility2.setStatus("failure");
        m_Facility2.setStoreType("failure");
        m_Facility2.seteAushadhiFacilityId(1);

        ArrayList<M_Facility> m_FacilityList = new ArrayList<>();
        m_FacilityList.add(m_Facility2);
        m_FacilityList.add(m_Facility);
        when(facilityRepo.getFacilityDetails()).thenReturn(m_FacilityList);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(null);

        // Act
        eAushadhiServiceImpl.getStockDetailsFromEAushadhi();

        // Assert
        verify(m_Facility).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(m_Facility).setCreatedDate(Mockito.<Date>any());
        verify(m_Facility).setDeleted(Mockito.<Boolean>any());
        verify(m_Facility).setFacilityCode(eq("failure"));
        verify(m_Facility).setFacilityDesc(eq("failure"));
        verify(m_Facility).setFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setFacilityName(eq("failure"));
        verify(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        verify(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        verify(m_Facility).setLastModDate(Mockito.<Date>any());
        verify(m_Facility).setLocation(eq("failure"));
        verify(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(m_Facility).setPageNo(Mockito.<Integer>any());
        verify(m_Facility).setPhysicalLocation(eq("failure"));
        verify(m_Facility).setProcessed(Mockito.<Character>any());
        verify(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        verify(m_Facility).setStatus(eq("failure"));
        verify(m_Facility).setStoreType(eq("failure"));
        verify(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(e_AusdhFacilityProcessLogRepo).findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any());
        verify(facilityRepo).getFacilityDetails();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStockDetailsFromEAushadhi()}
     */
    @Test
    void testGetStockDetailsFromEAushadhi5() {
        // Arrange
        E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog = new E_AusdhFacilityProcessLog();
        e_AusdhFacilityProcessLog.setAcknowledge(true);
        e_AusdhFacilityProcessLog.setAmrithFacilityId(3);
        e_AusdhFacilityProcessLog.setCreatedBy("cron_job");
        e_AusdhFacilityProcessLog.setCreatedDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setDeleted(true);
        e_AusdhFacilityProcessLog.setEaushadiFacilityId(3);
        e_AusdhFacilityProcessLog.setLastFailureDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setLastModDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setLastSuccessDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog.setModifiedBy("cron_job");
        e_AusdhFacilityProcessLog.setProcessed("cron_Job");
        e_AusdhFacilityProcessLog.setStockUpdateAmrit(true);
        e_AusdhFacilityProcessLog.seteLID(3);

        E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog2 = new E_AusdhFacilityProcessLog();
        e_AusdhFacilityProcessLog2.setAcknowledge(false);
        e_AusdhFacilityProcessLog2.setAmrithFacilityId(1);
        e_AusdhFacilityProcessLog2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        e_AusdhFacilityProcessLog2.setCreatedDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog2.setDeleted(false);
        e_AusdhFacilityProcessLog2.setEaushadiFacilityId(1);
        e_AusdhFacilityProcessLog2.setLastFailureDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog2.setLastModDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog2.setLastSuccessDate(mock(Timestamp.class));
        e_AusdhFacilityProcessLog2.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        e_AusdhFacilityProcessLog2.setProcessed("failure");
        e_AusdhFacilityProcessLog2.setStockUpdateAmrit(false);
        e_AusdhFacilityProcessLog2.seteLID(1);

        ArrayList<E_AusdhFacilityProcessLog> e_AusdhFacilityProcessLogList = new ArrayList<>();
        e_AusdhFacilityProcessLogList.add(e_AusdhFacilityProcessLog2);
        e_AusdhFacilityProcessLogList.add(e_AusdhFacilityProcessLog);
        when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any())).thenReturn(e_AusdhFacilityProcessLogList);
        M_Facility m_Facility = mock(M_Facility.class);
        when(m_Facility.getFacilityID()).thenReturn(1);
        when(m_Facility.geteAushadhiFacilityId()).thenReturn(1);
        doNothing().when(m_Facility).setCreatedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setCreatedDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setDeleted(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setFacilityCode(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityDesc(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setFacilityName(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setLastModDate(Mockito.<Date>any());
        doNothing().when(m_Facility).setLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setModifiedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setPageNo(Mockito.<Integer>any());
        doNothing().when(m_Facility).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setProcessed(Mockito.<Character>any());
        doNothing().when(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setStatus(Mockito.<String>any());
        doNothing().when(m_Facility).setStoreType(Mockito.<String>any());
        doNothing().when(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("failure");
        m_Facility.setFacilityDesc("failure");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("failure");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(Date.class));
        m_Facility.setLocation("failure");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("failure");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("failure");
        m_Facility.setStoreType("failure");
        m_Facility.seteAushadhiFacilityId(1);

        ArrayList<M_Facility> m_FacilityList = new ArrayList<>();
        m_FacilityList.add(m_Facility);
        when(facilityRepo.getFacilityDetails()).thenReturn(m_FacilityList);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(null);

        // Act
        eAushadhiServiceImpl.getStockDetailsFromEAushadhi();

        // Assert
        verify(m_Facility, atLeast(1)).getFacilityID();
        verify(m_Facility, atLeast(1)).geteAushadhiFacilityId();
        verify(m_Facility).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(m_Facility).setCreatedDate(Mockito.<Date>any());
        verify(m_Facility).setDeleted(Mockito.<Boolean>any());
        verify(m_Facility).setFacilityCode(eq("failure"));
        verify(m_Facility).setFacilityDesc(eq("failure"));
        verify(m_Facility).setFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setFacilityName(eq("failure"));
        verify(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        verify(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        verify(m_Facility).setLastModDate(Mockito.<Date>any());
        verify(m_Facility).setLocation(eq("failure"));
        verify(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(m_Facility).setPageNo(Mockito.<Integer>any());
        verify(m_Facility).setPhysicalLocation(eq("failure"));
        verify(m_Facility).setProcessed(Mockito.<Character>any());
        verify(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        verify(m_Facility).setStatus(eq("failure"));
        verify(m_Facility).setStoreType(eq("failure"));
        verify(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(e_AusdhFacilityProcessLogRepo).findByAmrithFacilityIdAndEaushadiFacilityId(Mockito.<Integer>any(),
                Mockito.<Integer>any());
        verify(facilityRepo).getFacilityDetails();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService() throws FHIRException {
        // Arrange
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(null);

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService2() throws FHIRException {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
        verify(responseEntity, atLeast(1)).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService3() throws FHIRException {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("https://example.org/example");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService4() throws FHIRException {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("foo");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService5() throws FHIRException {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("42");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService6() throws FHIRException {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService7() throws FHIRException {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.hasBody()).thenReturn(false);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
        verify(responseEntity).hasBody();
        verify(responseEntity, atLeast(1)).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getStoreStockDetailsService(M_Facility)}
     */
    @Test
    void testGetStoreStockDetailsService8() throws FHIRException {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("https://example.org/example");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);
        M_Facility facilityRecords = mock(M_Facility.class);
        when(facilityRecords.geteAushadhiFacilityId()).thenReturn(1);
        doNothing().when(facilityRecords).setCreatedBy(Mockito.<String>any());
        doNothing().when(facilityRecords).setCreatedDate(Mockito.<Date>any());
        doNothing().when(facilityRecords).setDeleted(Mockito.<Boolean>any());
        doNothing().when(facilityRecords).setFacilityCode(Mockito.<String>any());
        doNothing().when(facilityRecords).setFacilityDesc(Mockito.<String>any());
        doNothing().when(facilityRecords).setFacilityID(Mockito.<Integer>any());
        doNothing().when(facilityRecords).setFacilityName(Mockito.<String>any());
        doNothing().when(facilityRecords).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(facilityRecords).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(facilityRecords).setLastModDate(Mockito.<Date>any());
        doNothing().when(facilityRecords).setLocation(Mockito.<String>any());
        doNothing().when(facilityRecords).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(facilityRecords).setModifiedBy(Mockito.<String>any());
        doNothing().when(facilityRecords).setPageNo(Mockito.<Integer>any());
        doNothing().when(facilityRecords).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(facilityRecords).setProcessed(Mockito.<Character>any());
        doNothing().when(facilityRecords).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(facilityRecords).setStatus(Mockito.<String>any());
        doNothing().when(facilityRecords).setStoreType(Mockito.<String>any());
        doNothing().when(facilityRecords).seteAushadhiFacilityId(Mockito.<Integer>any());
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.getStoreStockDetailsService(facilityRecords));
        verify(facilityRecords).geteAushadhiFacilityId();
        verify(facilityRecords).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(facilityRecords).setCreatedDate(Mockito.<Date>any());
        verify(facilityRecords).setDeleted(Mockito.<Boolean>any());
        verify(facilityRecords).setFacilityCode(eq("Facility Code"));
        verify(facilityRecords).setFacilityDesc(eq("Facility Desc"));
        verify(facilityRecords).setFacilityID(Mockito.<Integer>any());
        verify(facilityRecords).setFacilityName(eq("Facility Name"));
        verify(facilityRecords).setFacilityTypeID(Mockito.<Integer>any());
        verify(facilityRecords).setIsMainFacility(Mockito.<Boolean>any());
        verify(facilityRecords).setLastModDate(Mockito.<Date>any());
        verify(facilityRecords).setLocation(eq("Location"));
        verify(facilityRecords).setMainFacilityID(Mockito.<Integer>any());
        verify(facilityRecords).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(facilityRecords).setPageNo(Mockito.<Integer>any());
        verify(facilityRecords).setPhysicalLocation(eq("Physical Location"));
        verify(facilityRecords).setProcessed(Mockito.<Character>any());
        verify(facilityRecords).setProviderServiceMapID(Mockito.<Integer>any());
        verify(facilityRecords).setStatus(eq("Status"));
        verify(facilityRecords).setStoreType(eq("Store Type"));
        verify(facilityRecords).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#insertItemsInAmrit(EAushadhiResponse, M_Facility)}
     */
    @Test
    void testInsertItemsInAmrit() throws Exception {
        // Arrange
        M_ItemForm m_ItemForm = new M_ItemForm();
        m_ItemForm.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_ItemForm.setCreatedDate(mock(Date.class));
        m_ItemForm.setDeleted(true);
        m_ItemForm.setItemForm("Item Form");
        m_ItemForm.setItemFormCode("Item Form Code");
        m_ItemForm.setItemFormDesc("Item Form Desc");
        m_ItemForm.setItemFormID(1);
        m_ItemForm.setLastModDate(mock(Date.class));
        m_ItemForm.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_ItemForm.setProcessed('A');
        m_ItemForm.setProviderServiceMapID(1);
        when(itemFormRepo.getItemFormID(Mockito.<String>any())).thenReturn(m_ItemForm);

        M_Route m_Route = new M_Route();
        m_Route.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Route.setCreatedDate(mock(Date.class));
        m_Route.setDeleted(true);
        m_Route.setLastModDate(mock(Date.class));
        m_Route.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Route.setProcessed('A');
        m_Route.setProviderServiceMapID(1);
        m_Route.setRouteCode("Route Code");
        m_Route.setRouteDesc("Route Desc");
        m_Route.setRouteID(1);
        m_Route.setRouteName("Route Name");
        when(routeRepo.getItemRouteID(Mockito.<String>any())).thenReturn(m_Route);

        EAushadhiResponse data = new EAushadhiResponse();
        data.setBatchno("Batchno");
        data.setBrandid("Brandid");
        data.setCategory("Category");
        data.setCpacode("Cpacode");
        data.setDrugname("Drugname");
        data.setEdl("Edl");
        data.setExpdate("2020-03-01");
        data.setInhandqty("Inhandqty");
        data.setItemtypename("Itemtypename");
        data.setMfgdate("2020-03-01");
        data.setPono("Pono");
        data.setPrgid("Prgid");
        data.setSpecification("Specification");
        data.setStockstatus("Stockstatus");

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.insertItemsInAmrit(data, facilityRecords));
        verify(itemFormRepo).getItemFormID(eq("Itemtypename"));
        verify(routeRepo).getItemRouteID(eq("Oral"));
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#insertItemsInAmrit(EAushadhiResponse, M_Facility)}
     */
    @Test
    void testInsertItemsInAmrit2() throws Exception {
        // Arrange
        M_ItemCategory m_ItemCategory = new M_ItemCategory();
        m_ItemCategory.setAlertBeforeDays(1);
        m_ItemCategory.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_ItemCategory.setCreatedDate(mock(Date.class));
        m_ItemCategory.setDeleted(true);
        m_ItemCategory.setIssueType("Issue Type");
        m_ItemCategory.setItemCategoryCode("Item Category Code");
        m_ItemCategory.setItemCategoryDesc("Item Category Desc");
        m_ItemCategory.setItemCategoryID(1);
        m_ItemCategory.setItemCategoryName("Item Category Name");
        m_ItemCategory.setLastModDate(mock(Date.class));
        m_ItemCategory.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_ItemCategory.setProcessed('A');
        m_ItemCategory.setProviderServiceMapID(1);
        m_ItemCategory.setStatus("Status");
        when(itemCategoryRepo.getItemCategoryID(Mockito.<String>any(), Mockito.<Integer>any())).thenReturn(m_ItemCategory);

        M_ItemForm m_ItemForm = new M_ItemForm();
        m_ItemForm.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_ItemForm.setCreatedDate(mock(Date.class));
        m_ItemForm.setDeleted(true);
        m_ItemForm.setItemForm("Item Form");
        m_ItemForm.setItemFormCode("Item Form Code");
        m_ItemForm.setItemFormDesc("Item Form Desc");
        m_ItemForm.setItemFormID(1);
        m_ItemForm.setLastModDate(mock(Date.class));
        m_ItemForm.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_ItemForm.setProcessed('A');
        m_ItemForm.setProviderServiceMapID(1);
        when(itemFormRepo.getItemFormID(Mockito.<String>any())).thenReturn(m_ItemForm);

        ItemMaster itemMaster = new ItemMaster();
        itemMaster.setComposition("Composition");
        itemMaster.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        itemMaster.setCreatedDate(mock(Date.class));
        itemMaster.setDeleted(true);
        itemMaster.setDiscontinued(true);
        itemMaster.setIsEDL(true);
        itemMaster.setIsEaushadi(true);
        itemMaster.setIsMedical(true);
        itemMaster.setIsScheduledDrug(true);
        itemMaster.setItemCategoryID(1);
        itemMaster.setItemCode("Item Code");
        itemMaster.setItemDesc("Item Desc");
        itemMaster.setItemFormID(1);
        itemMaster.setItemID(1);
        itemMaster.setItemName("Item Name");
        itemMaster.setLastModDate(mock(Date.class));
        itemMaster.setManufacturerID(1);
        itemMaster.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        itemMaster.setPharmacologyCategoryID(1);
        itemMaster.setProcessed('A');
        itemMaster.setProviderServiceMapID(1);
        itemMaster.setRouteID(1);
        itemMaster.setSctCode("Sct Code");
        itemMaster.setSctTerm("Sct Term");
        itemMaster.setStatus("Status");
        itemMaster.setStrength("Strength");
        itemMaster.setUomID(1);
        when(itemRepo.save(Mockito.<ItemMaster>any())).thenReturn(itemMaster);

        M_Route m_Route = new M_Route();
        m_Route.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Route.setCreatedDate(mock(Date.class));
        m_Route.setDeleted(true);
        m_Route.setLastModDate(mock(Date.class));
        m_Route.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Route.setProcessed('A');
        m_Route.setProviderServiceMapID(1);
        m_Route.setRouteCode("Route Code");
        m_Route.setRouteDesc("Route Desc");
        m_Route.setRouteID(1);
        m_Route.setRouteName("Route Name");
        when(routeRepo.getItemRouteID(Mockito.<String>any())).thenReturn(m_Route);

        EAushadhiResponse data = new EAushadhiResponse();
        data.setBatchno("Batchno");
        data.setBrandid("Brandid");
        data.setCategory("42");
        data.setCpacode("Cpacode");
        data.setDrugname("Drugname");
        data.setEdl("Edl");
        data.setExpdate("2020-03-01");
        data.setInhandqty("Inhandqty");
        data.setItemtypename("Itemtypename");
        data.setMfgdate("2020-03-01");
        data.setPono("Pono");
        data.setPrgid("Prgid");
        data.setSpecification("Specification");
        data.setStockstatus("Stockstatus");

        M_Facility facilityRecords = new M_Facility();
        facilityRecords.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityRecords.setCreatedDate(mock(Date.class));
        facilityRecords.setDeleted(true);
        facilityRecords.setFacilityCode("Facility Code");
        facilityRecords.setFacilityDesc("Facility Desc");
        facilityRecords.setFacilityID(1);
        facilityRecords.setFacilityName("Facility Name");
        facilityRecords.setFacilityTypeID(1);
        facilityRecords.setIsMainFacility(true);
        facilityRecords.setLastModDate(mock(Date.class));
        facilityRecords.setLocation("Location");
        facilityRecords.setMainFacilityID(1);
        facilityRecords.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityRecords.setPageNo(1);
        facilityRecords.setPhysicalLocation("Physical Location");
        facilityRecords.setProcessed('A');
        facilityRecords.setProviderServiceMapID(1);
        facilityRecords.setStatus("Status");
        facilityRecords.setStoreType("Store Type");
        facilityRecords.seteAushadhiFacilityId(1);

        // Act
        ItemMaster actualInsertItemsInAmritResult = eAushadhiServiceImpl.insertItemsInAmrit(data, facilityRecords);

        // Assert
        verify(itemCategoryRepo).getItemCategoryID(isNull(), Mockito.<Integer>any());
        verify(itemFormRepo).getItemFormID(eq("Itemtypename"));
        verify(routeRepo).getItemRouteID(eq("Oral"));
        verify(itemRepo).save(Mockito.<ItemMaster>any());
        assertSame(itemMaster, actualInsertItemsInAmritResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#insertItemStockEntry(Integer, ItemMaster, EAushadhiResponse, Integer, Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testInsertItemStockEntry() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.text.ParseException: Unparseable date: "2020-03-01"
        //       at java.base/java.text.DateFormat.parse(DateFormat.java:399)
        //       at com.wipro.fhir.service.e_aushdhi.EAushadhiServiceImpl.insertItemStockEntry(EAushadhiServiceImpl.java:599)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        ItemMaster addedItemDetails = new ItemMaster();
        addedItemDetails.setComposition("Composition");
        addedItemDetails.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        addedItemDetails.setCreatedDate(mock(Date.class));
        addedItemDetails.setDeleted(true);
        addedItemDetails.setDiscontinued(true);
        addedItemDetails.setIsEDL(true);
        addedItemDetails.setIsEaushadi(true);
        addedItemDetails.setIsMedical(true);
        addedItemDetails.setIsScheduledDrug(true);
        addedItemDetails.setItemCategoryID(1);
        addedItemDetails.setItemCode("Item Code");
        addedItemDetails.setItemDesc("Item Desc");
        addedItemDetails.setItemFormID(1);
        addedItemDetails.setItemID(1);
        addedItemDetails.setItemName("Item Name");
        addedItemDetails.setLastModDate(mock(Date.class));
        addedItemDetails.setManufacturerID(1);
        addedItemDetails.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        addedItemDetails.setPharmacologyCategoryID(1);
        addedItemDetails.setProcessed('A');
        addedItemDetails.setProviderServiceMapID(1);
        addedItemDetails.setRouteID(1);
        addedItemDetails.setSctCode("Sct Code");
        addedItemDetails.setSctTerm("Sct Term");
        addedItemDetails.setStatus("Status");
        addedItemDetails.setStrength("Strength");
        addedItemDetails.setUomID(1);

        EAushadhiResponse eAushadhiResp = new EAushadhiResponse();
        eAushadhiResp.setBatchno("Batchno");
        eAushadhiResp.setBrandid("Brandid");
        eAushadhiResp.setCategory("Category");
        eAushadhiResp.setCpacode("Cpacode");
        eAushadhiResp.setDrugname("Drugname");
        eAushadhiResp.setEdl("Edl");
        eAushadhiResp.setExpdate("2020-03-01");
        eAushadhiResp.setInhandqty("Inhandqty");
        eAushadhiResp.setItemtypename("Itemtypename");
        eAushadhiResp.setMfgdate("2020-03-01");
        eAushadhiResp.setPono("Pono");
        eAushadhiResp.setPrgid("Prgid");
        eAushadhiResp.setSpecification("Specification");
        eAushadhiResp.setStockstatus("Stockstatus");

        // Act
        eAushadhiServiceImpl.insertItemStockEntry(1, addedItemDetails, eAushadhiResp, 1, 1);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#insertItemFacilityMapping(Integer, ItemMaster)}
     */
    @Test
    void testInsertItemFacilityMapping() throws Exception {
        // Arrange
        M_itemfacilitymapping m_itemfacilitymapping = new M_itemfacilitymapping();
        m_itemfacilitymapping.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_itemfacilitymapping.setCreatedDate(mock(Date.class));
        m_itemfacilitymapping.setDeleted(true);
        m_itemfacilitymapping.setFacilityID(1);
        m_itemfacilitymapping.setItemFacilityMapID(1);
        m_itemfacilitymapping.setItemID(1);
        m_itemfacilitymapping.setLastModDate(mock(Date.class));
        m_itemfacilitymapping.setMappingType("Mapping Type");
        m_itemfacilitymapping.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_itemfacilitymapping.setProviderServiceMapID(1);
        m_itemfacilitymapping.setStatus("Status");
        when(m_itemfacilitymappingRepo.save(Mockito.<M_itemfacilitymapping>any())).thenReturn(m_itemfacilitymapping);

        ItemMaster addedItemDetails = new ItemMaster();
        addedItemDetails.setComposition("Composition");
        addedItemDetails.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        addedItemDetails.setCreatedDate(mock(Date.class));
        addedItemDetails.setDeleted(true);
        addedItemDetails.setDiscontinued(true);
        addedItemDetails.setIsEDL(true);
        addedItemDetails.setIsEaushadi(true);
        addedItemDetails.setIsMedical(true);
        addedItemDetails.setIsScheduledDrug(true);
        addedItemDetails.setItemCategoryID(1);
        addedItemDetails.setItemCode("Item Code");
        addedItemDetails.setItemDesc("Item Desc");
        addedItemDetails.setItemFormID(1);
        addedItemDetails.setItemID(1);
        addedItemDetails.setItemName("Item Name");
        addedItemDetails.setLastModDate(mock(Date.class));
        addedItemDetails.setManufacturerID(1);
        addedItemDetails.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        addedItemDetails.setPharmacologyCategoryID(1);
        addedItemDetails.setProcessed('A');
        addedItemDetails.setProviderServiceMapID(1);
        addedItemDetails.setRouteID(1);
        addedItemDetails.setSctCode("Sct Code");
        addedItemDetails.setSctTerm("Sct Term");
        addedItemDetails.setStatus("Status");
        addedItemDetails.setStrength("Strength");
        addedItemDetails.setUomID(1);

        // Act
        int actualInsertItemFacilityMappingResult = eAushadhiServiceImpl.insertItemFacilityMapping(1, addedItemDetails);

        // Assert
        verify(m_itemfacilitymappingRepo).save(Mockito.<M_itemfacilitymapping>any());
        assertEquals(1, actualInsertItemFacilityMappingResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#mapItemDetailsAndInsertStockToSubFacility(EAushadhiResponse, M_Facility, Integer, Integer, ItemMaster)}
     */
    @Test
    void testMapItemDetailsAndInsertStockToSubFacility() throws Exception {
        // Arrange
        M_itemfacilitymapping m_itemfacilitymapping = new M_itemfacilitymapping();
        m_itemfacilitymapping.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_itemfacilitymapping.setCreatedDate(mock(Date.class));
        m_itemfacilitymapping.setDeleted(true);
        m_itemfacilitymapping.setFacilityID(1);
        m_itemfacilitymapping.setItemFacilityMapID(1);
        m_itemfacilitymapping.setItemID(1);
        m_itemfacilitymapping.setLastModDate(mock(Date.class));
        m_itemfacilitymapping.setMappingType("Mapping Type");
        m_itemfacilitymapping.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_itemfacilitymapping.setProviderServiceMapID(1);
        m_itemfacilitymapping.setStatus("Status");
        when(m_itemfacilitymappingRepo.save(Mockito.<M_itemfacilitymapping>any())).thenReturn(m_itemfacilitymapping);

        EAushadhiResponse data = new EAushadhiResponse();
        data.setBatchno("Batchno");
        data.setBrandid("Brandid");
        data.setCategory("Category");
        data.setCpacode("Cpacode");
        data.setDrugname("Drugname");
        data.setEdl("Edl");
        data.setExpdate("2020-03-01");
        data.setInhandqty("Inhandqty");
        data.setItemtypename("Itemtypename");
        data.setMfgdate("2020-03-01");
        data.setPono("Pono");
        data.setPrgid("Prgid");
        data.setSpecification("Specification");
        data.setStockstatus("Stockstatus");

        M_Facility amritFacilityID = new M_Facility();
        amritFacilityID.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        amritFacilityID.setCreatedDate(mock(Date.class));
        amritFacilityID.setDeleted(true);
        amritFacilityID.setFacilityCode("Facility Code");
        amritFacilityID.setFacilityDesc("Facility Desc");
        amritFacilityID.setFacilityID(1);
        amritFacilityID.setFacilityName("Facility Name");
        amritFacilityID.setFacilityTypeID(1);
        amritFacilityID.setIsMainFacility(true);
        amritFacilityID.setLastModDate(mock(Date.class));
        amritFacilityID.setLocation("Location");
        amritFacilityID.setMainFacilityID(1);
        amritFacilityID.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        amritFacilityID.setPageNo(1);
        amritFacilityID.setPhysicalLocation("Physical Location");
        amritFacilityID.setProcessed('A');
        amritFacilityID.setProviderServiceMapID(1);
        amritFacilityID.setStatus("Status");
        amritFacilityID.setStoreType("Store Type");
        amritFacilityID.seteAushadhiFacilityId(1);

        ItemMaster addedItemDetails = new ItemMaster();
        addedItemDetails.setComposition("Composition");
        addedItemDetails.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        addedItemDetails.setCreatedDate(mock(Date.class));
        addedItemDetails.setDeleted(true);
        addedItemDetails.setDiscontinued(true);
        addedItemDetails.setIsEDL(true);
        addedItemDetails.setIsEaushadi(true);
        addedItemDetails.setIsMedical(true);
        addedItemDetails.setIsScheduledDrug(true);
        addedItemDetails.setItemCategoryID(1);
        addedItemDetails.setItemCode("Item Code");
        addedItemDetails.setItemDesc("Item Desc");
        addedItemDetails.setItemFormID(1);
        addedItemDetails.setItemID(1);
        addedItemDetails.setItemName("Item Name");
        addedItemDetails.setLastModDate(mock(Date.class));
        addedItemDetails.setManufacturerID(1);
        addedItemDetails.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        addedItemDetails.setPharmacologyCategoryID(1);
        addedItemDetails.setProcessed('A');
        addedItemDetails.setProviderServiceMapID(1);
        addedItemDetails.setRouteID(1);
        addedItemDetails.setSctCode("Sct Code");
        addedItemDetails.setSctTerm("Sct Term");
        addedItemDetails.setStatus("Status");
        addedItemDetails.setStrength("Strength");
        addedItemDetails.setUomID(1);

        // Act
        eAushadhiServiceImpl.mapItemDetailsAndInsertStockToSubFacility(data, amritFacilityID, 1, 1, addedItemDetails);

        // Assert
        verify(m_itemfacilitymappingRepo).save(Mockito.<M_itemfacilitymapping>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#mapItemDetailsAndInsertStockToSubFacility(EAushadhiResponse, M_Facility, Integer, Integer, ItemMaster)}
     */
    @Test
    void testMapItemDetailsAndInsertStockToSubFacility2() throws Exception {
        // Arrange
        M_itemfacilitymapping m_itemfacilitymapping = new M_itemfacilitymapping();
        m_itemfacilitymapping.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_itemfacilitymapping.setCreatedDate(mock(Date.class));
        m_itemfacilitymapping.setDeleted(true);
        m_itemfacilitymapping.setFacilityID(1);
        m_itemfacilitymapping.setItemFacilityMapID(1);
        m_itemfacilitymapping.setItemID(1);
        m_itemfacilitymapping.setLastModDate(mock(Date.class));
        m_itemfacilitymapping.setMappingType("Mapping Type");
        m_itemfacilitymapping.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_itemfacilitymapping.setProviderServiceMapID(1);
        m_itemfacilitymapping.setStatus("Status");
        when(m_itemfacilitymappingRepo.save(Mockito.<M_itemfacilitymapping>any())).thenReturn(m_itemfacilitymapping);

        EAushadhiResponse data = new EAushadhiResponse();
        data.setBatchno("Batchno");
        data.setBrandid("Brandid");
        data.setCategory("Category");
        data.setCpacode("Cpacode");
        data.setDrugname("Drugname");
        data.setEdl("Edl");
        data.setExpdate("2020-03-01");
        data.setInhandqty("Inhandqty");
        data.setItemtypename("Itemtypename");
        data.setMfgdate("2020-03-01");
        data.setPono("Pono");
        data.setPrgid("Prgid");
        data.setSpecification("Specification");
        data.setStockstatus("Stockstatus");
        M_Facility amritFacilityID = mock(M_Facility.class);
        when(amritFacilityID.getFacilityID()).thenReturn(1);
        doNothing().when(amritFacilityID).setCreatedBy(Mockito.<String>any());
        doNothing().when(amritFacilityID).setCreatedDate(Mockito.<Date>any());
        doNothing().when(amritFacilityID).setDeleted(Mockito.<Boolean>any());
        doNothing().when(amritFacilityID).setFacilityCode(Mockito.<String>any());
        doNothing().when(amritFacilityID).setFacilityDesc(Mockito.<String>any());
        doNothing().when(amritFacilityID).setFacilityID(Mockito.<Integer>any());
        doNothing().when(amritFacilityID).setFacilityName(Mockito.<String>any());
        doNothing().when(amritFacilityID).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(amritFacilityID).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(amritFacilityID).setLastModDate(Mockito.<Date>any());
        doNothing().when(amritFacilityID).setLocation(Mockito.<String>any());
        doNothing().when(amritFacilityID).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(amritFacilityID).setModifiedBy(Mockito.<String>any());
        doNothing().when(amritFacilityID).setPageNo(Mockito.<Integer>any());
        doNothing().when(amritFacilityID).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(amritFacilityID).setProcessed(Mockito.<Character>any());
        doNothing().when(amritFacilityID).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(amritFacilityID).setStatus(Mockito.<String>any());
        doNothing().when(amritFacilityID).setStoreType(Mockito.<String>any());
        doNothing().when(amritFacilityID).seteAushadhiFacilityId(Mockito.<Integer>any());
        amritFacilityID.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        amritFacilityID.setCreatedDate(mock(Date.class));
        amritFacilityID.setDeleted(true);
        amritFacilityID.setFacilityCode("Facility Code");
        amritFacilityID.setFacilityDesc("Facility Desc");
        amritFacilityID.setFacilityID(1);
        amritFacilityID.setFacilityName("Facility Name");
        amritFacilityID.setFacilityTypeID(1);
        amritFacilityID.setIsMainFacility(true);
        amritFacilityID.setLastModDate(mock(Date.class));
        amritFacilityID.setLocation("Location");
        amritFacilityID.setMainFacilityID(1);
        amritFacilityID.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        amritFacilityID.setPageNo(1);
        amritFacilityID.setPhysicalLocation("Physical Location");
        amritFacilityID.setProcessed('A');
        amritFacilityID.setProviderServiceMapID(1);
        amritFacilityID.setStatus("Status");
        amritFacilityID.setStoreType("Store Type");
        amritFacilityID.seteAushadhiFacilityId(1);

        ItemMaster addedItemDetails = new ItemMaster();
        addedItemDetails.setComposition("Composition");
        addedItemDetails.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        addedItemDetails.setCreatedDate(mock(Date.class));
        addedItemDetails.setDeleted(true);
        addedItemDetails.setDiscontinued(true);
        addedItemDetails.setIsEDL(true);
        addedItemDetails.setIsEaushadi(true);
        addedItemDetails.setIsMedical(true);
        addedItemDetails.setIsScheduledDrug(true);
        addedItemDetails.setItemCategoryID(1);
        addedItemDetails.setItemCode("Item Code");
        addedItemDetails.setItemDesc("Item Desc");
        addedItemDetails.setItemFormID(1);
        addedItemDetails.setItemID(1);
        addedItemDetails.setItemName("Item Name");
        addedItemDetails.setLastModDate(mock(Date.class));
        addedItemDetails.setManufacturerID(1);
        addedItemDetails.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        addedItemDetails.setPharmacologyCategoryID(1);
        addedItemDetails.setProcessed('A');
        addedItemDetails.setProviderServiceMapID(1);
        addedItemDetails.setRouteID(1);
        addedItemDetails.setSctCode("Sct Code");
        addedItemDetails.setSctTerm("Sct Term");
        addedItemDetails.setStatus("Status");
        addedItemDetails.setStrength("Strength");
        addedItemDetails.setUomID(1);

        // Act
        eAushadhiServiceImpl.mapItemDetailsAndInsertStockToSubFacility(data, amritFacilityID, 1, 1, addedItemDetails);

        // Assert
        verify(amritFacilityID, atLeast(1)).getFacilityID();
        verify(amritFacilityID).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(amritFacilityID).setCreatedDate(Mockito.<Date>any());
        verify(amritFacilityID).setDeleted(Mockito.<Boolean>any());
        verify(amritFacilityID).setFacilityCode(eq("Facility Code"));
        verify(amritFacilityID).setFacilityDesc(eq("Facility Desc"));
        verify(amritFacilityID).setFacilityID(Mockito.<Integer>any());
        verify(amritFacilityID).setFacilityName(eq("Facility Name"));
        verify(amritFacilityID).setFacilityTypeID(Mockito.<Integer>any());
        verify(amritFacilityID).setIsMainFacility(Mockito.<Boolean>any());
        verify(amritFacilityID).setLastModDate(Mockito.<Date>any());
        verify(amritFacilityID).setLocation(eq("Location"));
        verify(amritFacilityID).setMainFacilityID(Mockito.<Integer>any());
        verify(amritFacilityID).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(amritFacilityID).setPageNo(Mockito.<Integer>any());
        verify(amritFacilityID).setPhysicalLocation(eq("Physical Location"));
        verify(amritFacilityID).setProcessed(Mockito.<Character>any());
        verify(amritFacilityID).setProviderServiceMapID(Mockito.<Integer>any());
        verify(amritFacilityID).setStatus(eq("Status"));
        verify(amritFacilityID).setStoreType(eq("Store Type"));
        verify(amritFacilityID).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(m_itemfacilitymappingRepo).save(Mockito.<M_itemfacilitymapping>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#syncDispenseDetailsToEAushadhi(String, String)}
     */
    @Test
    void testSyncDispenseDetailsToEAushadhi() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.syncDispenseDetailsToEAushadhi("Request", "JaneDoe"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.syncDispenseDetailsToEAushadhi("0", "JaneDoe"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.syncDispenseDetailsToEAushadhi("42", "JaneDoe"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.syncDispenseDetailsToEAushadhi("", "JaneDoe"));
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl.syncDispenseDetailsToEAushadhi("EDL", "JaneDoe"));
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID() throws FHIRException {
        // Arrange
        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act and Assert
        assertTrue(
                eAushadhiServiceImpl.getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe")
                        .isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID2() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("Ben Search By Ben ID");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID3() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("foo");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID4() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("42");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID5() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act
        ArrayList<Map<String, Object>> actualPatientIssueDetailsForEAushadhByFacilityID = eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe");

        // Assert
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertTrue(actualPatientIssueDetailsForEAushadhByFacilityID.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID6() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("E-aushadhi drugs are not dispensed");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID7() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn(null);
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act
        ArrayList<Map<String, Object>> actualPatientIssueDetailsForEAushadhByFacilityID = eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe");

        // Assert
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertTrue(actualPatientIssueDetailsForEAushadhByFacilityID.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID8() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");

        ArrayList<Objects[]> objectsArrayList = new ArrayList<>();
        objectsArrayList.add(new Objects[]{null});
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(objectsArrayList);

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class, () -> eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe"));
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhByFacilityID(List, M_Facility, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhByFacilityID9() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        T_PatientIssue t_PatientIssue2 = new T_PatientIssue();
        t_PatientIssue2.setAge(0);
        t_PatientIssue2.setBenRegID(2L);
        t_PatientIssue2.setBeneficiaryID(2L);
        t_PatientIssue2.setCreatedBy("gnum_gender_code");
        t_PatientIssue2.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue2.setDeleted(false);
        t_PatientIssue2.setDoctorName("hststr_prescribed_by");
        t_PatientIssue2.setFacilityID(2);
        t_PatientIssue2.setGender("hststr_prescribed_by");
        t_PatientIssue2.setIssueType("hststr_prescribed_by");
        t_PatientIssue2.setIssuedBy("hststr_prescribed_by");
        t_PatientIssue2.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue2.setModifiedBy("gnum_gender_code");
        t_PatientIssue2.setParkingPlaceID(2L);
        t_PatientIssue2.setPatientIssueID(2L);
        t_PatientIssue2.setPatientName("hststr_prescribed_by");
        t_PatientIssue2.setPrescriptionID(2);
        t_PatientIssue2.setProcessed('A');
        t_PatientIssue2.setProviderServiceMapID(2);
        t_PatientIssue2.setReference("hststr_prescribed_by");
        t_PatientIssue2.setSyncFacilityID(2);
        t_PatientIssue2.setVanID(2L);
        t_PatientIssue2.setVanSerialNo(0L);
        t_PatientIssue2.setVisitCode(0L);
        t_PatientIssue2.setVisitID(2);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue2);
        patientIssueDetails.add(t_PatientIssue);

        M_Facility facilityDet = new M_Facility();
        facilityDet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        facilityDet.setCreatedDate(mock(java.sql.Date.class));
        facilityDet.setDeleted(true);
        facilityDet.setFacilityCode("Facility Code");
        facilityDet.setFacilityDesc("Facility Desc");
        facilityDet.setFacilityID(1);
        facilityDet.setFacilityName("Facility Name");
        facilityDet.setFacilityTypeID(1);
        facilityDet.setIsMainFacility(true);
        facilityDet.setLastModDate(mock(java.sql.Date.class));
        facilityDet.setLocation("Location");
        facilityDet.setMainFacilityID(1);
        facilityDet.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        facilityDet.setPageNo(1);
        facilityDet.setPhysicalLocation("Physical Location");
        facilityDet.setProcessed('A');
        facilityDet.setProviderServiceMapID(1);
        facilityDet.setStatus("Status");
        facilityDet.setStoreType("Store Type");
        facilityDet.seteAushadhiFacilityId(1);

        // Act
        ArrayList<Map<String, Object>> actualPatientIssueDetailsForEAushadhByFacilityID = eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhByFacilityID(patientIssueDetails, facilityDet, "JaneDoe");

        // Assert
        verify(patientIssueRepo, atLeast(1)).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel, atLeast(1)).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertTrue(actualPatientIssueDetailsForEAushadhByFacilityID.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID() throws FHIRException {
        // Arrange, Act and Assert
        assertTrue(eAushadhiServiceImpl.getPatientIssueDetailsForEAushadhiWithoutFacilityID(new ArrayList<>(), "JaneDoe")
                .isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID2() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("Ben Search By Ben ID");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> eAushadhiServiceImpl.getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID3() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("foo");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> eAushadhiServiceImpl.getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID4() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("42");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> eAushadhiServiceImpl.getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID5() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");

        M_Facility m_Facility = new M_Facility();
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(java.sql.Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("Facility Code");
        m_Facility.setFacilityDesc("Facility Desc");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("Facility Name");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(java.sql.Date.class));
        m_Facility.setLocation("Location");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("Physical Location");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("Status");
        m_Facility.setStoreType("Store Type");
        m_Facility.seteAushadhiFacilityId(1);
        when(facilityRepo.geteAushadhiFacilityID(Mockito.<Integer>any())).thenReturn(m_Facility);
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act
        ArrayList<Map<String, Object>> actualPatientIssueDetailsForEAushadhiWithoutFacilityID = eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe");

        // Assert
        verify(facilityRepo).geteAushadhiFacilityID(Mockito.<Integer>any());
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertTrue(actualPatientIssueDetailsForEAushadhiWithoutFacilityID.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID6() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("E-aushadhi drugs are not dispensed");

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> eAushadhiServiceImpl.getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe"));
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID7() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn(null);

        M_Facility m_Facility = new M_Facility();
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(java.sql.Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("Facility Code");
        m_Facility.setFacilityDesc("Facility Desc");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("Facility Name");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(java.sql.Date.class));
        m_Facility.setLocation("Location");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("Physical Location");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("Status");
        m_Facility.setStoreType("Store Type");
        m_Facility.seteAushadhiFacilityId(1);
        when(facilityRepo.geteAushadhiFacilityID(Mockito.<Integer>any())).thenReturn(m_Facility);
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act
        ArrayList<Map<String, Object>> actualPatientIssueDetailsForEAushadhiWithoutFacilityID = eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe");

        // Assert
        verify(facilityRepo).geteAushadhiFacilityID(Mockito.<Integer>any());
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertTrue(actualPatientIssueDetailsForEAushadhiWithoutFacilityID.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID8() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");
        M_Facility m_Facility = mock(M_Facility.class);
        when(m_Facility.getFacilityID()).thenReturn(1);
        when(m_Facility.geteAushadhiFacilityId()).thenReturn(1);
        doNothing().when(m_Facility).setCreatedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setCreatedDate(Mockito.<java.sql.Date>any());
        doNothing().when(m_Facility).setDeleted(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setFacilityCode(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityDesc(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setFacilityName(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setLastModDate(Mockito.<java.sql.Date>any());
        doNothing().when(m_Facility).setLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setModifiedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setPageNo(Mockito.<Integer>any());
        doNothing().when(m_Facility).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setProcessed(Mockito.<Character>any());
        doNothing().when(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setStatus(Mockito.<String>any());
        doNothing().when(m_Facility).setStoreType(Mockito.<String>any());
        doNothing().when(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(java.sql.Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("Facility Code");
        m_Facility.setFacilityDesc("Facility Desc");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("Facility Name");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(java.sql.Date.class));
        m_Facility.setLocation("Location");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("Physical Location");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("Status");
        m_Facility.setStoreType("Store Type");
        m_Facility.seteAushadhiFacilityId(1);
        when(facilityRepo.geteAushadhiFacilityID(Mockito.<Integer>any())).thenReturn(m_Facility);
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act
        ArrayList<Map<String, Object>> actualPatientIssueDetailsForEAushadhiWithoutFacilityID = eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe");

        // Assert
        verify(m_Facility).getFacilityID();
        verify(m_Facility).geteAushadhiFacilityId();
        verify(m_Facility).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(m_Facility).setCreatedDate(Mockito.<java.sql.Date>any());
        verify(m_Facility).setDeleted(Mockito.<Boolean>any());
        verify(m_Facility).setFacilityCode(eq("Facility Code"));
        verify(m_Facility).setFacilityDesc(eq("Facility Desc"));
        verify(m_Facility).setFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setFacilityName(eq("Facility Name"));
        verify(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        verify(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        verify(m_Facility).setLastModDate(Mockito.<java.sql.Date>any());
        verify(m_Facility).setLocation(eq("Location"));
        verify(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(m_Facility).setPageNo(Mockito.<Integer>any());
        verify(m_Facility).setPhysicalLocation(eq("Physical Location"));
        verify(m_Facility).setProcessed(Mockito.<Character>any());
        verify(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        verify(m_Facility).setStatus(eq("Status"));
        verify(m_Facility).setStoreType(eq("Store Type"));
        verify(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(facilityRepo).geteAushadhiFacilityID(Mockito.<Integer>any());
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertTrue(actualPatientIssueDetailsForEAushadhiWithoutFacilityID.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID9() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");
        M_Facility m_Facility = mock(M_Facility.class);
        when(m_Facility.getFacilityID()).thenReturn(1);
        when(m_Facility.geteAushadhiFacilityId()).thenReturn(1);
        doNothing().when(m_Facility).setCreatedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setCreatedDate(Mockito.<java.sql.Date>any());
        doNothing().when(m_Facility).setDeleted(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setFacilityCode(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityDesc(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setFacilityName(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setLastModDate(Mockito.<java.sql.Date>any());
        doNothing().when(m_Facility).setLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setModifiedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setPageNo(Mockito.<Integer>any());
        doNothing().when(m_Facility).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setProcessed(Mockito.<Character>any());
        doNothing().when(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setStatus(Mockito.<String>any());
        doNothing().when(m_Facility).setStoreType(Mockito.<String>any());
        doNothing().when(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(java.sql.Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("Facility Code");
        m_Facility.setFacilityDesc("Facility Desc");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("Facility Name");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(java.sql.Date.class));
        m_Facility.setLocation("Location");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("Physical Location");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("Status");
        m_Facility.setStoreType("Store Type");
        m_Facility.seteAushadhiFacilityId(1);
        when(facilityRepo.geteAushadhiFacilityID(Mockito.<Integer>any())).thenReturn(m_Facility);

        ArrayList<Objects[]> objectsArrayList = new ArrayList<>();
        objectsArrayList.add(new Objects[]{null});
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(objectsArrayList);

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue);

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> eAushadhiServiceImpl.getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe"));
        verify(m_Facility).getFacilityID();
        verify(m_Facility).geteAushadhiFacilityId();
        verify(m_Facility).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(m_Facility).setCreatedDate(Mockito.<java.sql.Date>any());
        verify(m_Facility).setDeleted(Mockito.<Boolean>any());
        verify(m_Facility).setFacilityCode(eq("Facility Code"));
        verify(m_Facility).setFacilityDesc(eq("Facility Desc"));
        verify(m_Facility).setFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setFacilityName(eq("Facility Name"));
        verify(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        verify(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        verify(m_Facility).setLastModDate(Mockito.<java.sql.Date>any());
        verify(m_Facility).setLocation(eq("Location"));
        verify(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(m_Facility).setPageNo(Mockito.<Integer>any());
        verify(m_Facility).setPhysicalLocation(eq("Physical Location"));
        verify(m_Facility).setProcessed(Mockito.<Character>any());
        verify(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        verify(m_Facility).setStatus(eq("Status"));
        verify(m_Facility).setStoreType(eq("Store Type"));
        verify(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(facilityRepo).geteAushadhiFacilityID(Mockito.<Integer>any());
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getPatientIssueDetailsForEAushadhiWithoutFacilityID(List, String)}
     */
    @Test
    void testGetPatientIssueDetailsForEAushadhiWithoutFacilityID10() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");
        M_Facility m_Facility = mock(M_Facility.class);
        when(m_Facility.getFacilityID()).thenReturn(1);
        when(m_Facility.geteAushadhiFacilityId()).thenReturn(1);
        doNothing().when(m_Facility).setCreatedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setCreatedDate(Mockito.<java.sql.Date>any());
        doNothing().when(m_Facility).setDeleted(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setFacilityCode(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityDesc(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setFacilityName(Mockito.<String>any());
        doNothing().when(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(m_Facility).setLastModDate(Mockito.<java.sql.Date>any());
        doNothing().when(m_Facility).setLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setModifiedBy(Mockito.<String>any());
        doNothing().when(m_Facility).setPageNo(Mockito.<Integer>any());
        doNothing().when(m_Facility).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(m_Facility).setProcessed(Mockito.<Character>any());
        doNothing().when(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(m_Facility).setStatus(Mockito.<String>any());
        doNothing().when(m_Facility).setStoreType(Mockito.<String>any());
        doNothing().when(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        m_Facility.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        m_Facility.setCreatedDate(mock(java.sql.Date.class));
        m_Facility.setDeleted(true);
        m_Facility.setFacilityCode("Facility Code");
        m_Facility.setFacilityDesc("Facility Desc");
        m_Facility.setFacilityID(1);
        m_Facility.setFacilityName("Facility Name");
        m_Facility.setFacilityTypeID(1);
        m_Facility.setIsMainFacility(true);
        m_Facility.setLastModDate(mock(java.sql.Date.class));
        m_Facility.setLocation("Location");
        m_Facility.setMainFacilityID(1);
        m_Facility.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Facility.setPageNo(1);
        m_Facility.setPhysicalLocation("Physical Location");
        m_Facility.setProcessed('A');
        m_Facility.setProviderServiceMapID(1);
        m_Facility.setStatus("Status");
        m_Facility.setStoreType("Store Type");
        m_Facility.seteAushadhiFacilityId(1);
        when(facilityRepo.geteAushadhiFacilityID(Mockito.<Integer>any())).thenReturn(m_Facility);
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue t_PatientIssue = new T_PatientIssue();
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        t_PatientIssue.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        t_PatientIssue.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);

        T_PatientIssue t_PatientIssue2 = new T_PatientIssue();
        t_PatientIssue2.setAge(0);
        t_PatientIssue2.setBenRegID(2L);
        t_PatientIssue2.setBeneficiaryID(2L);
        t_PatientIssue2.setCreatedBy("gnum_gender_code");
        t_PatientIssue2.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue2.setDeleted(false);
        t_PatientIssue2.setDoctorName("hststr_prescribed_by");
        t_PatientIssue2.setFacilityID(2);
        t_PatientIssue2.setGender("hststr_prescribed_by");
        t_PatientIssue2.setIssueType("hststr_prescribed_by");
        t_PatientIssue2.setIssuedBy("hststr_prescribed_by");
        t_PatientIssue2.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        t_PatientIssue2.setModifiedBy("gnum_gender_code");
        t_PatientIssue2.setParkingPlaceID(2L);
        t_PatientIssue2.setPatientIssueID(2L);
        t_PatientIssue2.setPatientName("hststr_prescribed_by");
        t_PatientIssue2.setPrescriptionID(2);
        t_PatientIssue2.setProcessed('A');
        t_PatientIssue2.setProviderServiceMapID(2);
        t_PatientIssue2.setReference("hststr_prescribed_by");
        t_PatientIssue2.setSyncFacilityID(2);
        t_PatientIssue2.setVanID(2L);
        t_PatientIssue2.setVanSerialNo(0L);
        t_PatientIssue2.setVisitCode(0L);
        t_PatientIssue2.setVisitID(2);

        ArrayList<T_PatientIssue> patientIssueDetails = new ArrayList<>();
        patientIssueDetails.add(t_PatientIssue2);
        patientIssueDetails.add(t_PatientIssue);

        // Act
        ArrayList<Map<String, Object>> actualPatientIssueDetailsForEAushadhiWithoutFacilityID = eAushadhiServiceImpl
                .getPatientIssueDetailsForEAushadhiWithoutFacilityID(patientIssueDetails, "JaneDoe");

        // Assert
        verify(m_Facility, atLeast(1)).getFacilityID();
        verify(m_Facility, atLeast(1)).geteAushadhiFacilityId();
        verify(m_Facility).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(m_Facility).setCreatedDate(Mockito.<java.sql.Date>any());
        verify(m_Facility).setDeleted(Mockito.<Boolean>any());
        verify(m_Facility).setFacilityCode(eq("Facility Code"));
        verify(m_Facility).setFacilityDesc(eq("Facility Desc"));
        verify(m_Facility).setFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setFacilityName(eq("Facility Name"));
        verify(m_Facility).setFacilityTypeID(Mockito.<Integer>any());
        verify(m_Facility).setIsMainFacility(Mockito.<Boolean>any());
        verify(m_Facility).setLastModDate(Mockito.<java.sql.Date>any());
        verify(m_Facility).setLocation(eq("Location"));
        verify(m_Facility).setMainFacilityID(Mockito.<Integer>any());
        verify(m_Facility).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(m_Facility).setPageNo(Mockito.<Integer>any());
        verify(m_Facility).setPhysicalLocation(eq("Physical Location"));
        verify(m_Facility).setProcessed(Mockito.<Character>any());
        verify(m_Facility).setProviderServiceMapID(Mockito.<Integer>any());
        verify(m_Facility).setStatus(eq("Status"));
        verify(m_Facility).setStoreType(eq("Store Type"));
        verify(m_Facility).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(facilityRepo, atLeast(1)).geteAushadhiFacilityID(Mockito.<Integer>any());
        verify(patientIssueRepo, atLeast(1)).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        verify(aPIChannel, atLeast(1)).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertTrue(actualPatientIssueDetailsForEAushadhiWithoutFacilityID.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getDrugDispenseDetails(T_PatientIssue, M_Facility, Integer)}
     */
    @Test
    void testGetDrugDispenseDetails() throws Exception {
        // Arrange
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue patientIssueDetails = new T_PatientIssue();
        patientIssueDetails.setAge(1);
        patientIssueDetails.setBenRegID(1L);
        patientIssueDetails.setBeneficiaryID(1L);
        patientIssueDetails.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        patientIssueDetails.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientIssueDetails.setDeleted(true);
        patientIssueDetails.setDoctorName("Doctor Name");
        patientIssueDetails.setFacilityID(1);
        patientIssueDetails.setGender("Gender");
        patientIssueDetails.setIssueType("Issue Type");
        patientIssueDetails.setIssuedBy("Issued By");
        patientIssueDetails.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientIssueDetails.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        patientIssueDetails.setParkingPlaceID(1L);
        patientIssueDetails.setPatientIssueID(1L);
        patientIssueDetails.setPatientName("Patient Name");
        patientIssueDetails.setPrescriptionID(1);
        patientIssueDetails.setProcessed('A');
        patientIssueDetails.setProviderServiceMapID(1);
        patientIssueDetails.setReference("Reference");
        patientIssueDetails.setSyncFacilityID(1);
        patientIssueDetails.setVanID(1L);
        patientIssueDetails.setVanSerialNo(1L);
        patientIssueDetails.setVisitCode(1L);
        patientIssueDetails.setVisitID(1);

        M_Facility requestObj = new M_Facility();
        requestObj.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        requestObj.setCreatedDate(mock(java.sql.Date.class));
        requestObj.setDeleted(true);
        requestObj.setFacilityCode("Facility Code");
        requestObj.setFacilityDesc("Facility Desc");
        requestObj.setFacilityID(1);
        requestObj.setFacilityName("Facility Name");
        requestObj.setFacilityTypeID(1);
        requestObj.setIsMainFacility(true);
        requestObj.setLastModDate(mock(java.sql.Date.class));
        requestObj.setLocation("Location");
        requestObj.setMainFacilityID(1);
        requestObj.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        requestObj.setPageNo(1);
        requestObj.setPhysicalLocation("Physical Location");
        requestObj.setProcessed('A');
        requestObj.setProviderServiceMapID(1);
        requestObj.setStatus("Status");
        requestObj.setStoreType("Store Type");
        requestObj.seteAushadhiFacilityId(1);

        // Act
        ArrayList<SyncDispenseDetailsRequest> actualDrugDispenseDetails = eAushadhiServiceImpl
                .getDrugDispenseDetails(patientIssueDetails, requestObj, 1);

        // Assert
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        assertTrue(actualDrugDispenseDetails.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getDrugDispenseDetails(T_PatientIssue, M_Facility, Integer)}
     */
    @Test
    void testGetDrugDispenseDetails2() throws Exception {
        // Arrange
        ArrayList<Objects[]> objectsArrayList = new ArrayList<>();
        objectsArrayList.add(new Objects[]{null});
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(objectsArrayList);

        T_PatientIssue patientIssueDetails = new T_PatientIssue();
        patientIssueDetails.setAge(1);
        patientIssueDetails.setBenRegID(1L);
        patientIssueDetails.setBeneficiaryID(1L);
        patientIssueDetails.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        patientIssueDetails.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientIssueDetails.setDeleted(true);
        patientIssueDetails.setDoctorName("Doctor Name");
        patientIssueDetails.setFacilityID(1);
        patientIssueDetails.setGender("Gender");
        patientIssueDetails.setIssueType("Issue Type");
        patientIssueDetails.setIssuedBy("Issued By");
        patientIssueDetails.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientIssueDetails.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        patientIssueDetails.setParkingPlaceID(1L);
        patientIssueDetails.setPatientIssueID(1L);
        patientIssueDetails.setPatientName("Patient Name");
        patientIssueDetails.setPrescriptionID(1);
        patientIssueDetails.setProcessed('A');
        patientIssueDetails.setProviderServiceMapID(1);
        patientIssueDetails.setReference("Reference");
        patientIssueDetails.setSyncFacilityID(1);
        patientIssueDetails.setVanID(1L);
        patientIssueDetails.setVanSerialNo(1L);
        patientIssueDetails.setVisitCode(1L);
        patientIssueDetails.setVisitID(1);

        M_Facility requestObj = new M_Facility();
        requestObj.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        requestObj.setCreatedDate(mock(java.sql.Date.class));
        requestObj.setDeleted(true);
        requestObj.setFacilityCode("Facility Code");
        requestObj.setFacilityDesc("Facility Desc");
        requestObj.setFacilityID(1);
        requestObj.setFacilityName("Facility Name");
        requestObj.setFacilityTypeID(1);
        requestObj.setIsMainFacility(true);
        requestObj.setLastModDate(mock(java.sql.Date.class));
        requestObj.setLocation("Location");
        requestObj.setMainFacilityID(1);
        requestObj.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        requestObj.setPageNo(1);
        requestObj.setPhysicalLocation("Physical Location");
        requestObj.setProcessed('A');
        requestObj.setProviderServiceMapID(1);
        requestObj.setStatus("Status");
        requestObj.setStoreType("Store Type");
        requestObj.seteAushadhiFacilityId(1);

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> eAushadhiServiceImpl.getDrugDispenseDetails(patientIssueDetails, requestObj, 1));
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getDrugDispenseDetails(T_PatientIssue, M_Facility, Integer)}
     */
    @Test
    void testGetDrugDispenseDetails3() throws Exception {
        // Arrange
        when(patientIssueRepo.getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        T_PatientIssue patientIssueDetails = new T_PatientIssue();
        patientIssueDetails.setAge(1);
        patientIssueDetails.setBenRegID(1L);
        patientIssueDetails.setBeneficiaryID(1L);
        patientIssueDetails.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        patientIssueDetails.setCreatedDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientIssueDetails.setDeleted(true);
        patientIssueDetails.setDoctorName("Doctor Name");
        patientIssueDetails.setFacilityID(1);
        patientIssueDetails.setGender("Gender");
        patientIssueDetails.setIssueType("Issue Type");
        patientIssueDetails.setIssuedBy("Issued By");
        patientIssueDetails.setLastModDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientIssueDetails.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        patientIssueDetails.setParkingPlaceID(1L);
        patientIssueDetails.setPatientIssueID(1L);
        patientIssueDetails.setPatientName("Patient Name");
        patientIssueDetails.setPrescriptionID(1);
        patientIssueDetails.setProcessed('A');
        patientIssueDetails.setProviderServiceMapID(1);
        patientIssueDetails.setReference("Reference");
        patientIssueDetails.setSyncFacilityID(1);
        patientIssueDetails.setVanID(1L);
        patientIssueDetails.setVanSerialNo(1L);
        patientIssueDetails.setVisitCode(1L);
        patientIssueDetails.setVisitID(1);
        M_Facility requestObj = mock(M_Facility.class);
        when(requestObj.getFacilityID()).thenReturn(1);
        doNothing().when(requestObj).setCreatedBy(Mockito.<String>any());
        doNothing().when(requestObj).setCreatedDate(Mockito.<java.sql.Date>any());
        doNothing().when(requestObj).setDeleted(Mockito.<Boolean>any());
        doNothing().when(requestObj).setFacilityCode(Mockito.<String>any());
        doNothing().when(requestObj).setFacilityDesc(Mockito.<String>any());
        doNothing().when(requestObj).setFacilityID(Mockito.<Integer>any());
        doNothing().when(requestObj).setFacilityName(Mockito.<String>any());
        doNothing().when(requestObj).setFacilityTypeID(Mockito.<Integer>any());
        doNothing().when(requestObj).setIsMainFacility(Mockito.<Boolean>any());
        doNothing().when(requestObj).setLastModDate(Mockito.<java.sql.Date>any());
        doNothing().when(requestObj).setLocation(Mockito.<String>any());
        doNothing().when(requestObj).setMainFacilityID(Mockito.<Integer>any());
        doNothing().when(requestObj).setModifiedBy(Mockito.<String>any());
        doNothing().when(requestObj).setPageNo(Mockito.<Integer>any());
        doNothing().when(requestObj).setPhysicalLocation(Mockito.<String>any());
        doNothing().when(requestObj).setProcessed(Mockito.<Character>any());
        doNothing().when(requestObj).setProviderServiceMapID(Mockito.<Integer>any());
        doNothing().when(requestObj).setStatus(Mockito.<String>any());
        doNothing().when(requestObj).setStoreType(Mockito.<String>any());
        doNothing().when(requestObj).seteAushadhiFacilityId(Mockito.<Integer>any());
        requestObj.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        requestObj.setCreatedDate(mock(java.sql.Date.class));
        requestObj.setDeleted(true);
        requestObj.setFacilityCode("Facility Code");
        requestObj.setFacilityDesc("Facility Desc");
        requestObj.setFacilityID(1);
        requestObj.setFacilityName("Facility Name");
        requestObj.setFacilityTypeID(1);
        requestObj.setIsMainFacility(true);
        requestObj.setLastModDate(mock(java.sql.Date.class));
        requestObj.setLocation("Location");
        requestObj.setMainFacilityID(1);
        requestObj.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        requestObj.setPageNo(1);
        requestObj.setPhysicalLocation("Physical Location");
        requestObj.setProcessed('A');
        requestObj.setProviderServiceMapID(1);
        requestObj.setStatus("Status");
        requestObj.setStoreType("Store Type");
        requestObj.seteAushadhiFacilityId(1);

        // Act
        ArrayList<SyncDispenseDetailsRequest> actualDrugDispenseDetails = eAushadhiServiceImpl
                .getDrugDispenseDetails(patientIssueDetails, requestObj, 1);

        // Assert
        verify(requestObj).getFacilityID();
        verify(requestObj).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(requestObj).setCreatedDate(Mockito.<java.sql.Date>any());
        verify(requestObj).setDeleted(Mockito.<Boolean>any());
        verify(requestObj).setFacilityCode(eq("Facility Code"));
        verify(requestObj).setFacilityDesc(eq("Facility Desc"));
        verify(requestObj).setFacilityID(Mockito.<Integer>any());
        verify(requestObj).setFacilityName(eq("Facility Name"));
        verify(requestObj).setFacilityTypeID(Mockito.<Integer>any());
        verify(requestObj).setIsMainFacility(Mockito.<Boolean>any());
        verify(requestObj).setLastModDate(Mockito.<java.sql.Date>any());
        verify(requestObj).setLocation(eq("Location"));
        verify(requestObj).setMainFacilityID(Mockito.<Integer>any());
        verify(requestObj).setModifiedBy(eq("Jan 1, 2020 9:00am GMT+0100"));
        verify(requestObj).setPageNo(Mockito.<Integer>any());
        verify(requestObj).setPhysicalLocation(eq("Physical Location"));
        verify(requestObj).setProcessed(Mockito.<Character>any());
        verify(requestObj).setProviderServiceMapID(Mockito.<Integer>any());
        verify(requestObj).setStatus(eq("Status"));
        verify(requestObj).setStoreType(eq("Store Type"));
        verify(requestObj).seteAushadhiFacilityId(Mockito.<Integer>any());
        verify(patientIssueRepo).getDispensedDrugDetails(Mockito.<Integer>any(), Mockito.<Integer>any(),
                Mockito.<Long>any());
        assertTrue(actualDrugDispenseDetails.isEmpty());
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#sendStockAdditionAckToEAushadhi(Integer, Integer)}
     */
    @Test
    void testSendStockAdditionAckToEAushadhi() throws Exception {
        // Arrange
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(null);

        // Act
        String actualSendStockAdditionAckToEAushadhiResult = eAushadhiServiceImpl.sendStockAdditionAckToEAushadhi(1, 1);

        // Assert
        verify(httpUtils).getWithResponseEntity(eq("${eaushadhiStoreStockAckUrl}1/1"), Mockito.<HttpHeaders>any());
        assertEquals("0", actualSendStockAdditionAckToEAushadhiResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#sendStockAdditionAckToEAushadhi(Integer, Integer)}
     */
    @Test
    void testSendStockAdditionAckToEAushadhi2() throws Exception {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        // Act
        String actualSendStockAdditionAckToEAushadhiResult = eAushadhiServiceImpl.sendStockAdditionAckToEAushadhi(1, 1);

        // Assert
        verify(httpUtils).getWithResponseEntity(eq("${eaushadhiStoreStockAckUrl}1/1"), Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
        assertEquals("0", actualSendStockAdditionAckToEAushadhiResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#sendStockAdditionAckToEAushadhi(Integer, Integer)}
     */
    @Test
    void testSendStockAdditionAckToEAushadhi3() throws Exception {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("https://example.org/example");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        // Act
        String actualSendStockAdditionAckToEAushadhiResult = eAushadhiServiceImpl.sendStockAdditionAckToEAushadhi(1, 1);

        // Assert
        verify(httpUtils).getWithResponseEntity(eq("${eaushadhiStoreStockAckUrl}1/1"), Mockito.<HttpHeaders>any());
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
        assertEquals("0", actualSendStockAdditionAckToEAushadhiResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#sendStockAdditionAckToEAushadhi(Integer, Integer)}
     */
    @Test
    void testSendStockAdditionAckToEAushadhi4() throws Exception {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("0");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        // Act
        String actualSendStockAdditionAckToEAushadhiResult = eAushadhiServiceImpl.sendStockAdditionAckToEAushadhi(1, 1);

        // Assert
        verify(httpUtils).getWithResponseEntity(eq("${eaushadhiStoreStockAckUrl}1/1"), Mockito.<HttpHeaders>any());
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
        assertEquals("0", actualSendStockAdditionAckToEAushadhiResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#sendStockAdditionAckToEAushadhi(Integer, Integer)}
     */
    @Test
    void testSendStockAdditionAckToEAushadhi5() throws Exception {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("");
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        // Act
        String actualSendStockAdditionAckToEAushadhiResult = eAushadhiServiceImpl.sendStockAdditionAckToEAushadhi(1, 1);

        // Assert
        verify(httpUtils).getWithResponseEntity(eq("${eaushadhiStoreStockAckUrl}1/1"), Mockito.<HttpHeaders>any());
        verify(responseEntity).getBody();
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
        assertEquals("0", actualSendStockAdditionAckToEAushadhiResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#sendStockAdditionAckToEAushadhi(Integer, Integer)}
     */
    @Test
    void testSendStockAdditionAckToEAushadhi6() throws Exception {
        // Arrange
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.hasBody()).thenReturn(false);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.getWithResponseEntity(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(responseEntity);

        // Act
        String actualSendStockAdditionAckToEAushadhiResult = eAushadhiServiceImpl.sendStockAdditionAckToEAushadhi(1, 1);

        // Assert
        verify(httpUtils).getWithResponseEntity(eq("${eaushadhiStoreStockAckUrl}1/1"), Mockito.<HttpHeaders>any());
        verify(responseEntity).hasBody();
        verify(responseEntity).getStatusCodeValue();
        assertEquals("0", actualSendStockAdditionAckToEAushadhiResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#getFacilityStockProcessLog(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetFacilityStockProcessLog() throws FHIRException {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path $
        //       at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:397)
        //       at com.google.gson.Gson.fromJson(Gson.java:1227)
        //       at com.google.gson.Gson.fromJson(Gson.java:1137)
        //       at com.google.gson.Gson.fromJson(Gson.java:1047)
        //       at com.google.gson.Gson.fromJson(Gson.java:982)
        //       at com.wipro.fhir.utils.mapper.InputMapper.fromJson(InputMapper.java:70)
        //       at com.wipro.fhir.service.e_aushdhi.EAushadhiServiceImpl.getFacilityStockProcessLog(EAushadhiServiceImpl.java:1005)
        //   java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path $
        //       at com.google.gson.stream.JsonReader.beginObject(JsonReader.java:393)
        //       at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:386)
        //       at com.google.gson.Gson.fromJson(Gson.java:1227)
        //       at com.google.gson.Gson.fromJson(Gson.java:1137)
        //       at com.google.gson.Gson.fromJson(Gson.java:1047)
        //       at com.google.gson.Gson.fromJson(Gson.java:982)
        //       at com.wipro.fhir.utils.mapper.InputMapper.fromJson(InputMapper.java:70)
        //       at com.wipro.fhir.service.e_aushdhi.EAushadhiServiceImpl.getFacilityStockProcessLog(EAushadhiServiceImpl.java:1005)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange and Act
        eAushadhiServiceImpl.getFacilityStockProcessLog("Request");
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#updateSyncStatusForEAushadhiDispense(List)}
     */
    @Test
    void testUpdateSyncStatusForEAushadhiDispense() throws FHIRException {
        // Arrange, Act and Assert
        assertEquals("2", eAushadhiServiceImpl.updateSyncStatusForEAushadhiDispense(new ArrayList<>()));
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#updateSyncStatusForEAushadhiDispense(List)}
     */
    @Test
    void testUpdateSyncStatusForEAushadhiDispense2() throws FHIRException {
        // Arrange
        when(patientIssueRepo.updateEAushadhiIssueSyncStatus(Mockito.<Long>any())).thenReturn(1);

        ArrayList<String> syncedIssueDetails = new ArrayList<>();
        syncedIssueDetails.add("2");

        // Act
        String actualUpdateSyncStatusForEAushadhiDispenseResult = eAushadhiServiceImpl
                .updateSyncStatusForEAushadhiDispense(syncedIssueDetails);

        // Assert
        verify(patientIssueRepo).updateEAushadhiIssueSyncStatus(Mockito.<Long>any());
        assertEquals("1", actualUpdateSyncStatusForEAushadhiDispenseResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#updateSyncStatusForEAushadhiDispense(List)}
     */
    @Test
    void testUpdateSyncStatusForEAushadhiDispense3() throws FHIRException {
        // Arrange
        when(patientIssueRepo.updateEAushadhiIssueSyncStatus(Mockito.<Long>any())).thenReturn(0);

        ArrayList<String> syncedIssueDetails = new ArrayList<>();
        syncedIssueDetails.add("2");

        // Act
        String actualUpdateSyncStatusForEAushadhiDispenseResult = eAushadhiServiceImpl
                .updateSyncStatusForEAushadhiDispense(syncedIssueDetails);

        // Assert
        verify(patientIssueRepo).updateEAushadhiIssueSyncStatus(Mockito.<Long>any());
        assertEquals("2", actualUpdateSyncStatusForEAushadhiDispenseResult);
    }

    /**
     * Method under test:
     * {@link EAushadhiServiceImpl#updateSyncStatusForEAushadhiDispense(List)}
     */
    @Test
    void testUpdateSyncStatusForEAushadhiDispense4() throws FHIRException {
        // Arrange
        ArrayList<String> syncedIssueDetails = new ArrayList<>();
        syncedIssueDetails.add("foo");
        syncedIssueDetails.add("2");

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> eAushadhiServiceImpl.updateSyncStatusForEAushadhiDispense(syncedIssueDetails));
    }
}
