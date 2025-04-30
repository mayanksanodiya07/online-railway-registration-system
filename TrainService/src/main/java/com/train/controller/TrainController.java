package com.train.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.train.entity.Train;
import com.train.service.TrainService;

import jakarta.validation.Valid;
 

@RestController
@RequestMapping("/trains")
public class TrainController {

	@Autowired
	private TrainService trainService;

	@PostMapping
	public ResponseEntity<Train> addTrain(@Valid @RequestBody Train train) {
			Train savedTrain = trainService.addTrain(train);
			return ResponseEntity.ok(savedTrain);
	}

	@PostMapping("/bulk")
	public ResponseEntity<List<Train>> addMultipleTrains( @RequestBody List<@Valid Train> trains) {
			List<Train> savedTrains = trainService.addMultipleTrains(trains);
			return ResponseEntity.ok(savedTrains);
	}

	@GetMapping
	public ResponseEntity<List<Train>> getAllTrains() {
			List<Train> trains = trainService.getAllTrains();
			return ResponseEntity.ok(trains);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
			Train train = trainService.getTrainById(id);
			return ResponseEntity.ok(train);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Train>> searchTrains(@RequestParam String source, @RequestParam String destination) {
			List<Train> trains = trainService.searchTrains(source, destination);
			return ResponseEntity.ok(trains);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTrainById(@PathVariable Long id) {
			trainService.deleteTrainById(id);
			return ResponseEntity.ok("Train deleted successfully");
	}

	@DeleteMapping
	public ResponseEntity<String> deleteAllTrains() {
			trainService.deleteAllTrains();
			return ResponseEntity.ok("All trains deleted successfully");
	}

	@PatchMapping("/{id}/source")
	public ResponseEntity<Train> updateTrainSource(@PathVariable Long id, @RequestParam String source) {
			Train updatedTrain = trainService.updateTrainSource(id, source);
			return ResponseEntity.ok(updatedTrain);
	}

	@PatchMapping("/{id}/destination")
	public ResponseEntity<Train> updateTrainDestination(@PathVariable Long id, @RequestParam String destination) {
			Train updatedTrain = trainService.updateTrainDestination(id, destination);
			return ResponseEntity.ok(updatedTrain);
	}

	@PatchMapping("/{id}/source-destination")
	public ResponseEntity<Train> updateTrainSourceAndDestination(@PathVariable Long id, @RequestParam String source,
			@RequestParam String destination) {
			Train updatedTrain = trainService.updateTrainSourceAndDestination(id, source, destination);
			return ResponseEntity.ok(updatedTrain);
	}

	@PatchMapping("/{id}/departure-time")
	public ResponseEntity<Train> updateTrainDepartureTime(@PathVariable Long id,
			@RequestParam LocalDateTime departureTime) {
			Train updatedTrain = trainService.updateTrainDepartureTime(id, departureTime);
			return ResponseEntity.ok(updatedTrain);
	}

	@PatchMapping("/{id}/arrival-time")
	public ResponseEntity<Train> updateTrainArrivalTime(@PathVariable Long id,
			@RequestParam LocalDateTime arrivalTime) {
			Train updatedTrain = trainService.updateTrainArrivalTime(id, arrivalTime);
			return ResponseEntity.ok(updatedTrain);
	}

	@PatchMapping("/{id}/times")
	public ResponseEntity<Train> updateTrainTimes(@PathVariable Long id, @RequestParam LocalDateTime departureTime,
			@RequestParam LocalDateTime arrivalTime) {
			Train updatedTrain = trainService.updateTrainTimes(id, departureTime, arrivalTime);
			return ResponseEntity.ok(updatedTrain);
	}

	@PatchMapping("/{id}/availableSeats")
	public ResponseEntity<Train> updateAvailableSeats(@PathVariable Long id, @RequestParam int availableSeats) {
			Train updatedTrain = trainService.updateAvailableSeats(id, availableSeats);
			return ResponseEntity.ok(updatedTrain);
	}

	@PostMapping("/{id}/book")
	public ResponseEntity<String> bookSeats(@PathVariable Long id, @RequestParam int seats) {
	    trainService.bookSeats(id, seats); // If an exception is thrown, Spring handles it globally unless you override it
	    return ResponseEntity.ok("Seats booked successfully!");
	}

	
	@PutMapping("/{id}/release-seats")
	public ResponseEntity<String> releaseSeats(@PathVariable Long id, @RequestParam int seats) {
	    trainService.releaseSeats(id, seats);
	    return ResponseEntity.ok("Seats released successfully");
	}
}