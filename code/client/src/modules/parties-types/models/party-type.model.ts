export class PartyType {

	[key: string]: string | number | null;
	pos!: number | null;
	id!: number | null;
	name!: string | null;
	version!: number | null;

    constructor(options?: Partial<PartyType>) {
        Object.assign(this, options);
    }	
}