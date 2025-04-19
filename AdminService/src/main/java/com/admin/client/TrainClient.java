package com.admin.client;

import com.admin.model.Train;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(url="http://localhost:8083", name="TRAIN-SERVICE")
public interface TrainClient {

    @PostMapping("/trains/bulk")
    List<Train> addMultipleTrains(@RequestBody List<Train> trains);

    @DeleteMapping("/trains/{id}")
    void deleteTrain(@PathVariable("id") Long id);

    @DeleteMapping("/trains")
    void deleteAllTrains();

    @PatchMapping("/trains/{id}/source")
    Train updateTrainSource(@PathVariable("id") Long id, @RequestParam String source);

    @PatchMapping("/trains/{id}/destination")
    Train updateTrainDestination(@PathVariable("id") Long id, @RequestParam String destination);

    @PatchMapping("/trains/{id}/source-destination")
    Train updateTrainSourceAndDestination(@PathVariable("id") Long id, @RequestParam String source, @RequestParam String destination);

    @PatchMapping("/trains/{id}/departure-time")
    Train updateTrainDepartureTime(@PathVariable("id") Long id, @RequestParam String departureTime);

    @PatchMapping("/trains/{id}/arrival-time")
    Train updateTrainArrivalTime(@PathVariable("id") Long id, @RequestParam String arrivalTime);

    @PatchMapping("/trains/{id}/times")
    Train updateTrainTimes(@PathVariable("id") Long id, @RequestParam String departureTime, @RequestParam String arrivalTime);

    @PatchMapping("/trains/{id}/availableSeats")
    Train updateTrainAvailableSeats(@PathVariable("id") Long id, @RequestParam int availableSeats);
}
