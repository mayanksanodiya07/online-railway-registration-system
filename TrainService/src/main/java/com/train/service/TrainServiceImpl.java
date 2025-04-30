package com.train.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.train.entity.Train;
import com.train.exception.InvalidTrainOperationException;
import com.train.exception.TrainNotFoundException;
import com.train.repository.TrainRepository;

import jakarta.transaction.Transactional;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Override
    public Train addTrain(Train train) {
        validateTrainData(train);
        return trainRepository.save(train);
    }

    @Override
    public List<Train> addMultipleTrains(List<Train> trains) {
        trains.forEach(this::validateTrainData);
        return trainRepository.saveAll(trains);
    }

    @Override
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public Train getTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new TrainNotFoundException("Train with ID " + id + " not found."));
    }

    @Override
    public List<Train> searchTrains(String source, String destination) {
        return trainRepository.findBySourceAndDestination(source, destination);
    }

    @Override
    public void deleteTrainById(Long id) {
        if (!trainRepository.existsById(id)) {
            throw new TrainNotFoundException("Cannot delete. Train with ID " + id + " not found.");
        }
        trainRepository.deleteById(id);
    }

    @Override
    public void deleteAllTrains() {
        trainRepository.deleteAll();
    }

    @Override
    @Transactional
    public void bookSeats(Long trainId, int seatsToBook) {
        if (seatsToBook <= 0) {
            throw new InvalidTrainOperationException("Number of seats to book must be positive.");
        }
        Train train = getTrainById(trainId);
        if (train.getAvailableSeats() < seatsToBook) {
            throw new InvalidTrainOperationException("Not enough available seats.");
        }
        train.setAvailableSeats(train.getAvailableSeats() - seatsToBook);
        train.setBookedSeats(train.getBookedSeats() + seatsToBook);
        trainRepository.save(train);
    }

    @Override
    @Transactional
    public void releaseSeats(Long trainId, int seatsToRelease) {
        if (seatsToRelease <= 0) {
            throw new InvalidTrainOperationException("Number of seats to release must be positive.");
        }
        Train train = getTrainById(trainId);
        if (train.getBookedSeats() < seatsToRelease) {
            throw new InvalidTrainOperationException("Cannot release more seats than booked.");
        }
        train.setAvailableSeats(train.getAvailableSeats() + seatsToRelease);
        train.setBookedSeats(train.getBookedSeats() - seatsToRelease);
        trainRepository.save(train);
    }

    @Override
    public Train updateAvailableSeats(Long id, int availableSeats) {
        if (availableSeats < 0) {
            throw new InvalidTrainOperationException("Available seats cannot be negative.");
        }
        return handleUpdate(id, train -> train.setAvailableSeats(availableSeats));
    }

    @Override
    public Train updateTrainSource(Long id, String source) {
        return handleUpdate(id, train -> train.setSource(source));
    }

    @Override
    public Train updateTrainDestination(Long id, String destination) {
        return handleUpdate(id, train -> train.setDestination(destination));
    }

    @Override
    public Train updateTrainSourceAndDestination(Long id, String source, String destination) {
        return handleUpdate(id, train -> {
            train.setSource(source);
            train.setDestination(destination);
        });
    }

    @Override
    public Train updateTrainDepartureTime(Long id, LocalDateTime departureTime) {
        return handleUpdate(id, train -> train.setDepartureTime(departureTime));
    }

    @Override
    public Train updateTrainArrivalTime(Long id, LocalDateTime arrivalTime) {
        return handleUpdate(id, train -> train.setArrivalTime(arrivalTime));
    }

    @Override
    public Train updateTrainTimes(Long id, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        return handleUpdate(id, train -> {
            train.setDepartureTime(departureTime);
            train.setArrivalTime(arrivalTime);
        });
    }

    private Train handleUpdate(Long id, Consumer<Train> updateFunction) {
        Train train = getTrainById(id);
        updateFunction.accept(train);
        return trainRepository.save(train);
    }

    private void validateTrainData(Train train) {
        if (train.getAvailableSeats() < 0 || train.getBookedSeats() < 0) {
            throw new InvalidTrainOperationException("Seat counts cannot be negative.");
        }
        if (train.getDepartureTime() != null && train.getArrivalTime() != null &&
                train.getArrivalTime().isBefore(train.getDepartureTime())) {
            throw new InvalidTrainOperationException("Arrival time cannot be before departure time.");
        }
    }
}
