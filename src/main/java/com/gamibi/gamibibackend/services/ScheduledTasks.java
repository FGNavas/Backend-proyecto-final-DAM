package com.gamibi.gamibibackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Clase que contiene las tareas programadas para ejecutarse en segundo plano de manera automática.
 */
@Service
public class ScheduledTasks {

    @Autowired
    private ApiDataFetcherService apiDataFetcherService;

    /**
     * Método programado para ejecutarse diariamente a la medianoche.
     * Llama al servicio para recuperar y almacenar datos.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Se ejecuta a las 00:00 todos los días
    public void fetchDailyData() {
        apiDataFetcherService.fetchAndStoreData();
    }
}