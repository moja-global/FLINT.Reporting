package global.moja.businessintelligence.daos;

import global.moja.businessintelligence.models.QuantityObservation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Jacksonized
@Builder
@Setter
@Getter
@EqualsAndHashCode
public class ProcessingResponse {
    private Long taskId;
    private Long databaseId;
    private Long partyId;
    private int statusCode;
    private List<QuantityObservation> observations;
}