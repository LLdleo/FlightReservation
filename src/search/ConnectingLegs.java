package search;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * @author Jackson Powell
 * @since 2020-05-01
 * Responsibilities: Aggregate list of search.ConnectingLegs.
 * Significant association: search.ConnectingLeg for objects that this aggregates and ArrayList for the functionality of this object.
 */
public class ConnectingLegs extends ArrayList<ConnectingLeg> {
    private static final long serialVersionUID = 1L;
}
