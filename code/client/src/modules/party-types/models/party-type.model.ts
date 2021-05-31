export class PartyType {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	parentPartyTypeId: number | undefined | null;
	id: number | undefined | null;
	name: string | undefined | null;
	plural: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<PartyType>) {
        Object.assign(this, options);
    }	
}