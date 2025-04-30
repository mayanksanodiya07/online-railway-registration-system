package com.train.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.train.config.SecurityConfig;
import com.train.entity.Train;
import com.train.service.TrainService;
import com.train.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainController.class)
@Import(SecurityConfig.class)
public class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;
    @MockBean
    private JwtUtil jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;

    private Train train1, train2;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        train1 = new Train(1L, "Rajdhani Express", "DELHI", "MUMBAI", LocalDateTime.of(2025, 5, 1, 10, 0),
                LocalDateTime.of(2025, 5, 1, 15, 0), 100, 0);
        train2 = new Train(2L, "Vande Bharat", "PUNE", "CHENNAI", LocalDateTime.of(2025, 5, 2, 9, 0),
                LocalDateTime.of(2025, 5, 2, 15, 0), 150, 10);
    }
    
    @Test 
    @WithMockUser(username = "admin", roles = {"ADMIN-SERVICE"})
    public void testAddTrain() throws Exception {
        when(trainService.addTrain(any(Train.class))).thenReturn(train1); 

        mockMvc.perform(post("/trains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(train1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("DELHI"));
    }

    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testAddMultipleTrains() throws Exception {
        List<Train> trains = Arrays.asList(train1, train2);
        when(trainService.addMultipleTrains(any())).thenReturn(trains);

        mockMvc.perform(post("/trains/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trains)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetAllTrains() throws Exception {
        List<Train> trains = Arrays.asList(train1, train2);
        when(trainService.getAllTrains()).thenReturn(trains);

        mockMvc.perform(get("/trains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].source").value("DELHI"))
                .andExpect(jsonPath("$[1].source").value("PUNE"));
    }

    @Test
    public void testGetTrainById() throws Exception {
        when(trainService.getTrainById(1L)).thenReturn(train1);

        mockMvc.perform(get("/trains/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").value("MUMBAI"));
    }

    @Test
    public void testSearchTrains() throws Exception {
        when(trainService.searchTrains("PUNE", "CHENNAI")).thenReturn(List.of(train2));

        mockMvc.perform(get("/trains/search")
                        .param("source", "PUNE")
                        .param("destination", "CHENNAI"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destination").value("CHENNAI"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN-SERVICE"})
    public void testDeleteTrainById() throws Exception {
        mockMvc.perform(delete("/trains/1"))
                .andExpect(status().isOk());
        verify(trainService).deleteTrainById(1L);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testDeleteAllTrains() throws Exception {
        mockMvc.perform(delete("/trains"))
                .andExpect(status().isOk())
                .andExpect(content().string("All trains deleted successfully"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testUpdateTrainSource() throws Exception {
        Train updated = new Train(train1);
        updated.setSource("AGRA");
        when(trainService.updateTrainSource(1L, "AGRA")).thenReturn(updated);

        mockMvc.perform(patch("/trains/1/source").param("source", "AGRA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("AGRA"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testUpdateTrainDestination() throws Exception {
        Train updated = new Train(train1);
        updated.setDestination("KOLKATA");
        when(trainService.updateTrainDestination(1L, "KOLKATA")).thenReturn(updated);

        mockMvc.perform(patch("/trains/1/destination").param("destination", "KOLKATA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").value("KOLKATA"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testUpdateTrainSourceAndDestination() throws Exception {
        Train updated = new Train(train1);
        updated.setSource("AGRA");
        updated.setDestination("LUCKNOW");
        when(trainService.updateTrainSourceAndDestination(1L, "AGRA", "LUCKNOW")).thenReturn(updated);

        mockMvc.perform(patch("/trains/1/source-destination")
                        .param("source", "AGRA")
                        .param("destination", "LUCKNOW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("AGRA"))
                .andExpect(jsonPath("$.destination").value("LUCKNOW"));
    }

    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testUpdateDepartureTime() throws Exception {
        LocalDateTime newTime = LocalDateTime.of(2025, 5, 3, 8, 0);
        Train updated = new Train(train1);
        updated.setDepartureTime(newTime);
        when(trainService.updateTrainDepartureTime(1L, newTime)).thenReturn(updated);

        mockMvc.perform(patch("/trains/1/departure-time")
                        .param("departureTime", newTime.toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.departureTime").value(newTime.toString()));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testUpdateArrivalTime() throws Exception {
        LocalDateTime newTime = LocalDateTime.of(2025, 5, 3, 20, 30);
        Train updated = new Train(train1);
        updated.setArrivalTime(newTime);
        when(trainService.updateTrainArrivalTime(1L, newTime)).thenReturn(updated);

        mockMvc.perform(patch("/trains/1/arrival-time")
                        .param("arrivalTime", newTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrivalTime").value(newTime.toString()));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testUpdateTrainTimes() throws Exception {
        LocalDateTime departure = LocalDateTime.of(2025, 5, 5, 6, 0);
        LocalDateTime arrival = LocalDateTime.of(2025, 5, 5, 12, 30);

        Train updated = new Train(train1);
        updated.setDepartureTime(departure);
        updated.setArrivalTime(arrival);

        when(trainService.updateTrainTimes(1L, departure, arrival)).thenReturn(updated);

        mockMvc.perform(patch("/trains/1/times")
                        .param("departureTime", departure.toString())
                        .param("arrivalTime", arrival.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departureTime").value(departure.toString()))
                .andExpect(jsonPath("$.arrivalTime").value(arrival.toString()));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN-SERVICE")
    public void testUpdateAvailableSeats() throws Exception {
        Train updated = new Train(train1);
        updated.setAvailableSeats(50);

        when(trainService.updateAvailableSeats(1L, 50)).thenReturn(updated);

        mockMvc.perform(patch("/trains/1/availableSeats").param("availableSeats", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSeats").value(50));
    }

    @Test
    @WithMockUser(roles = "BOOKING")
    public void testBookSeats() throws Exception {
        mockMvc.perform(post("/trains/1/book").param("seats", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Seats booked successfully!"));
    }

}
