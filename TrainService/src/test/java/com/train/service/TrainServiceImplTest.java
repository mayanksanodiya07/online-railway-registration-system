package com.train.service;

import com.train.entity.Train;
import com.train.exception.InvalidTrainOperationException;
import com.train.exception.TrainNotFoundException;
import com.train.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainServiceImplTest {

    @InjectMocks
    private TrainServiceImpl trainService;

    @Mock
    private TrainRepository trainRepository;

    private Train sampleTrain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTrain = new Train();
        sampleTrain.setId(1L);
        sampleTrain.setTrainName("Rajdhani Express");
        sampleTrain.setSource("Mumbai");
        sampleTrain.setDestination("Delhi");
        sampleTrain.setAvailableSeats(100);
        sampleTrain.setBookedSeats(0);
        sampleTrain.setDepartureTime(LocalDateTime.now().plusHours(2));
        sampleTrain.setArrivalTime(LocalDateTime.now().plusHours(10));
    }

    @Test
    void testAddTrain() {
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train savedTrain = trainService.addTrain(sampleTrain);

        assertNotNull(savedTrain);
        assertEquals("Mumbai", savedTrain.getSource());
    }
    
    @Test
    void testAddTrain_InvalidTimes() {
        sampleTrain.setDepartureTime(LocalDateTime.now().plusHours(5));
        sampleTrain.setArrivalTime(LocalDateTime.now().plusHours(2)); // Invalid

        InvalidTrainOperationException exception = assertThrows(InvalidTrainOperationException.class, () -> {
            trainService.addTrain(sampleTrain);
        });

        assertEquals("Arrival time cannot be before departure time.", exception.getMessage());
    }

    @Test
    void testAddMultipleTrains() {
        List<Train> trains = List.of(sampleTrain);
        when(trainRepository.saveAll(trains)).thenReturn(trains);

        List<Train> savedTrains = trainService.addMultipleTrains(trains);

        assertEquals(1, savedTrains.size());
        assertEquals("Rajdhani Express", savedTrains.get(0).getTrainName());
    }

    @Test
    void testGetAllTrains() {
        List<Train> trains = List.of(sampleTrain);
        when(trainRepository.findAll()).thenReturn(trains);

        List<Train> result = trainService.getAllTrains();

        assertEquals(1, result.size());
    }

    @Test
    void testGetTrainById_Found() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));

        Train result = trainService.getTrainById(1L);

        assertNotNull(result);
        assertEquals("Mumbai", result.getSource());
    }

    @Test
    void testGetTrainById_NotFound() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        TrainNotFoundException exception = assertThrows(TrainNotFoundException.class, () -> {
            trainService.getTrainById(1L);
        });

        assertEquals("Train with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testSearchTrains() {
        List<Train> mockTrains = List.of(sampleTrain);
        when(trainRepository.findBySourceAndDestination("Mumbai", "Delhi")).thenReturn(mockTrains);

        List<Train> result = trainService.searchTrains("Mumbai", "Delhi");

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteTrainById() {
    	when(trainRepository.existsById(1L)).thenReturn(true);
        doNothing().when(trainRepository).deleteById(1L);

        trainService.deleteTrainById(1L);

        verify(trainRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeleteTrainById_NotFound() {
        when(trainRepository.existsById(1L)).thenReturn(false);

        TrainNotFoundException exception = assertThrows(TrainNotFoundException.class, () -> {
            trainService.deleteTrainById(1L);
        });

        assertEquals("Cannot delete. Train with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testDeleteAllTrains() {
    	
        doNothing().when(trainRepository).deleteAll();

        trainService.deleteAllTrains();

        verify(trainRepository, times(1)).deleteAll();
    }

    @Test
    void testBookSeats() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        trainService.bookSeats(1L, 10);

        assertEquals(90, sampleTrain.getAvailableSeats());
        assertEquals(10, sampleTrain.getBookedSeats());
    }
    
    @Test
    void testBookSeats_InvalidInput() {
        InvalidTrainOperationException exception = assertThrows(InvalidTrainOperationException.class, () -> {
            trainService.bookSeats(1L, 0);
        });

        assertEquals("Number of seats to book must be positive.", exception.getMessage());
    }
    
    @Test
    void testBookSeats_NotEnoughSeats() {
        sampleTrain.setAvailableSeats(5);
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));

        InvalidTrainOperationException exception = assertThrows(InvalidTrainOperationException.class, () -> {
            trainService.bookSeats(1L, 10);
        });

        assertEquals("Not enough available seats.", exception.getMessage());
    }

    @Test
    void testReleaseSeats() {
        sampleTrain.setAvailableSeats(90);
        sampleTrain.setBookedSeats(10);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        trainService.releaseSeats(1L, 5);

        assertEquals(95, sampleTrain.getAvailableSeats());
        assertEquals(5, sampleTrain.getBookedSeats());
    }
    
    @Test
    void testReleaseSeats_InvalidInput() {
        InvalidTrainOperationException exception = assertThrows(InvalidTrainOperationException.class, () -> {
            trainService.releaseSeats(1L, 0);
        });

        assertEquals("Number of seats to release must be positive.", exception.getMessage());
    }

    @Test
    void testReleaseSeats_MoreThanBooked() {
        sampleTrain.setAvailableSeats(90);
        sampleTrain.setBookedSeats(5);
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));

        InvalidTrainOperationException exception = assertThrows(InvalidTrainOperationException.class, () -> {
            trainService.releaseSeats(1L, 10);
        });

        assertEquals("Cannot release more seats than booked.", exception.getMessage());
    }

    @Test
    void testUpdateAvailableSeats() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train updated = trainService.updateAvailableSeats(1L, 80);

        assertEquals(80, updated.getAvailableSeats());
    }
    
    @Test
    void testUpdateAvailableSeats_Negative() {
        InvalidTrainOperationException exception = assertThrows(InvalidTrainOperationException.class, () -> {
            trainService.updateAvailableSeats(1L, -5);
        });

        assertEquals("Available seats cannot be negative.", exception.getMessage());
    }

    @Test
    void testUpdateTrainSource() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train updated = trainService.updateTrainSource(1L, "Nagpur");

        assertEquals("Nagpur", updated.getSource());
    }

    @Test
    void testUpdateTrainDestination() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train updated = trainService.updateTrainDestination(1L, "Chennai");

        assertEquals("Chennai", updated.getDestination());
    }

    @Test
    void testUpdateTrainSourceAndDestination() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train updated = trainService.updateTrainSourceAndDestination(1L, "Pune", "Bangalore");

        assertEquals("Pune", updated.getSource());
        assertEquals("Bangalore", updated.getDestination());
    }

    @Test
    void testUpdateTrainDepartureTime() {
        LocalDateTime newDeparture = LocalDateTime.now().plusDays(1);
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train updated = trainService.updateTrainDepartureTime(1L, newDeparture);

        assertEquals(newDeparture, updated.getDepartureTime());
    }

    @Test
    void testUpdateTrainArrivalTime() {
        LocalDateTime newArrival = LocalDateTime.now().plusDays(1).plusHours(6);
        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train updated = trainService.updateTrainArrivalTime(1L, newArrival);

        assertEquals(newArrival, updated.getArrivalTime());
    }

    @Test
    void testUpdateTrainTimes() {
        LocalDateTime dep = LocalDateTime.now().plusDays(2);
        LocalDateTime arr = dep.plusHours(6);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(sampleTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(sampleTrain);

        Train updated = trainService.updateTrainTimes(1L, dep, arr);

        assertEquals(dep, updated.getDepartureTime());
        assertEquals(arr, updated.getArrivalTime());
    }
}
