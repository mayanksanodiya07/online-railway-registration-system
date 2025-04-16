package com.train.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
		try {
			Train savedTrain = trainService.addTrain(train);
			return ResponseEntity.ok(savedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@PostMapping("/bulk")
	public ResponseEntity<List<Train>> addMultipleTrains(@RequestBody List<Train> trains) {
		try {
			List<Train> savedTrains = trainService.addMultipleTrains(trains);
			return ResponseEntity.ok(savedTrains);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@GetMapping
	public ResponseEntity<List<Train>> getAllTrains() {
		try {
			List<Train> trains = trainService.getAllTrains();
			return ResponseEntity.ok(trains);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
		try {
			System.out.println(id);
			Train train = trainService.getTrainById(id);
			return ResponseEntity.ok(train);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<Train>> searchTrains(@RequestParam String source, @RequestParam String destination) {
		try {
			List<Train> trains = trainService.searchTrains(source, destination);
			return ResponseEntity.ok(trains);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTrainById(@PathVariable Long id) {
		try {
			trainService.deleteTrainById(id);
			return ResponseEntity.ok("Train deleted successfully");
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		}
	}

	@DeleteMapping
	public ResponseEntity<String> deleteAllTrains() {
		try {
			trainService.deleteAllTrains();
			return ResponseEntity.ok("All trains deleted successfully");
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		}
	}

	@PatchMapping("/{id}/source")
	public ResponseEntity<Train> updateTrainSource(@PathVariable Long id, @RequestParam String source) {
		try {
			Train updatedTrain = trainService.updateTrainSource(id, source);
			return ResponseEntity.ok(updatedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@PatchMapping("/{id}/destination")
	public ResponseEntity<Train> updateTrainDestination(@PathVariable Long id, @RequestParam String destination) {
		try {
			Train updatedTrain = trainService.updateTrainDestination(id, destination);
			return ResponseEntity.ok(updatedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@PatchMapping("/{id}/source-destination")
	public ResponseEntity<Train> updateTrainSourceAndDestination(@PathVariable Long id, @RequestParam String source,
			@RequestParam String destination) {
		try {
			Train updatedTrain = trainService.updateTrainSourceAndDestination(id, source, destination);
			return ResponseEntity.ok(updatedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@PatchMapping("/{id}/departure-time")
	public ResponseEntity<Train> updateTrainDepartureTime(@PathVariable Long id,
			@RequestParam LocalDateTime departureTime) {
		try {
			Train updatedTrain = trainService.updateTrainDepartureTime(id, departureTime);
			return ResponseEntity.ok(updatedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@PatchMapping("/{id}/arrival-time")
	public ResponseEntity<Train> updateTrainArrivalTime(@PathVariable Long id,
			@RequestParam LocalDateTime arrivalTime) {
		try {
			Train updatedTrain = trainService.updateTrainArrivalTime(id, arrivalTime);
			return ResponseEntity.ok(updatedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@PatchMapping("/{id}/times")
	public ResponseEntity<Train> updateTrainTimes(@PathVariable Long id, @RequestParam LocalDateTime departureTime,
			@RequestParam LocalDateTime arrivalTime) {
		try {
			Train updatedTrain = trainService.updateTrainTimes(id, departureTime, arrivalTime);
			return ResponseEntity.ok(updatedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
	}

	@PatchMapping("/{id}/availableSeats")
	public ResponseEntity<Train> updateAvailableSeats(@PathVariable Long id, @RequestParam int availableSeats) {
		try {
			Train updatedTrain = trainService.updateAvailableSeats(id, availableSeats);
			return ResponseEntity.ok(updatedTrain);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(null);
		}
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errors);
	}

}