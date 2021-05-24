export class Unit {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	unitCategoryId: number | undefined | null;
	name: string | undefined | null;
	plural: string | undefined | null;
	symbol: string | undefined | null;
	scaleFactor: number | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<Unit>) {
        Object.assign(this, options);
    }	
}
