package global.moja.businessintelligence.daos;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

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
public class ProcessingRequest {
    private Long taskId;
    private Long databaseId;
    private Long partyId;

}