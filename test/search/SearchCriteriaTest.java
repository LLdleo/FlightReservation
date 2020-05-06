package search;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.time.LocalDate;

/**
 * @author Jackson Powell
 * @since 2020-05-06
 * Responsibilities: Test SearchCriteria validation.
 * Significant associations: SearchCriteria for functionality to test and CreatePossibleFlights for what criteria is needed to search for flights.
 */
public class SearchCriteriaTest {

    @Test
    public void testValidation() {
        try{
            new SearchCriteria("BOS","BOs", LocalDate.of(2020,5,10),true);
            Assert.fail();
        }catch(InvalidParameterException e){
            Assert.assertTrue(true);
        }
        try{
            new SearchCriteria("BOS","BOss", LocalDate.of(2020,5,10),true);
            Assert.fail();
        }catch(InvalidParameterException e){
            Assert.assertTrue(true);
        }
        try{
            new SearchCriteria("BOSs","BOs", LocalDate.of(2020,5,10),true);
            Assert.fail();
        }catch(InvalidParameterException e){
            Assert.assertTrue(true);
        }
        try{
            new SearchCriteria("BOS","CLE", null,true);
            Assert.fail();
        }catch(InvalidParameterException e){
            Assert.assertTrue(true);
        }
        try{
            new SearchCriteria("BO","BOs", LocalDate.of(2020,5,10),true);
            Assert.fail();
        }catch(InvalidParameterException e){
            Assert.assertTrue(true);
        }
        SearchCriteria criteria;
        try{
            criteria = new SearchCriteria("BOS","CLE", LocalDate.of(2020,5,10),false);
            Assert.assertEquals(criteria.getSearchAirportCode(),"CLE");
            Assert.assertEquals(criteria.getSearchAirportCode(),criteria.getArrivalAirportCode());
            criteria = new SearchCriteria("BOS","CLE", LocalDate.of(2020,5,10),true);
            Assert.assertEquals(criteria.getSearchAirportCode(),"BOS");
            Assert.assertEquals(criteria.getSearchAirportCode(),criteria.getDepartureAirportCode());

        }catch(InvalidParameterException e){
            Assert.fail();
        }

    }
}
