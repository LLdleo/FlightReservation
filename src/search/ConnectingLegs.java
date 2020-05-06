package search;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * Aggregate list of search.ConnectingLegs.
 * Significant association: search.ConnectingLeg for objects that this aggregates and ArrayList for the functionality of this object.
 *
 * @author Jackson Powell
 * @since 2020-05-01
 */
public class ConnectingLegs extends ArrayList<ConnectingLeg> {
    private static final long serialVersionUID = 1L;
}
