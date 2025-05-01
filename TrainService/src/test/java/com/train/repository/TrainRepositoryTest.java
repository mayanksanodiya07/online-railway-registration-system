package com.train.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.train.entity.Train;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Optional: use real DB
class TrainRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrainRepository trainRepository;

    private Train train;

    @BeforeEach
    void setUp() {
        train = new Train();
        train.setTrainName("Shatabdi");
        train.setSource("Bhopal");
        train.setDestination("Delhi");
        train.setAvailableSeats(50);
        train.setBookedSeats(0);
        train.setDepartureTime(LocalDateTime.of(2025, 5, 10, 10, 0));
        train.setArrivalTime(LocalDateTime.of(2025, 5, 10, 18, 0));

        entityManager.persist(train);
    }


    @Test
    void testFindBySourceAndDestination() {
        List<Train> trains = trainRepository.findBySourceAndDestination("Bhopal", "Delhi");
        assertEquals(1, trains.size());
        assertEquals("Shatabdi", trains.get(0).getTrainName());
    }

    @Test
    void testUpdateBookedSeats() {
        trainRepository.updateBookedSeats(train.getId(), 10, 40);
        
        entityManager.flush();
        entityManager.clear();
        
        Train updated = entityManager.find(Train.class, train.getId());
        assertEquals(10, updated.getBookedSeats());
        assertEquals(40, updated.getAvailableSeats());
    }
}
