package com.admin.controller;

import com.admin.model.Train;
import com.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/trains")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Add multiple trains
    @PostMapping("/bulk")
    public ResponseEntity<List<Train>> addMultipleTrains(@RequestBody List<Train> trains) {
    	System.out.println("okokok");
        List<Train> addedTrains = adminService.addMultipleTrains(trains);
        return ResponseEntity.ok(addedTrains);
    }

    // Delete a train by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        adminService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    // Delete all trains
    @DeleteMapping
    public ResponseEntity<Void> deleteAllTrains() {
        adminService.deleteAllTrains();
        return ResponseEntity.noContent().build();
    }

    // Update train source
    @PatchMapping("/{id}/source")
    public ResponseEntity<Train> updateTrainSource(@PathVariable Long id, @RequestParam String source) {

        Train updatedTrain = adminService.updateTrainSource(id, source);
        return ResponseEntity.ok(updatedTrain);
    }

    // Update train destination
    @PatchMapping("/{id}/destination")
    public ResponseEntity<Train> updateTrainDestination(@PathVariable Long id, @RequestParam String destination) {
        Train updatedTrain = adminService.updateTrainDestination(id, destination);
        return ResponseEntity.ok(updatedTrain);
    }

    // Update both source and destination
    @PatchMapping("/{id}/source-destination")
    public ResponseEntity<Train> updateTrainSourceAndDestination(@PathVariable Long id,
                                                                @RequestParam String source,
                                                                @RequestParam String destination) {
        Train updatedTrain = adminService.updateTrainSourceAndDestination(id, source, destination);
        return ResponseEntity.ok(updatedTrain);
    }

    // Update train departure time
    @PatchMapping("/{id}/departure-time")
    public ResponseEntity<Train> updateTrainDepartureTime(@PathVariable Long id, @RequestParam String departureTime) {
        Train updatedTrain = adminService.updateTrainDepartureTime(id, departureTime);
        return ResponseEntity.ok(updatedTrain);
    }

    // Update train arrival time
    @PatchMapping("/{id}/arrival-time")
    public ResponseEntity<Train> updateTrainArrivalTime(@PathVariable Long id, @RequestParam String arrivalTime) {
        Train updatedTrain = adminService.updateTrainArrivalTime(id, arrivalTime);
        return ResponseEntity.ok(updatedTrain);
    }

    // Update both departure and arrival time
    @PatchMapping("/{id}/times")
    public ResponseEntity<Train> updateTrainTimes(@PathVariable Long id,
                                                 @RequestParam String departureTime,
                                                 @RequestParam String arrivalTime) {
        Train updatedTrain = adminService.updateTrainTimes(id, departureTime, arrivalTime);
        return ResponseEntity.ok(updatedTrain);
    }

    // Update available seats
    @PatchMapping("/{id}/availableSeats")
    public ResponseEntity<Train> updateTrainAvailableSeats(@PathVariable Long id,
                                                           @RequestParam int availableSeats) {
        Train updatedTrain = adminService.updateTrainAvailableSeats(id, availableSeats);
        return ResponseEntity.ok(updatedTrain);
    }
}
