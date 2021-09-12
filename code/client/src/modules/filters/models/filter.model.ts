import { AccountabilityRule } from "@modules/accountabilities-rules/models/accountability-rule.model";
import { Accountability } from "@modules/accountabilities/models/accountability.model";

export class Filter {

	// The currently selected accountability type
	accountabilityTypeId: number | null = -1;

	// The current hierarchical level in the selected accountability type
	// Should start at 0 and stepwisely increase as one drills down the hierarchy
	// Should also stepwisely decrease to 0 as one drills up the hierarchy
	level: number = 0;	

	// The accountability rules corresponding to the currently selected accountability type
	accountabilitiesRules: AccountabilityRule[] = [];

	// The accountability rule at the current hierarchical level
	accountabilityRule?: AccountabilityRule | null;

	// The incrementally appended ids of the parent parties as one drills down the accountability hierarchy
	parentPartiesIds: number[] = [];	

	// The accountabilities corresponding to the currently selected accountability type / parent party
	accountabilities: Accountability[] = [];

	// The current subsidiary party id
	subsidiaryPartyId: number | null = -1;

	constructor(options?: Partial<Filter>) {
		Object.assign(this, options);
	}
}
