export class Party {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	partyTypeId: number | undefined | null;
	id: number | undefined | null;
	name: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<Party>) {
        Object.assign(this, options);
    }	
}