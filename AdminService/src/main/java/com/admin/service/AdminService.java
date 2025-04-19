package com.admin.service;

import com.admin.model.Train;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {


    // Add multiple trains
    public List<Train> addMultipleTrains(List<Train> trains);

    // Delete a train by ID
    public void deleteTrain(Long id);

    // Delete all trains
    public void deleteAllTrains();

    // Update train source
    public Train updateTrainSource(Long id, String source);

    // Update train destination
    public Train updateTrainDestination(Long id, String destination);

    // Update train source and destination
    public Train updateTrainSourceAndDestination(Long id, String source, String destination);

    // Update train departure time
    public Train updateTrainDepartureTime(Long id, String departureTime);

    // Update train arrival time
    public Train updateTrainArrivalTime(Long id, String arrivalTime);

    // Update both departure and arrival time
    public Train updateTrainTimes(Long id, String departureTime, String arrivalTime);

    // Update available seats
    public Train updateTrainAvailableSeats(Long id, int availableSeats);
}
