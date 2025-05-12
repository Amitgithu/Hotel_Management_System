package com.example.billservice.service.imple;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.BookingResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.dto.RoomResponseDto;
import com.example.billservice.dto.GuestResponseDTO;
import com.example.billservice.entity.Bill;
import com.example.billservice.exception.ResourceNotFoundException;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.BookingServiceClient;
import com.example.billservice.service.imple.PaymentService;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private BookingServiceClient bookingServiceClient;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BillServiceImpl billService;

    private BookingResponseDTO bookingResponseDTO;
    private Bill bill;
    private RoomResponseDto roomResponseDto;
    private GuestResponseDTO guestResponseDTO;
    private PaymentResponse paymentResponse;
    private Session mockSession;

    @BeforeEach
    void setUp() {
        // Mock Room & Guest Data
        roomResponseDto = new RoomResponseDto(1L, "101", "DELUXE", 150.0, true, Instant.now());
        guestResponseDTO = new GuestResponseDTO(1L, "John Doe", "john@example.com", "1234567890",
                "123 Main St", "American", GuestResponseDTO.IdProofType.PASSPORT, "XYZ12345", Instant.now());

        // Mock Booking Data
        bookingResponseDTO = new BookingResponseDTO(
                1L, guestResponseDTO, roomResponseDto, LocalDate.now(), LocalDate.now().plusDays(2), 2, "PENDING");

        // Mock Bill Data
        bill = new Bill(1L, 1L, "John Doe", 150.0, "CARD", "PENDING", LocalDateTime.now(), "session123");

        // Mock Payment Session
        mockSession = new Session();
        mockSession.setId("session123");
        mockSession.setUrl("https://mock-payment-url.com");

        // Mock Payment Response
        paymentResponse = new PaymentResponse("John Doe", "session123", "https://mock-payment-url.com");
    }

    @Test
    void testCreateBill_NewBooking_Success() throws Exception {
        when(bookingServiceClient.getBooking(1L)).thenReturn(bookingResponseDTO);
        when(paymentService.createCheckoutSession(150.0, "INR", "John Doe", "john@example.com"))
                .thenReturn(mockSession);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        PaymentResponse response = billService.createBill(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getCustomerName());
        assertEquals("session123", response.getSessionId());
        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    void testCreateBill_AlreadyPaid_ShouldReturnExistingSession() throws Exception {
        bill.setPaymentStatus("SUCCESS");
        when(billRepository.findByBookingId(1L)).thenReturn(Optional.of(bill));

        PaymentResponse response = billService.createBill(1L);

        assertEquals("Bill is already paid for booking ID: 1", response.getSessionUrl());
        verify(paymentService, never()).createCheckoutSession(anyDouble(), anyString(), anyString(), anyString());
    }

    @Test
    void testGetBill_Success() {
        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));
        when(modelMapper.map(bill, BillResponseDTO.class)).thenReturn(new BillResponseDTO(
                1L, 1L, "John Doe", "session123", 150.0, "CARD", "PENDING", LocalDateTime.now()));

        BillResponseDTO response = billService.getBill(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getCustomerName());
    }

    @Test
    void testGetBill_NotFound_ShouldThrowException() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> billService.getBill(1L));
    }

    @Test
    void testGetAllBills_Success() {
        when(billRepository.findAll()).thenReturn(List.of(bill));
        when(modelMapper.map(bill, BillResponseDTO.class)).thenReturn(new BillResponseDTO(
                1L, 1L, "John Doe", "session123", 150.0, "CARD", "PENDING", LocalDateTime.now()));

        List<BillResponseDTO> bills = billService.getAllBills();

        assertEquals(1, bills.size());
        assertEquals("John Doe", bills.get(0).getCustomerName());
    }
}