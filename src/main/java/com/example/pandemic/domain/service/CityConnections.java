package com.example.pandemic.domain.service;

import static com.example.pandemic.domain.model.City.Name.*;

import com.example.pandemic.domain.model.City;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CityConnections {
    private CityConnections() {
    }

    private static final Map<City.Name, Set<City.Name>> connections = initConnections();

    public static boolean hasConnection(City.Name city1, City.Name city2) {
        return getConnections(city1).contains(city2);
    }

    public static Set<City.Name> getConnections(City.Name city) {
        return connections.get(city);
    }

    private static Map<City.Name, Set<City.Name>> initConnections() {
        var connections = new HashMap<City.Name, Set<City.Name>>(48);

        // blue
        connections.put(ATLANTA, Set.of(CHICAGO, MIAMI, WASHINGTON));
        connections.put(CHICAGO, Set.of(ATLANTA, SAN_FRANCISCO, MONTREAL, LOS_ANGELES));
        connections.put(MONTREAL, Set.of(CHICAGO, WASHINGTON, NEW_YORK));
        connections.put(WASHINGTON, Set.of(ATLANTA, MIAMI, NEW_YORK, MONTREAL));
        connections.put(NEW_YORK, Set.of(MONTREAL, WASHINGTON, MADRID, LONDON));
        connections.put(SAN_FRANCISCO, Set.of(CHICAGO, LOS_ANGELES, TOKYO, MANILA));
        connections.put(MADRID, Set.of(NEW_YORK, LONDON, PARIS, ALGIERS, SAO_PAOLO));
        connections.put(LONDON, Set.of(NEW_YORK, MADRID, PARIS, ESSEN));
        connections.put(PARIS, Set.of(LONDON, MADRID, ESSEN, ALGIERS, MILAN));
        connections.put(MILAN, Set.of(PARIS, ISTANBUL, ESSEN));
        connections.put(ESSEN, Set.of(PARIS, LONDON, MILAN, PETERSBURG));
        connections.put(PETERSBURG, Set.of(ESSEN, ISTANBUL, MOSCOW));

        // yellow
        connections.put(MIAMI, Set.of(ATLANTA, WASHINGTON, BOGOTA, MEXICO));
        connections.put(MEXICO, Set.of(MIAMI, BOGOTA, LIMA, LOS_ANGELES, CHICAGO));
        connections.put(BOGOTA, Set.of(MIAMI, SAO_PAOLO, MEXICO, LIMA, BUENOS_AIRES));
        connections.put(LOS_ANGELES, Set.of(MEXICO, SAN_FRANCISCO, SYDNEY, CHICAGO));
        connections.put(LIMA, Set.of(MEXICO, BOGOTA, SANTIAGO));
        connections.put(SANTIAGO, Set.of(LIMA));
        connections.put(BUENOS_AIRES, Set.of(SAO_PAOLO, BOGOTA));
        connections.put(SAO_PAOLO, Set.of(BUENOS_AIRES, BOGOTA, MADRID, LAGOS));
        connections.put(LAGOS, Set.of(SAO_PAOLO, KINSHASA, KHARTOUM));
        connections.put(KINSHASA, Set.of(LAGOS, JOHANNESBURG, KHARTOUM));
        connections.put(JOHANNESBURG, Set.of(KINSHASA, KHARTOUM));
        connections.put(KHARTOUM, Set.of(LAGOS, KINSHASA, JOHANNESBURG, CAIRO));

        // red
        connections.put(BANGKOK, Set.of(KOLKATA, HONG_KONG, CHENNAI, HO_CHI_MINH, JAKARTA));
        connections.put(JAKARTA, Set.of(BANGKOK, CHENNAI, HO_CHI_MINH, SYDNEY));
        connections.put(HO_CHI_MINH, Set.of(JAKARTA, BANGKOK, HONG_KONG, MANILA));
        connections.put(MANILA, Set.of(SYDNEY, HO_CHI_MINH, HONG_KONG, TAIPEI));
        connections.put(SYDNEY, Set.of(JAKARTA, MANILA, LOS_ANGELES));
        connections.put(HONG_KONG, Set.of(KOLKATA, BANGKOK, TAIPEI, MANILA, SHANGHAI, HO_CHI_MINH));
        connections.put(TAIPEI, Set.of(OSAKA, MANILA, HONG_KONG, SHANGHAI));
        connections.put(SHANGHAI, Set.of(HONG_KONG, TAIPEI, BEIJING, SEOUL, TOKYO));
        connections.put(BEIJING, Set.of(SEOUL, SHANGHAI));
        connections.put(SEOUL, Set.of(SHANGHAI, BEIJING, TOKYO));
        connections.put(TOKYO, Set.of(SHANGHAI, SEOUL, OSAKA, SAN_FRANCISCO));
        connections.put(OSAKA, Set.of(TOKYO, TAIPEI));

        // black
        connections.put(CAIRO, Set.of(KHARTOUM, ALGIERS, ISTANBUL, BAGHDAD, RIYADH));
        connections.put(ALGIERS, Set.of(CAIRO, PARIS, MADRID, ISTANBUL));
        connections.put(ISTANBUL, Set.of(MILAN, ALGIERS, PETERSBURG, MOSCOW, BAGHDAD, CAIRO));
        connections.put(BAGHDAD, Set.of(ISTANBUL, TEHRAN, CAIRO, RIYADH, KARACHI));
        connections.put(MOSCOW, Set.of(PETERSBURG, ISTANBUL, TEHRAN));
        connections.put(RIYADH, Set.of(CAIRO, BAGHDAD, KARACHI));
        connections.put(TEHRAN, Set.of(MOSCOW, BAGHDAD, KARACHI, DELHI));
        connections.put(KARACHI, Set.of(BAGHDAD, TEHRAN, RIYADH, MUMBAI, DELHI));
        connections.put(DELHI, Set.of(TEHRAN, KARACHI, MUMBAI, KOLKATA, CHENNAI));
        connections.put(KOLKATA, Set.of(DELHI, CHENNAI, BANGKOK, HONG_KONG));
        connections.put(CHENNAI, Set.of(MUMBAI, DELHI, KOLKATA, BANGKOK, JAKARTA));
        connections.put(MUMBAI, Set.of(KARACHI, DELHI, CHENNAI));

        return connections;
    }
}
