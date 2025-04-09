package com.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(url="http://localhost:8083", name = "TRAIN-SERVICE")
public interface TrainClient {
	
	@GetMapping("/trains/{id}")
	ResponseEntity<Map<String, Object>> getTrainById(@PathVariable("id") Long id);

	@PostMapping("/trains/{id}/book")
	ResponseEntity<String> bookSeats(@PathVariable("id") Long id, @RequestParam("seats") int seats);
}
