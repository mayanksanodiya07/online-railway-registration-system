package com.train.service;


import java.time.LocalDateTime;
import java.util.List;

import com.train.entity.Train;

public interface TrainService {

	 Train addTrain(Train train);

	List<Train> addMultipleTrains(List<Train> trains);

	List<Train> getAllTrains();

	Train getTrainById(Long id);

	List<Train> searchTrains(String source, String destination);

	void deleteTrainById(Long id);

	void deleteAllTrains();

	void bookSeats(Long trainId, int seatsToBook);

	Train updateTrainSource(Long id, String source);

	Train updateTrainDestination(Long id, String destination);

	Train updateTrainSourceAndDestination(Long id, String source, String destination);

	Train updateTrainDepartureTime(Long id, LocalDateTime departureTime);

	Train updateTrainArrivalTime(Long id, LocalDateTime arrivalTime);

	Train updateTrainTimes(Long id, LocalDateTime departureTime, LocalDateTime arrivalTime);

	Train updateAvailableSeats(Long id, int availableSeats);

}
