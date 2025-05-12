package com.example.billservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillController.class)
class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillService billService;

    @Autowired
    private ObjectMapper objectMapper;

    private BillResponseDTO billResponseDTO;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        billResponseDTO = new BillResponseDTO(
                1L,
                100L,
                "John Doe",
                "session123",
                500.0,
                "CARD",
                "PENDING",
                LocalDateTime.now()
        );

        paymentResponse = new PaymentResponse(
                "John Doe",
                "session123",
                "https://payment-url.com"
        );
    }

    @Test
    void testCreateBill_Success() throws Exception {
        Mockito.when(billService.createBill(Mockito.eq(100L))).thenReturn(paymentResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bills/generate")
                        .param("bookingId", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.sessionId").value("session123"))
                .andExpect(jsonPath("$.sessionUrl").value("https://payment-url.com"));
    }

    @Test
    void testGetBill_Success() throws Exception {
        Mockito.when(billService.getBill(1L)).thenReturn(billResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.totalAmount").value(500.0));
    }

    @Test
    void testGetAllBills_Success() throws Exception {
        List<BillResponseDTO> billList = List.of(billResponseDTO);
        Mockito.when(billService.getAllBills()).thenReturn(billList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerName").value("John Doe"));
    }
}