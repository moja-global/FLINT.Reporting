export class AccountabilityRule {

	[key: string]: string | number | null;
	pos!: number | null;
	id!: number | null;
	accountabilityTypeId!: number | null;
	level!: number | null;
	parentPartyTypeId!: number | null;
	subsidiaryPartyTypeId!: number | null;
	subsidiaryPartyTypeName!: string | null;
	version!: number | null;

    constructor(options?: Partial<AccountabilityRule>) {
        Object.assign(this, options);
    }	
}