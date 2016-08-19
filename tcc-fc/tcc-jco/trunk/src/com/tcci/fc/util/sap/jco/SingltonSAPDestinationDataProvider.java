package com.tcci.fc.util.sap.jco;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;


/**
 *Use DestinationDataProvider to make SAP connection.
 */
public class SingltonSAPDestinationDataProvider implements DestinationDataProvider {
    
    protected static final Logger logger = Logger.getLogger( JCoConnection.class.getCanonicalName() );
    
    private static SingltonSAPDestinationDataProvider destinationDataProvider  = null;
    private static HashMap<String, Properties> destinations = new HashMap<String, Properties>();
    
    
    private SingltonSAPDestinationDataProvider(){}
    
    public static SingltonSAPDestinationDataProvider getSingltonSAPDestinationDataProvider(){
        if (null==destinationDataProvider){
            destinationDataProvider = new SingltonSAPDestinationDataProvider();
        }
        
        return destinationDataProvider;
    }
    
    
    @Override
    public Properties getDestinationProperties(String destinationName) {
        if (null==destinations){
            throw new RuntimeException("getDestinationProperties destinations is null !");
        }        
        if (!destinations.isEmpty() && destinations.containsKey(destinationName)) {
            return destinations.get(destinationName);
        } else {
            // throw new RuntimeException("getDestinationProperties destinationName = " + destinationName + " is not available !");
            return null;
        }
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }

    /**
     *Add new destination
     *@param properties holds all the required data for a destination
     **/
    void addDestination(String destinationName, Properties properties) {
            destinations.put(destinationName, properties);
    }
    
    public void removeDestination(String destinationName){
        destinations.remove(destinationName);
    }
    
    public Map<String, Properties> getAllDestinations(){
        return destinations;
    }
   
}
