package com.train.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.train.entity.Train;
import com.train.repository.TrainRepository;

import jakarta.transaction.Transactional;

@Service
public class TrainServiceImpl implements TrainService {

	private static final Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);

	@Autowired
	private TrainRepository trainRepository;

	@Override
	public Train addTrain(Train train) {
		try {
			return trainRepository.save(train);
		} catch (Exception e) {
			logger.error("Error adding train: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to add train: " + e.getMessage());
		}
	}

	@Override
	public List<Train> addMultipleTrains(List<Train> trains) {
		try {
			return trainRepository.saveAll(trains);
		} catch (Exception e) {
			logger.error("Error adding multiple trains: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to add multiple trains: " + e.getMessage());
		}
	}

	@Override
	public List<Train> getAllTrains() {
		try {
			return trainRepository.findAll();
		} catch (Exception e) {
			logger.error("Error retrieving all trains: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to retrieve all trains: " + e.getMessage());
		}
	}

	@Override
	public Train getTrainById(Long id) {
		return trainRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with ID: " + id));
	}

	@Override
	public List<Train> searchTrains(String source, String destination) {
		try {
			return trainRepository.findBySourceAndDestination(source, destination);
		} catch (Exception e) {
			logger.error("Error searching trains: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to search trains: " + e.getMessage());
		}
	}

	@Override
	public void deleteTrainById(Long id) {
		try {
			trainRepository.deleteById(id);
		} catch (Exception e) {
			logger.error("Error deleting train by ID: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to delete train: " + e.getMessage());
		}
	}

	@Override
	public void deleteAllTrains() {
		try {
			trainRepository.deleteAll();
		} catch (Exception e) {
			logger.error("Error deleting all trains: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to delete all trains: " + e.getMessage());
		}
	}

	@Override 
	@Transactional
	public void bookSeats(Long trainId, int seatsToBook) {
		logger.info("Booking {} seats for train ID {}", seatsToBook, trainId);

		Train train = trainRepository.findById(trainId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with ID: " + trainId));

		logger.info("Train found: {} with {} available seats", train.getTrainName(), train.getAvailableSeats());

		if (seatsToBook > train.getAvailableSeats()) {
			logger.warn("Not enough seats available! Requested: {}, Available: {}", seatsToBook,
					train.getAvailableSeats());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough seats available!");
		}

		try {
			train.setAvailableSeats(train.getAvailableSeats() - seatsToBook);
			train.setBookedSeats(train.getBookedSeats() + seatsToBook);
			trainRepository.save(train);
		} catch (Exception e) {
			logger.error("Error booking seats: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to book seats: " + e.getMessage());
		}

		logger.info("Booking successful! Remaining seats: {}", train.getAvailableSeats());
	}

	@Override
	@Transactional
	public void releaseSeats(Long trainId, int seatsToRelease) {
	    logger.info("Releasing {} seats for train ID {}", seatsToRelease, trainId);

	    Train train = trainRepository.findById(trainId).orElseThrow(
	            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with ID: " + trainId));

	    logger.info("Train found: {} with {} booked seats", train.getTrainName(), train.getBookedSeats());

	    if (seatsToRelease <= 0) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seats to release must be positive");
	    }

	    if (seatsToRelease > train.getBookedSeats()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot release more seats than booked");
	    }

	    try {
	        train.setAvailableSeats(train.getAvailableSeats() + seatsToRelease);
	        train.setBookedSeats(train.getBookedSeats() - seatsToRelease);
	        trainRepository.save(train);
	    } catch (Exception e) {
	        logger.error("Error releasing seats: {}", e.getMessage());
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                "Failed to release seats: " + e.getMessage());
	    }

	    logger.info("Seats released successfully. Updated available seats: {}", train.getAvailableSeats());
	}

	@Override
	public Train updateAvailableSeats(Long id, int availableSeats) {
		return trainRepository.findById(id).map(train -> {
			try {
				train.setAvailableSeats(availableSeats);
				return trainRepository.save(train);
			} catch (Exception e) {
				logger.error("Error updating available seats: {}", e.getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Failed to update available seats: " + e.getMessage());
			}
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with ID: " + id));
	}

	// Similar exception handling for other update methods...
	@Override
	public Train updateTrainSource(Long id, String source) {
		return handleUpdate(id, train -> train.setSource(source), "source");
	}

	@Override
	public Train updateTrainDestination(Long id, String destination) {
		return handleUpdate(id, train -> train.setDestination(destination), "destination");
	}

	@Override
	public Train updateTrainSourceAndDestination(Long id, String source, String destination) {
		return handleUpdate(id, train -> {
			train.setSource(source);
			train.setDestination(destination);
		}, "source and destination");
	}

	@Override
	public Train updateTrainDepartureTime(Long id, LocalDateTime departureTime) {
		return handleUpdate(id, train -> train.setDepartureTime(departureTime), "departure time");
	}

	@Override
	public Train updateTrainArrivalTime(Long id, LocalDateTime arrivalTime) {
		return handleUpdate(id, train -> train.setArrivalTime(arrivalTime), "arrival time");
	}

	@Override
	public Train updateTrainTimes(Long id, LocalDateTime departureTime, LocalDateTime arrivalTime) {
		return handleUpdate(id, train -> {
			train.setDepartureTime(departureTime);
			train.setArrivalTime(arrivalTime);
		}, "departure and arrival times");
	}

	private Train handleUpdate(Long id, Consumer<Train> updateFunction, String updateType) {
		return trainRepository.findById(id).map(train -> {
			try {
				updateFunction.accept(train);
				System.out.println(train);
				return trainRepository.save(train);
			} catch (Exception e) {
				logger.error("Error updating train {}: {}", updateType, e.getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Failed to update train " + updateType + ": " + e.getMessage());
			}
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with ID: " + id));
	}
}