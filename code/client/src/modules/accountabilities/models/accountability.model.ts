export class Accountability {

	[key: string]: string | number | null | undefined;
	pos?: number | null;
	id!: number | null;
	accountabilityTypeId!: number | null;
	accountabilityRuleId!: number | null;
	parentPartyId!: number | null;
	parentPartyName?: string | null;
	subsidiaryPartyId!: number | null;
	subsidiaryPartyName?: string | null;
	version!: number | null;

    constructor(options?: Partial<Accountability>) {
        Object.assign(this, options);
    }	
}
