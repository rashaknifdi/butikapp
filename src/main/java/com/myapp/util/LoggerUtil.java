package com.myapp.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Denna klass används för att logga information och felmeddelanden på ett enhetligt sätt i hela projektet.
public class LoggerUtil {

    // Skapar en logger som är kopplad till denna klass.
    // Loggern används för att skriva ut meddelanden till konsolen.
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    //Loggar ett informationsmeddelande.
     //Används för att visa att något har lyckats eller att en viktig händelse har inträffat.
    public static void logInfo(String message) {
        logger.info(message);
    }

    //Loggar ett felmeddelande.
     //Används för att visa att något har gått fel eller att ett undantag har inträffat.
    public static void logError(String message) {logger.error(message);
    }
}
