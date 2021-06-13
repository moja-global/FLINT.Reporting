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
public class DataProcessingResponse {
    private Long taskId;
    private Long databaseId;
    private Long partyId;

    @EqualsAndHashCode.Exclude
    private int statusCode;
}