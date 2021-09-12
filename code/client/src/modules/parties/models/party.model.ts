export class Party {

	[key: string]: string | number | null;
	pos!: number | null;
	id!: number | null;
	partyTypeId!: number | null;
	name!: string | null;
	version!: number | null;

    constructor(options?: Partial<Party>) {
        Object.assign(this, options);
    }	
}