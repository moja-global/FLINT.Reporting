package global.moja.taskmanager.daos;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Jacksonized
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class DataAggregationRequest {

    private Long taskId;
    private Long databaseId;
    private Long parentPartyId;
    private Long accountabilityTypeId;

}
