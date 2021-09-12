export class AccountabilityType {

	[key: string]: string | number | null;
	pos!: number | null;
	id!: number | null;
	name!: string | null;
	version!: number | null;

    constructor(options?: Partial<AccountabilityType>) {
        Object.assign(this, options);
    }	
}