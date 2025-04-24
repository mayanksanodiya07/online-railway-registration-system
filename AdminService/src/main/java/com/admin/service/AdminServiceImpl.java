package com.admin.service;

import com.admin.client.TrainClient;
import com.admin.model.Train;

import jakarta.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private TrainClient trainClient;

    // Add multiple trains
    public List<Train> addMultipleTrains(List<Train> trains) {
        return trainClient.addMultipleTrains(trains);
    }

    // Delete a train by ID
    public void deleteTrain(Long id) {
        trainClient.deleteTrain(id);
    }

    // Delete all trains
    public void deleteAllTrains() {
        trainClient.deleteAllTrains();
    }

    // Update train source
    public Train updateTrainSource(Long id, String source) {
        return trainClient.updateTrainSource(id, source);
    }

    // Update train destination
    public Train updateTrainDestination(Long id, String destination) {
        return trainClient.updateTrainDestination(id, destination);
    }

    // Update train source and destination
    public Train updateTrainSourceAndDestination(Long id, String source, String destination) {
        return trainClient.updateTrainSourceAndDestination(id, source, destination);
    }

    // Update train departure time
    public Train updateTrainDepartureTime(Long id, String departureTime) {
        return trainClient.updateTrainDepartureTime(id, departureTime);
    }

    // Update train arrival time
    public Train updateTrainArrivalTime(Long id, String arrivalTime) {
        return trainClient.updateTrainArrivalTime(id, arrivalTime);
    }

    // Update both departure and arrival time
    public Train updateTrainTimes(Long id, String departureTime, String arrivalTime) {
        return trainClient.updateTrainTimes(id, departureTime, arrivalTime);
    }

    // Update available seats
    public Train updateTrainAvailableSeats(Long id, int availableSeats) {
        return trainClient.updateTrainAvailableSeats(id, availableSeats);
    }
}
