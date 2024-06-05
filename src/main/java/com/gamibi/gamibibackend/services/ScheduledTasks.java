package com.gamibi.gamibibackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasks {

    @Autowired
    private ApiDataFetcherService apiDataFetcherService;

  //  @Scheduled(cron = "0 0 0 * * ?") // Se ejecuta a medianoche todos los días
  @Scheduled(cron = "0 10 21 * * *") // Se ejecutará todos los días a las 20:30
    public void fetchDailyData() {
        apiDataFetcherService.fetchAndStoreData();
    }
}