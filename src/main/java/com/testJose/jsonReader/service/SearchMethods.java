package com.testJose.jsonReader.service;

import com.testJose.jsonReader.domain.Country;
import com.testJose.jsonReader.repository.CountryRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchMethods {
    private CountryRepository countryRepository;

    public SearchMethods(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    public boolean countryNotFounded(String departure, String arrival){

        Boolean founded= true;

        String departureCountry= null;
        String arrivalCountry= null;

        for(Country country : countryRepository.findAll()) {
            if (country.getCca3().equals(departure)) {
                departureCountry=country.getRegion();
                if(country.getBorders().length==0){
                    founded=false;
                }
            }
            if (country.getCca3().equals(arrival) ) {
                arrivalCountry=country.getRegion();
                if(country.getBorders().length==0){
                    founded=false;
                }
            }

        }
        if(departureCountry.equals("Americas") && !arrivalCountry.equals("Americas")){
            founded = false;

        }else
        if(((departureCountry.equals("Europe"))
                || (departureCountry.equals("Asia"))
                || (departureCountry.equals("Africa"))) &&  (arrivalCountry.equals("Americas"))  ){
            founded = false;
        }
        return founded;
    }

    public List<String> pathCountry(String departure, String arrival) {

        List<Double> pathDistances=new ArrayList<>();
        List<String> pathCountries=new ArrayList<>();

        Double departureLatitud=null;
        Double departureLongitud=null;
        Double arrivalLatitud=null;
        Double arrivalLongitud=null;
        Double distance;
        Double savedDistance;

        for (Country country : countryRepository.findAll()) {
            if (country.getCca3().equals(departure)) {
                departureLatitud= Double.valueOf(Arrays.asList(country.getLatlng()).get(0));
                departureLongitud= Double.valueOf(Arrays.asList(country.getLatlng()).get(1));
            }
            if (country.getCca3().equals(arrival)) {
                arrivalLatitud= Double.valueOf(Arrays.asList(country.getLatlng()).get(0));
                arrivalLongitud= Double.valueOf(Arrays.asList(country.getLatlng()).get(1));
            }
        }

        savedDistance= Math.sqrt( Math.pow((departureLatitud-arrivalLatitud),2)
                +Math.pow((departureLongitud-arrivalLongitud),2) );
        pathDistances.add(savedDistance);
        pathCountries.add(departure);




        while (savedDistance != 0) {
            for (Country country : countryRepository.findAll()) {
                if(country.getCca3().equals(departure)){
                    System.out.println(country.getCca3());
                    for(int i= 0;i < Arrays.asList(country.getBorders()).size(); i++){
                        System.out.println(Arrays.asList(country.getBorders()).get(i)+"linea 88");
                        for(Country borderCountries : countryRepository.findAll()){


                            if(borderCountries.getCca3().equals(Arrays.asList(country.getBorders()).get(i))){
                                departureLatitud= Double.valueOf(Arrays.asList(borderCountries.getLatlng()).get(0));
                                departureLongitud= Double.valueOf(Arrays.asList(borderCountries.getLatlng()).get(1));

                                distance= Math.sqrt( Math.pow((departureLatitud-arrivalLatitud),2)
                                        +Math.pow((departureLongitud-arrivalLongitud),2) );
                                System.out.println(distance+"distance");
                                if(distance<=savedDistance ){
                                    savedDistance=distance;
                                    departure=borderCountries.getCca3();
                                    System.out.println("condicion sustitucion");
                                    System.out.println(savedDistance);

                                }
                            }



                        }


                    }
                    pathCountries.add(departure);
                    pathDistances.add(savedDistance);
                    System.out.println(pathCountries+"final del loop");
                }
            }
        }

        pathCountries.remove(pathCountries.size()-1);
        pathDistances.remove(pathDistances.size()-1);


        return pathCountries;
    }
}